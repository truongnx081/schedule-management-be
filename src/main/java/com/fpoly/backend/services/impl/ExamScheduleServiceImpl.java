package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.ClazzDTO;
import com.fpoly.backend.dto.ExamScheduleDTO;
import com.fpoly.backend.entities.*;
import com.fpoly.backend.mapper.ExamScheduleMapper;
import com.fpoly.backend.repository.ClazzRepository;
import com.fpoly.backend.repository.ExamScheduleRepository;
import com.fpoly.backend.repository.RoomRepository;
import com.fpoly.backend.repository.ShiftRepository;
import com.fpoly.backend.services.ExamScheduleService;
import com.fpoly.backend.services.IdentifyUserAccessService;
import com.fpoly.backend.until.ExcelUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExamScheduleServiceImpl implements ExamScheduleService {
    @Autowired
    IdentifyUserAccessService identifyUserAccessService;
    @Autowired
    private ExamScheduleRepository examScheduleRepository;
    @Autowired
    private ExamScheduleMapper examScheduleMapper;
    @Autowired
    private ClazzRepository clazzRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private ExcelUtility excelUtility;

    @Override
    public List<Map<String, Object>> getExameScheduleByDateRange(LocalDate startDate, LocalDate endDate) {
        Integer studentId = identifyUserAccessService.getStudent().getId();
        return examScheduleRepository.getExamScheduleByDateRange(studentId, startDate, endDate);
    }

    @Override
    public ExamScheduleDTO create(ExamScheduleDTO request) {
        ExamSchedule examSchedule = examScheduleMapper.toEntity(request);

        Clazz clazz = clazzRepository.findById(request.getClazzId()).orElseThrow(()->
                new RuntimeException("Clazz not found"));
        Room room = roomRepository.findById(request.getRoomId()).orElseThrow(()->
                new RuntimeException("Room not found"));
        Shift shift = shiftRepository.findById(request.getShiftId()).orElseThrow(()->
                new RuntimeException("Shift not found"));

        examSchedule.setClazz(clazz);
        examSchedule.setRoom(room);
        examSchedule.setShift(shift);

        return examScheduleMapper.toDTO(examScheduleRepository.save(examSchedule));
    }

    @Override
    public ExamScheduleDTO update(ExamScheduleDTO request, Integer id) {
        ExamSchedule examSchedule = examScheduleRepository.findById(id).orElseThrow(()->
                new RuntimeException("ExamSchedule not found"));

        examScheduleMapper.updateExamSchedule(examSchedule,request);

        Clazz clazz = clazzRepository.findById(request.getClazzId()).orElseThrow(()->
                new RuntimeException("Clazz not found"));
        Room room = roomRepository.findById(request.getRoomId()).orElseThrow(()->
                new RuntimeException("Room not found"));
        Shift shift = shiftRepository.findById(request.getShiftId()).orElseThrow(()->
                new RuntimeException("Shift not found"));

        examSchedule.setClazz(clazz);
        examSchedule.setRoom(room);
        examSchedule.setShift(shift);

        return examScheduleMapper.toDTO(examScheduleRepository.save(examSchedule));
    }

    @Override
    public void delete(Integer id) {
        examScheduleRepository.deleteById(id);
    }

    @Override
    public ExamScheduleDTO getOne(Integer id) {
        ExamSchedule examSchedule = examScheduleRepository.findById(id).orElseThrow(()->
                new RuntimeException("ExamSchedule not found"));
        return examScheduleMapper.toDTO(examSchedule);
    }


    @Override
    public void importExamSchedule(MultipartFile file) {
        try {
            List<ExamSchedule> examSchedulesList = excelUtility.excelToExamScheduleList(file.getInputStream());
            examScheduleRepository.saveAll(examSchedulesList);
        } catch (IOException ex) {
            throw new RuntimeException("Xuất hiện lỗi trong Excel: " + ex.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getAllBathByClazzInstructor(Integer clazzId) {
        Integer instructorId = identifyUserAccessService.getInstructor().getId();
        return examScheduleRepository.getAllBathByClazzInstructor(clazzId, instructorId);
    }

    @Override
    public List<Map<String, Object>> getAllExamOfAdmin() {
        return examScheduleRepository.getAllExamScheduleOfAdmin();
    }

    @Override
    public List<Map<String, Object>> getAllExamByBlockAndSemesterAndYearAndSpecializationIdOfAdmin(Integer block, String semester, Integer year, Integer specializationId) {
        return examScheduleRepository.getAllExamByBlockAndSemesterAndYearAndSpecializationIdOfAdmin(block, semester, year, specializationId);
    }
}
