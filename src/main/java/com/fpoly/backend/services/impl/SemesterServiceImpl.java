package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.SemesterDTO;
import com.fpoly.backend.entities.Semester;
import com.fpoly.backend.mapper.SemesterMapper;
import com.fpoly.backend.repository.SemesterRepository;
import com.fpoly.backend.services.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {

    @Autowired
    SemesterRepository semesterRepository;
    @Autowired
    private SemesterMapper semesterMapper;

    @Override
    public Semester findById(String semester) {
        return semesterRepository.findById(semester).orElse(null);
    }

    @Override
    public List<SemesterDTO> getAllSemester() {
        return semesterRepository.findAll().stream().map(semesterMapper::toDTO).collect(Collectors.toList());
    }
}
