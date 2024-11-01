package com.fpoly.backend.services;

import com.fpoly.backend.entities.StudyHistory;
import org.springframework.stereotype.Service;

@Service
public interface StudyHistoryService {
    StudyHistory findById(Integer id);
    
}
