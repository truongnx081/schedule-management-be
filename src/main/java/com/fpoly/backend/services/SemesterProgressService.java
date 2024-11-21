package com.fpoly.backend.services;

import com.fpoly.backend.dto.SemesterProgressDTO;
import com.fpoly.backend.entities.SemesterProgress;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface SemesterProgressService {
    SemesterProgressDTO createSemesterProgress(SemesterProgressDTO semesterProgressDTO);

    void deleteSP(Integer semesterProgressId);

    SemesterProgressDTO updateSemesterProgress(Integer semesterProgressId, SemesterProgressDTO semesterProgressDTO);

    SemesterProgress findActivedProgressTrue();


    List<Map<String, Object>> getAllSemesterProgress();
}
