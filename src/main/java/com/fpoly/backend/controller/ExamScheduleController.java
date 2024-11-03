package com.fpoly.backend.controller;

import com.fpoly.backend.dto.Response;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.services.ExamScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/examschedules")
@Validated
public class ExamScheduleController {
    @Autowired
    private ExamScheduleService examScheduleService ;

    //Get exam schedule
    @GetMapping("/getExamSchedules")
    public ResponseEntity<Response> getExamSchedulesByStudentIdAndStartDateAndEndDate(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        try {
            List<Map<String, Object>> examScheduleByDateRange = examScheduleService.getExameScheduleByDateRange( startDate, endDate);
            return ResponseEntity.ok(new Response(LocalDateTime.now(),examScheduleByDateRange , "Đã lấy thành công lịch thi theo ngày đã chọn ! ", HttpStatus.OK.value()));
        }
        catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(),null,e.getMessage(),e.getStatus().value()));
        }
    }
}
