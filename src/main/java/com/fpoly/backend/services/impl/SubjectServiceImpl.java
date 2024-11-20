package com.fpoly.backend.services.impl;

import com.fpoly.backend.entities.Subject;
import com.fpoly.backend.repository.SubjectRepository;
import com.fpoly.backend.services.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    SubjectRepository subjectRepository;
    @Override
    public Subject findById(Integer id) {
        return subjectRepository.findById(id).orElse(null);
    }

    @Override
    public List<Map<String, Object>> getAllSubjectBySpecializationId(Integer specializationId) {
        return subjectRepository.getAllSubjectBySpecializationId(specializationId);
    }

    @Override
    public List<Map<String, Object>> findAllSubject() {
        return subjectRepository.findAllSubject();
    }

    @Override
    public List<Map<String, Object>> getSubjectDetail(Integer subjectId) {
        return subjectRepository.getSubjectDetailById(subjectId);
    }
}
