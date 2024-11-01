package com.fpoly.backend.services.impl;

import com.fpoly.backend.entities.Instructor;
import com.fpoly.backend.repository.InstructorRepository;
import com.fpoly.backend.services.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {
    @Autowired
    InstructorRepository instructorRepository;

    @Override
    public Instructor findById(Integer id) {
        return instructorRepository.findById(id).orElse(null);
    }
}
