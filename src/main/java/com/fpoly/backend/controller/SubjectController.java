package com.fpoly.backend.controller;

import com.fpoly.backend.dto.Response;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subjects")
@Validated
public class SubjectController {
    @Autowired
    SubjectService subjectService;

    // lấy tất cả môn học
    @GetMapping("/getAllSubject")
    public ResponseEntity<Response> getAllSubject() {
        try {
            List<Map<String, Object>> subjects = subjectService.findAllSubject();
            return ResponseEntity.ok(new Response(LocalDateTime.now(), subjects, "Đã lấy thành công tất cả môn học", HttpStatus.OK.value()));
        }
        catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(),null,e.getMessage(),e.getStatus().value()));
        }
    }

    // Tìm kiếm môn học theo id bộ môn
    @GetMapping("/getAllSubjectBySpecializationId")
    public ResponseEntity<Response> getAllSubjectBySpecializationId(@RequestParam Integer specializationId) {
        try {
            List<Map<String, Object>> subjects = subjectService.getAllSubjectBySpecializationId(specializationId);
            return ResponseEntity.ok(new Response(LocalDateTime.now(), subjects, "Đã lấy thành công các môn học theo id =  " + specializationId, HttpStatus.OK.value()));
        }
        catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(),null,e.getMessage(),e.getStatus().value()));
        }
    }
}
