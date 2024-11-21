package com.fpoly.backend.services;

import com.fpoly.backend.dto.AdminDTO;
import com.fpoly.backend.dto.InstructorDTO;
import com.fpoly.backend.entities.Instructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface InstructorService {
    Instructor findById(Integer id);

    public List<Map<String, Object>> getAllTeachingScheduleByInstructor(LocalDate startDate, LocalDate endDate);
    InstructorDTO getInstructorInfor();
}
