package com.fpoly.backend.services.impl;

import com.fpoly.backend.repository.ExamScheduleRepository;
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

    @Override
    public List<Map<String, Object>> getExameScheduleByDateRange(LocalDate startDate, LocalDate endDate) {
        Integer studentId = identifyUserAccessService.getStudent().getId();
        return examScheduleRepository.getExamScheduleByDateRange(studentId, startDate, endDate);
    }
}
