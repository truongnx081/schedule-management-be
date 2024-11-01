package com.fpoly.backend.services;

import com.fpoly.backend.entities.Semester;
import org.springframework.stereotype.Service;

@Service
public interface SemesterService {
    Semester findById(String semester);
}
