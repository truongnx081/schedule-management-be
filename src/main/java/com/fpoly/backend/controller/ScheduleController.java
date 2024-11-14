package com.fpoly.backend.controller;

import com.fpoly.backend.dto.Response;
import com.fpoly.backend.dto.ScheduleDTO;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedules")
@Validated
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;


    // Xem lich hoc theo ngay bat dau va kết thúc
    @GetMapping("/getSchedules")
    public ResponseEntity<Response> getSchedulesByStudentIdAndStartdateAndEnddate(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        try {
            List<Map<String, Object>> scheduleByDateRange = scheduleService.getScheduleByDateRange(startDate, endDate);
            return ResponseEntity.ok(new Response(LocalDateTime.now(),scheduleByDateRange , "Đã lấy thành công các môn học theo id =  ", HttpStatus.OK.value()));
        }
        catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(),null,e.getMessage(),e.getStatus().value()));
        }
    }

    @PutMapping("/cancelSchedule")
    public ResponseEntity<Response> updateScheduleStatus(
            @RequestParam Integer scheduleId,
            @RequestBody ScheduleDTO request) {
        try {

            ScheduleDTO updatedSchedule = scheduleService.putScheduleStatus(request, scheduleId);
            return ResponseEntity.ok(new Response(
                    LocalDateTime.now(),
                    updatedSchedule,
                    "Update schedule status successfully",
                    HttpStatus.OK.value())
            );
        } catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus())
                    .body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }

    @GetMapping("/getscheduleStatusFalse")
    public ResponseEntity<Response> getClazzsByScheduleStatus() {
        try {
            List<Map<String, Object>> clazzs = scheduleService.getClazzsByScheduleStatus();
            return ResponseEntity.ok(new Response(
                    LocalDateTime.now(),
                    clazzs,
                    "Lấy danh sách lớp thành công.",
                    HttpStatus.OK.value()
            ));
        } catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus())
                    .body(new Response(
                            LocalDateTime.now(),
                            null,
                            e.getMessage(),
                            e.getStatus().value()
                    ));
        }
    }

}
