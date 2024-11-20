package com.fpoly.backend.services;

import com.fpoly.backend.dto.StudyInDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface StudyInService {
    List<StudyInDTO> findAllByBlockAndSemesterAnhYear();

    List<Map<String, Object>> getAllIdOfStudyInByBlockAndSemesterAndYearOfStudent(
            Integer blockId, String semesterId, Integer yearId
    );

    List<Map<String, Object>> getAllMarkAverageStudentsByClazzId(Integer clazzId);

    StudyInDTO createStudyIn(StudyInDTO studyInDTO);
}
