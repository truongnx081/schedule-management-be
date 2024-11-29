package com.fpoly.backend.controller;

import com.fpoly.backend.dto.Response;
import com.fpoly.backend.dto.SemesterProgressDTO;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.services.SemesterProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/semesterprogresses")
@Validated
public class SemesterProgressController {

    @Autowired
    SemesterProgressService semesterProgressService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/getAllSP")
    public ResponseEntity<Response> getAllSemesterProgress() {
        try {
            List<Map<String, Object>> getAllSP = semesterProgressService.getAllSemesterProgress();
            return ResponseEntity.ok(new Response(LocalDateTime.now(), getAllSP, "Lấy danh sách thành công Semester Progress thành công !", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }


    // create semesterProgresses
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/createSP")
    public ResponseEntity<Response> createSemesterProgress(@RequestBody SemesterProgressDTO semesterProgressDTO) {
        try {
            SemesterProgressDTO createSP = semesterProgressService.createSemesterProgress(semesterProgressDTO);
            return ResponseEntity.ok(new Response(LocalDateTime.now(), createSP, "Semester Progress được tạo thành công !", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }

    // update semesterProgresses
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/updateSP")
    public ResponseEntity<Response> updateSemesterProgress(@RequestParam Integer semesterProgressId, @RequestBody SemesterProgressDTO semesterProgressDTO) {
        try {
            SemesterProgressDTO updateSP = semesterProgressService.updateSemesterProgress(semesterProgressId, semesterProgressDTO);
            return ResponseEntity.ok(new Response(LocalDateTime.now(), updateSP, "Semester Progress đã được cập nhật thành công!", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }
    // delete semesterProgresses
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/deleteSP")
    public ResponseEntity<Response> deleteSemesterProgress(@RequestParam Integer semesterProgressId) {
        try {
            semesterProgressService.deleteSP(semesterProgressId);
            return ResponseEntity.ok(new Response(LocalDateTime.now(), null, "Semester Progress đã được xóa thành công!", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN, ROLE_INSTRUCTOR, ROLE_STUDENT')")
    @GetMapping("/current-progress")
    public ResponseEntity<Response> getCurrentProgress (){
        try{
            String currentProgress = semesterProgressService.findCurrentProgress();
            return ResponseEntity.ok(new Response(LocalDateTime.now(), currentProgress, "Semester Progress đã được xóa thành công!", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }
}
