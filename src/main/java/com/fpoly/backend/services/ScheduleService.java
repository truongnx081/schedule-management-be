package com.fpoly.backend.services;

import com.fpoly.backend.dto.ScheduleDTO;
import com.fpoly.backend.entities.Schedule;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public interface ScheduleService {
    Schedule findById(Integer id);

    List<Map<String, Object>> getScheduleByDateRange(LocalDate startDate, LocalDate endDate);

    ScheduleDTO putScheduleStatus(ScheduleDTO request, Integer scheduleId);

    List<Map<String,Object>>getClazzsByScheduleStatus();

}
