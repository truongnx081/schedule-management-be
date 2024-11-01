package com.fpoly.backend.services;

import com.fpoly.backend.dto.StudyInDTO;
import com.fpoly.backend.entities.Clazz;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ClazzService {
    Clazz findById(Integer id);

    List<Map<String, Object>> findClazzByBlockAndSemesterAndYear(Integer block, String semester, Integer year);

    List<Map<String, Object>> findClazzsBySubjectIdAndShiftAndBlockAndSemesterAndYear (Integer subjectId, Integer shift, Integer block, String semester, Integer year);

    List<Map<String, Object>> getAllClazzByStudent();

    public StudyInDTO registerClazz(Integer clazzId);

}
