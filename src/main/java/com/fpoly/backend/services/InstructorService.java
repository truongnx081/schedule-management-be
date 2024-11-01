package com.fpoly.backend.services;

import com.fpoly.backend.entities.Instructor;
import org.springframework.stereotype.Service;

@Service
public interface InstructorService {
    Instructor findById(Integer id);
}
