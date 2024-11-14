package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.ScheduleDTO;
import com.fpoly.backend.entities.Clazz;
import com.fpoly.backend.entities.Major;
import com.fpoly.backend.entities.Schedule;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.entities.Admin;
import com.fpoly.backend.entities.Clazz;
import com.fpoly.backend.entities.ExamSchedule;
import com.fpoly.backend.entities.Schedule;
import com.fpoly.backend.mapper.ScheduleMapper;
import com.fpoly.backend.repository.ClazzRepository;
import com.fpoly.backend.repository.ScheduleRepository;
import com.fpoly.backend.services.IdentifyUserAccessService;
import com.fpoly.backend.services.ScheduleService;
import com.fpoly.backend.until.ExcelUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    private IdentifyUserAccessService identifyUserAccessService;
    @Autowired
    ScheduleMapper scheduleMapper;
    @Autowired
    ClazzRepository clazzRepository;
    @Autowired
    private ClazzRepository clazzRepository;
    @Autowired
    private ExcelUtility excelUtility;

    @Override
    public Schedule findById(Integer id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Map<String, Object>> getScheduleByDateRange(LocalDate startDate, LocalDate endDate) {
        Integer studentId = identifyUserAccessService.getStudent().getId();
        return scheduleRepository.getScheduleByDateRange(studentId, startDate, endDate);
    }

    @Override
    public ScheduleDTO putScheduleStatus(ScheduleDTO request, Integer scheduleId) {

        Schedule schedule= scheduleRepository.findById(scheduleId)
                .orElseThrow(()-> new RuntimeException("Schedule not found"));;
        scheduleMapper.updateSchedule(schedule,request);
        schedule.setId(scheduleId);
        Clazz clazz = clazzRepository.findById(request.getClazzId()).orElseThrow(() ->
                new AppUnCheckedException("Clazz not found", HttpStatus.NOT_FOUND)
        );
        schedule.setClazz(clazz);
        schedule.setStatus(false);

    public ScheduleDTO create(ScheduleDTO request) {
        Schedule schedule = scheduleMapper.toEntity(request);

        Clazz clazz = clazzRepository.findById(request.getClazzId()).orElseThrow(()->
                new RuntimeException("Clazz not found"));
        Admin admin = identifyUserAccessService.getAdmin();

        schedule.setClazz(clazz);
        schedule.setCreateAt(new Date());
        schedule.setCreatedBy(admin.getCode());

        return scheduleMapper.toDTO(scheduleRepository.save(schedule));
    }

    @Override
    public ScheduleDTO update(ScheduleDTO request, Integer id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(()->
                new RuntimeException("Schedule not found"));
        scheduleMapper.updateSchedule(schedule, request);

        Clazz clazz = clazzRepository.findById(request.getClazzId()).orElseThrow(()->
                new RuntimeException("Clazz not found"));
        Admin admin = identifyUserAccessService.getAdmin();

        schedule.setClazz(clazz);
        schedule.setUpdatedAt(new Date());
        schedule.setUpdatedBy(admin.getCode());
        return scheduleMapper.toDTO(scheduleRepository.save(schedule));
    }

    @Override
    public List<Map<String, Object>> getClazzsByScheduleStatus() {
        Integer instructorId = identifyUserAccessService.getInstructor().getId();
        return scheduleRepository.getClazzsByScheduleStatus(instructorId);
      
    public void delete(Integer id) {
        scheduleRepository.deleteById(id);
    }

    @Override
    public ScheduleDTO getOne(Integer id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(()->
                new RuntimeException("Schedule not found"));
        return scheduleMapper.toDTO(schedule);
    }

    @Override
    public List<ScheduleDTO> getAll() {
        return scheduleRepository.findAll().stream()
                .map(scheduleMapper::toDTO).toList();
    }

    @Override
    public void importStudySchedule(MultipartFile file) {
        try {
            List<Schedule> schedulesList = excelUtility.excelToStudyScheduleList(file.getInputStream());
            scheduleRepository.saveAll(schedulesList);
        } catch (IOException ex) {
            throw new RuntimeException("Excel data is failed to store: " + ex.getMessage());
        }
    }
}
