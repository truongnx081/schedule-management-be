package com.fpoly.backend.services;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public interface StudyHistoryService {

    public Map<String ,Integer>getreportLearningProgressByStudentId();
    
    List<Map<String, Object>> getAllStudyHistoryByStudentId();

}
