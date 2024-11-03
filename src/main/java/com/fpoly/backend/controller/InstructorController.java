package com.fpoly.backend.controller;

import com.fpoly.backend.dto.Response;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.services.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/instructors")
@Validated
public class InstructorController {
    @Autowired
    InstructorService instructorService;

    @GetMapping("/getAllTeachingSchedule")
    public ResponseEntity<Response> getTeachingSchedule(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        try {
            return ResponseEntity.ok(new Response(
                    LocalDateTime.now(),
                    instructorService.getAllTeachingScheduleByInstructor(startDate, endDate),
                    "Get All Teaching Schedule Successful",
                    HttpStatus.OK.value())
            );
        } catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }
}
