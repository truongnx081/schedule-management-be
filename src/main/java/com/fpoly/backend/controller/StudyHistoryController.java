//package com.fpoly.backend.controller;
//
//import com.fpoly.backend.dto.Response;
//import com.fpoly.backend.exception.AppUnCheckedException;
//import com.fpoly.backend.services.StudyHistoryService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/studyHistories")
//@Validated
//public class StudyHistoryController {
//    @Autowired
//    StudyHistoryService studyHistoryService;
//
//    //Get learning progress by student
//    @GetMapping("/learningProgressByStudent")
//    public ResponseEntity<Response> getLearningProgressByStudentId() {
//        try {
//            Map<String, Integer> learningProgress = studyHistoryService.getreportLearningProgressByStudentId();
//            return ResponseEntity.ok(new Response(
//                    LocalDateTime.now(),
//                    learningProgress,
//                    "Successful",
//                    HttpStatus.OK.value()));
//        } catch (AppUnCheckedException e) {
//            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
//        }
//    }
//
//
//    @GetMapping()
//    public ResponseEntity<Response> getStudyHistoryByStudentId() {
//        try {
//            List<Map<String, Object>> studyHistories = studyHistoryService.getAllStudyHistoryByStudentId();
//            return ResponseEntity.ok(new Response(LocalDateTime.now(), studyHistories, "Đã lấy thành công lịch sử các môn đã học theo studentId !  ", HttpStatus.OK.value()));
//        }
//        catch (AppUnCheckedException e) {
//            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(),null,e.getMessage(),e.getStatus().value()));
//        }
//    }
//}