package com.fpoly.backend.services.impl;

import com.fpoly.backend.entities.RetakeSchedule;
import com.fpoly.backend.repository.RetakeScheduleRepository;
import com.fpoly.backend.services.RetakeScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetakeScheduleServiceImpl implements RetakeScheduleService {

    @Autowired
    RetakeScheduleRepository retakeScheduleRepository;
    @Override
    public RetakeSchedule findById(Integer id) {
        return retakeScheduleRepository.findById(id).orElse(null);
    }
}
