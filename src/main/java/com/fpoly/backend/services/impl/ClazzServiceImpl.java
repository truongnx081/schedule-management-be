package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.StudyInDTO;
import com.fpoly.backend.entities.Clazz;
import com.fpoly.backend.entities.Student;
import com.fpoly.backend.entities.StudyIn;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.mapper.StudyInMapper;
import com.fpoly.backend.repository.ClazzRepository;
import com.fpoly.backend.repository.StudyInRepository;
import com.fpoly.backend.services.ClazzService;
import com.fpoly.backend.services.IdentifyUserAccessService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class ClazzServiceImpl implements ClazzService {

    ClazzRepository clazzRepository;
    IdentifyUserAccessService identifyUserAccessService;
    StudyInMapper studyInMapper;
    StudyInRepository studyInRepository;

    @Override
    public Clazz findById(Integer id) {
        return clazzRepository.findById(id).orElse(null);
    }

    @Override
    public List<Map<String, Object>> findClazzByBlockAndSemesterAndYear(Integer block, String semester, Integer year) {
        return clazzRepository.findClazzByBlockAndSemesterAndYear(block, semester, year);
    }

    @Override
    public List<Map<String, Object>> findClazzsBySubjectIdAndShiftAndBlockAndSemesterAndYear(Integer subjectId, Integer shift, Integer block, String semester, Integer year) {
        return clazzRepository.findClazzsBySubjectIdAndShiftAndBlockAndSemesterAndYear(subjectId, shift, block, semester, year);
    }

    @Override
    public  List<Map<String, Object>> getAllClazzByStudent() {
        Integer studentId=identifyUserAccessService.getStudent().getId();
        List<Map<String, Object>> clazzs = clazzRepository.getAllClazzByStudent(studentId);

        return clazzs;
    }

    @Override
    public StudyInDTO registerClazz(Integer clazzId) {
        Clazz clazz = clazzRepository.findById(clazzId)
                .orElseThrow(() -> new AppUnCheckedException("Clazz not found", HttpStatus.NOT_FOUND));

        Student student = identifyUserAccessService.getStudent();

        StudyIn studyIn = new StudyIn();
        studyIn.setClazz(clazz);
        studyIn.setStudent(student);

        StudyIn savedStudyIn = studyInRepository.save(studyIn);

        return studyInMapper.toDTO(savedStudyIn);
    }


}
