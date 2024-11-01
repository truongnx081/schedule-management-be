package com.fpoly.backend.services;

import com.fpoly.backend.entities.RetakeSchedule;
import org.springframework.stereotype.Service;

@Service
public interface RetakeScheduleService {
    RetakeSchedule findById(Integer id);
}
