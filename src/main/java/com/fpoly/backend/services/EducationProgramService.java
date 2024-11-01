package com.fpoly.backend.services;

import com.fpoly.backend.entities.EducationProgram;
import org.springframework.stereotype.Service;

@Service
public interface EducationProgramService {
    EducationProgram findById(Integer id);
}
