package com.fpoly.backend.controller;

import com.fpoly.backend.dto.Response;
import com.fpoly.backend.dto.StudyInDTO;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.services.StudyInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/studyins")
@Validated
public class StudyInController {
    @Autowired
    StudyInService studyInService;

    // Lấy danh sách lớp học có sinh viên theo block, học kỳ và năm
    @GetMapping
    public ResponseEntity<Response> getAllByBlockAndSemesterAnhYear(){
        try {
            List<StudyInDTO> studyIns = studyInService.findAllByBlockAndSemesterAnhYear();
            return ResponseEntity.ok(new Response(LocalDateTime.now(), studyIns, "Lấy danh sách lớp học có sinh viên theo block, học kỳ và năm học thành công", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e){
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }
}
