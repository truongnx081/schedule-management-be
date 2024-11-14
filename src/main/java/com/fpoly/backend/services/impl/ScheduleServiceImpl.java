package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.ScheduleDTO;
import com.fpoly.backend.entities.Clazz;
import com.fpoly.backend.entities.Major;
import com.fpoly.backend.entities.Schedule;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.mapper.ScheduleMapper;
import com.fpoly.backend.repository.ClazzRepository;
import com.fpoly.backend.repository.ScheduleRepository;
import com.fpoly.backend.services.IdentifyUserAccessService;
import com.fpoly.backend.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

        return scheduleMapper.toDTO(scheduleRepository.save(schedule));
    }

    @Override
    public List<Map<String, Object>> getClazzsByScheduleStatus() {
        Integer instructorId = identifyUserAccessService.getInstructor().getId();
        return scheduleRepository.getClazzsByScheduleStatus(instructorId);
    }
}
