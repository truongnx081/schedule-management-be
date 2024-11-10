//package com.fpoly.backend.services.impl;
//
//import com.fpoly.backend.repository.ApplyForRepository;
//import com.fpoly.backend.repository.StudentRepository;
//import com.fpoly.backend.services.IdentifyUserAccessService;
//import com.fpoly.backend.services.StudyHistoryService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.util.List;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class StudyHistoryServiceImpl implements StudyHistoryService {
//    @Autowired
//    StudyHistoryRepository studyHistoryRepository;
//
//    @Autowired
//    StudentRepository studentRepository;
//
//    @Autowired
//    IdentifyUserAccessService identifyUserAccessService;
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
//}
