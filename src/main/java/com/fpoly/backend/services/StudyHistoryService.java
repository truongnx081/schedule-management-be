package com.fpoly.backend.services;

import com.fpoly.backend.entities.StudyHistory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface StudyHistoryService {
    StudyHistory findById(Integer id);

    List<Map<String, Object>> getAllStudyHistoryByStudentId();
}
