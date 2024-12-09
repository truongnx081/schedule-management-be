package com.fpoly.backend.services;

import com.fpoly.backend.dto.SemesterDTO;
import com.fpoly.backend.entities.Semester;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SemesterService {
    Semester findById(String semester);

    List<SemesterDTO> getAllSemester();
}
