package com.fpoly.backend.services.impl;

import com.fpoly.backend.entities.Subject;
import com.fpoly.backend.repository.SubjectRepository;
import com.fpoly.backend.services.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    SubjectRepository subjectRepository;
    @Override
    public Subject findById(Integer id) {
        return subjectRepository.findById(id).orElse(null);
    }
}
