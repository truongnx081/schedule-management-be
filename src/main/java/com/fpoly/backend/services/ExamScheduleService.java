package com.fpoly.backend.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public interface ExamScheduleService {
    List<Map<String, Object>> getExameScheduleByDateRange(LocalDate startDate, LocalDate endDate);
}
