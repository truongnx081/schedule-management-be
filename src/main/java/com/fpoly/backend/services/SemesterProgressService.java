package com.fpoly.backend.services;

import com.fpoly.backend.dto.SemesterProgressDTO;
import com.fpoly.backend.entities.SemesterProgress;
import org.springframework.stereotype.Service;

@Service
public interface SemesterProgressService {
    SemesterProgressDTO createSemesterProgress(SemesterProgressDTO semesterProgressDTO);

    void deleteSP(Integer semesterProgressId);

    SemesterProgressDTO updateSemesterProgress(Integer semesterProgressId, SemesterProgressDTO semesterProgressDTO);

    SemesterProgress findActivedProgressTrue();
}
