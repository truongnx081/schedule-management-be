package com.fpoly.backend.services;

import com.fpoly.backend.dto.StudyInDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudyInService {
    List<StudyInDTO> findAllByBlockAndSemesterAnhYear();
}
