package com.fpoly.backend.services.impl;

import com.fpoly.backend.entities.Schedule;
import com.fpoly.backend.repository.ScheduleRepository;
import com.fpoly.backend.services.IdentifyUserAccessService;
import com.fpoly.backend.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public Schedule findById(Integer id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Map<String, Object>> getScheduleByDateRange(LocalDate startDate, LocalDate endDate) {
        Integer studentId = identifyUserAccessService.getStudent().getId();
        return scheduleRepository.getScheduleByDateRange(studentId, startDate, endDate);
    }
}
