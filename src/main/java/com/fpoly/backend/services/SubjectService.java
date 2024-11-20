package com.fpoly.backend.services;

import com.fpoly.backend.entities.Subject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface SubjectService {
    Subject findById(Integer id);

    List<Map<String, Object>> getAllSubjectBySpecializationId(Integer specializationId);

    List<Map<String, Object>> findAllSubject();

    List<Map<String, Object>> getSubjectDetail(Integer subjectId);
}
