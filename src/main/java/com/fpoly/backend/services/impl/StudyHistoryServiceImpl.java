package com.fpoly.backend.services.impl;

import com.fpoly.backend.entities.StudyHistory;
import com.fpoly.backend.mapper.StudyHistoryMapper;
import com.fpoly.backend.repository.StudentRepository;
import com.fpoly.backend.repository.StudyHistoryRepository;
import com.fpoly.backend.services.StudyHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyHistoryServiceImpl implements StudyHistoryService {
    @Autowired
    StudyHistoryRepository studyHistoryRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudyHistoryMapper studyHistoryMapper;

    @Override
    public StudyHistory findById(Integer id) {
        return studyHistoryRepository.findById(id).orElse(null);
    }

}
