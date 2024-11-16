package com.fpoly.backend.services.impl;

//

import com.fpoly.backend.dto.StudyResultDTO;
import com.fpoly.backend.entities.*;
import com.fpoly.backend.mapper.StudyResultMapper;
import com.fpoly.backend.repository.*;
import com.fpoly.backend.services.IdentifyUserAccessService;
import com.fpoly.backend.services.StudyInService;
import com.fpoly.backend.services.StudyResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Autowired
    IdentifyUserAccessService identifyUserAccessService;
    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private YearRepository yearRepository;
    @Autowired
    private StudyResultMapper studyResultMapper;
    @Autowired
    private StudyInService studyInService;

    @Autowired
    ApplyForRepository applyForRepository;

    @Override
    public Map<String, Integer> getreportLearningProgressByStudentId() {
        Integer studentId = identifyUserAccessService.getStudent().getId();

        Integer totalSubjects = applyForRepository.countSubjectByStudent(studentId);
        Integer totalSubjectsPass = studyResultRepository.countSubjectPassByStudent(studentId);

        Integer totalUnfinishedSubjects = totalSubjects - totalSubjectsPass;
        return Map.of("passedSubjects", totalSubjectsPass, "unfinishedSubjects", totalUnfinishedSubjects);
    }

    @Override
    public List<Map<String, Object>> getAllStudyResultByStudentId() {
        Integer studentId = identifyUserAccessService.getStudent().getId();
            return studyResultRepository.getAllStudyResultByStudentId(studentId);
    }

    @Override
    public List<Map<String, Object>> findAllByStudyInId(Integer studyInId) {
        return studyResultRepository.findAllByStudyInIdOfStudent(studyInId);
    }

    @Override
    public Map<String, Object> countPassAndFalseByBlockAndSemesterAndYearOfStudent(Integer blockId, String semesterId, Integer yearId) {
        int studentPass = 0;
        int studentFalse = 0;
        Double markAverage = 0.0;
        Map<String, Object> map = new HashMap<>();

        for (Map<String, Object> studyIn : studyInService.getAllIdOfStudyInByBlockAndSemesterAndYearOfStudent(blockId, semesterId, yearId)) {
            Integer id = (Integer) studyIn.get("id");
            Double marked = 0.0;
            Double percentage = 0.0;
            boolean breakOccurred = true; // Biến cờ để đánh dấu

            // Kiểm tra tất cả điểm của mỗi sinh viên
            for (Map<String, Object> studyResult : findAllByStudyInId(id)) {
                Double mark = (Double) studyResult.get("marked");
                Double percent = (Double) studyResult.get("percentage");
                if(mark < 5 && percent >= 30){
                    studentFalse += 1;
                    breakOccurred = false; // Đánh dấu cờ khi break xảy ra
                    break;
                }
                else{
                    marked += mark * (percent / 100);
                    percentage += (percent / 100);
                }
            }

            // Tính điểm trung bình
            if(breakOccurred){
                markAverage = marked / percentage;
                if(markAverage >= 5.0)
                    studentPass += 1;
                else
                    studentFalse += 1;
            }
        }
        map.put("studentPass", studentPass);
        map.put("studentFalse", studentFalse);

        return map;
    }

}
