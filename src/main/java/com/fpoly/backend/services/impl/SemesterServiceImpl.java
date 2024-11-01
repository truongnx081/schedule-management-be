package com.fpoly.backend.services.impl;

import com.fpoly.backend.entities.Semester;
import com.fpoly.backend.repository.SemesterRepository;
import com.fpoly.backend.services.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {

    @Autowired
    SemesterRepository semesterRepository;
    @Override
    public Semester findById(String semester) {
        return semesterRepository.findById(semester).orElse(null);
    }
}
