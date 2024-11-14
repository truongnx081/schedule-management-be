package com.fpoly.backend.controller;

import com.fpoly.backend.dto.AttendanceDTO;
import com.fpoly.backend.dto.ClazzDTO;
import com.fpoly.backend.dto.Response;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.services.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/attendances")
@Validated
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    // Lấy danh sách điểm danh theo sinh viên
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Response> getAttendanceByStudentId(@PathVariable Integer studentId){
        try {
            List<AttendanceDTO> attendanceDTOS = attendanceService.getAttendanceByStudentId(studentId);
            return ResponseEntity.ok(new Response(LocalDateTime.now(), attendanceDTOS, "Lấy danh sách điểm danh theo sinh viên thành công", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e){
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }
}
