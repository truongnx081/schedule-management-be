package com.fpoly.backend.services.impl;

import com.fpoly.backend.entities.EducationProgram;
import com.fpoly.backend.repository.EducationProgramRepository;
import com.fpoly.backend.services.EducationProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EducationProgramServiceImpl implements EducationProgramService {

    @Autowired
    EducationProgramRepository educationProgramRepository;

    @Override
    public EducationProgram findById(Integer id) {
        return educationProgramRepository.findById(id).orElse(null);
    }
}
