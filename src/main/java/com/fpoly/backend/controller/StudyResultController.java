package com.fpoly.backend.controller;


import com.fpoly.backend.dto.Response;
import com.fpoly.backend.dto.StudyResultDTO;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.services.StudyResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/studyResult")
@Validated
public class StudyResultController {
    @Autowired
    StudyResultService studyResultService;

    //Get learning progress by student
    @GetMapping("/learningProgressByStudent")
    public ResponseEntity<Response> getLearningProgressByStudentId() {
        try {
            Map<String, Integer> learningProgress = studyResultService.getreportLearningProgressByStudentId();
            return ResponseEntity.ok(new Response(
                    LocalDateTime.now(),
                    learningProgress,
                    "Successful",
                    HttpStatus.OK.value()));
        } catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }


    @GetMapping()
    public ResponseEntity<Response> getStudyResultByStudentId() {
        try {
            List<Map<String, Object>> studyHistories = studyResultService.getAllStudyResultByStudentId();
            return ResponseEntity.ok(new Response(LocalDateTime.now(), studyHistories, "Đã lấy thành công lịch sử các môn đã học theo studentId !  ", HttpStatus.OK.value()));
        }
        catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(),null,e.getMessage(),e.getStatus().value()));
        }
    }

    // Đếm số lượng sinh viên pass và false môn học theo block, học kỳ và năm học
    @GetMapping("/{block}/{semester}/{year}")
    public ResponseEntity<Response> getAllByStudyInId(@PathVariable Integer block,
                                                      @PathVariable String semester,
                                                      @PathVariable Integer year) {
        try {
            Map<String, Object> studyResultDTOS = studyResultService.countPassAndFalseByBlockAndSemesterAndYearOfStudent(block, semester, year);
            return ResponseEntity.ok(new Response(LocalDateTime.now(), studyResultDTOS, "Đếm số lượng môn học pass và false của sinh viên theo block, học kỳ và năm học thành công !  ", HttpStatus.OK.value()));
        }
        catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(),null,e.getMessage(),e.getStatus().value()));
        }
    }
}