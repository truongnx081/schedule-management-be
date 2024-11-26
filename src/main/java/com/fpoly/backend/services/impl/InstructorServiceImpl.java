package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.InstructorDTO;
import com.fpoly.backend.entities.Instructor;
import com.fpoly.backend.entities.Student;
import com.fpoly.backend.mapper.InstructorMapper;
import com.fpoly.backend.repository.InstructorRepository;
import com.fpoly.backend.services.IdentifyUserAccessService;
import com.fpoly.backend.services.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {
    @Autowired
    InstructorRepository instructorRepository;

    @Autowired
    IdentifyUserAccessService identifyUserAccessService;
    @Autowired
    private InstructorMapper instructorMapper;

    @Override
    public Instructor findById(Integer id) {
        return instructorRepository.findById(id).orElse(null);
    }

    @Override
    public List<Map<String, Object>> getAllTeachingScheduleByInstructor(LocalDate startDate, LocalDate endDate) {
        Integer instructorId = identifyUserAccessService.getInstructor().getId();
        return instructorRepository.getAllTeachingScheduleByInstructor(instructorId, startDate, endDate);
    }

    @Override
    public InstructorDTO getInstructorInfor() {
        Instructor instructor = identifyUserAccessService.getInstructor();
        return instructorMapper.toDTO(instructor);
    }

    @Override
    public List<InstructorDTO> getAllInstructorBySpecialization(Integer specializationId) {
        return instructorRepository.findAllBySpecializationId(specializationId).stream()
                .map(instructorMapper::toDTO).toList();
    }

}
