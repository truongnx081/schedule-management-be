package com.fpoly.backend.services;

import com.fpoly.backend.entities.Subject;
import org.springframework.stereotype.Service;

@Service
public interface SubjectService {
    Subject findById(Integer id);
}
