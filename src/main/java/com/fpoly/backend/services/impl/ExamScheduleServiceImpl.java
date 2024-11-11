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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<ExamScheduleDTO> getAll() {
        return examScheduleRepository.findAll().stream()
                .map(examScheduleMapper::toDTO).toList();
    }

    @Override
    public void importExamSchedule(List<ExamScheduleDTO> listRequestDTO) {
        for (ExamScheduleDTO request : listRequestDTO) {

            // Kiểm tra sự tồn tại của mã clazz
            if (examScheduleRepository.existsById(request.getId())) {
                throw new RuntimeException("Exam schedule was existed");
            }

            // Tạo và lưu đối tượng ExamSchedule
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

            // Lưu examSchedule
            examScheduleRepository.save(examSchedule);
        }
    }
}
