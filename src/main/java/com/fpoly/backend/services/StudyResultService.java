package com.fpoly.backend.services;

import com.fpoly.backend.dto.StudyResultDTO;
import com.fpoly.backend.entities.StudyResult;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public interface StudyResultService {

    List<Map<String, Object>> getAllStudyResultByStudentId();
    List<Map<String, Object>> findAllByStudyInId(Integer studyInId);
    Map<String, Object> countPassAndFalseByBlockAndSemesterAndYearOfStudent(
            Integer blockId, String semesterId, Integer yearId
    );
}
