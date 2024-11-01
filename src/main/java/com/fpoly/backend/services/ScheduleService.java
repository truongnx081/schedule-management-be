package com.fpoly.backend.services;

import com.fpoly.backend.entities.Schedule;
import org.springframework.stereotype.Service;

@Service
public interface ScheduleService {
    Schedule findById(Integer id);
}
