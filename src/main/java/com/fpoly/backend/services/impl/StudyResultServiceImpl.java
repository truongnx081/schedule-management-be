package com.fpoly.backend.services.impl;

//

import com.fpoly.backend.entities.Block;
import com.fpoly.backend.entities.Semester;
import com.fpoly.backend.entities.StudyResult;
import com.fpoly.backend.entities.Year;
import com.fpoly.backend.repository.BlockRepository;
import com.fpoly.backend.repository.SemesterRepository;
import com.fpoly.backend.repository.StudyResultRepository;
import com.fpoly.backend.repository.YearRepository;
import com.fpoly.backend.services.IdentifyUserAccessService;
import com.fpoly.backend.services.StudyResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class StudyResultServiceImpl implements StudyResultService {

    @Autowired
    StudyResultRepository studyResultRepository;
//
//    @Autowired
//    StudentRepository studentRepository;
//
    @Autowired
    IdentifyUserAccessService identifyUserAccessService;
    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private YearRepository yearRepository;

    //
//    @Autowired
//    ApplyForRepository applyForRepository;
//
//    @Override
//    public Map<String, Integer> getreportLearningProgressByStudentId() {
//        Integer studentId = identifyUserAccessService.getStudent().getId();
//
//        Integer totalSubjects = applyForRepository.countSubjectByStudent(studentId);
//        Integer totalSubjectsPass = applyForRepository.countSubjectByStudent(studentId);
//
//        Integer totalUnfinishedSubjects = totalSubjects - totalSubjectsPass;
//        return Map.of("passedSubjects", totalSubjectsPass, "unfinishedSubjects", totalUnfinishedSubjects);
//    }
//    public List<Map<String, Object>> getAllStudyHistoryByStudentId() {
//        Integer studentId = identifyUserAccessService.getStudent().getId();
//        return studyHistoryRepository.getAllStudyHistoryByStudentId(studentId);
//    }
//
    @Override
    public List<Map<String, Object>> getAllStudyResultByStudentId() {
        Integer studentId = identifyUserAccessService.getStudent().getId();
            return studyResultRepository.getAllStudyResultByStudentId(studentId);
    }

}
