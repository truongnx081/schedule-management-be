package com.fpoly.backend.controller;

import com.fpoly.backend.dto.Response;
import com.fpoly.backend.dto.StudyInDTO;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.services.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clazzs")
@Validated
public class ClazzController {
    @Autowired
    ClazzService clazzService;


    @GetMapping("/clazzsByBlockAndSemesterAndYear")
    public ResponseEntity<Response> findClazzsByBlockAndSemesterAndYear(
            @RequestParam("block") Integer block,
            @RequestParam("semester") String semester,
            @RequestParam("year") Integer year){
        try {
            List<Map<String, Object>> clazzs = clazzService.findClazzByBlockAndSemesterAndYear(block, semester, year);
            return ResponseEntity.ok(new Response(LocalDateTime.now(), clazzs, "Lấy lớp học theo thành công", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e){
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }

    @GetMapping("{subjectId}/{shift}/{block}/{semester}/{year}")
    public ResponseEntity<Response> findClazzsBySubjectIdAndShiftAndBlockAndSemesterAndYear(
            @PathVariable("subjectId") Integer subjectId,
            @PathVariable("shift") Integer shift,
            @PathVariable("block") Integer block,
            @PathVariable("semester") String semester,
            @PathVariable("year") Integer year){
        try{
            List<Map<String, Object>> clazzs = clazzService.findClazzsBySubjectIdAndShiftAndBlockAndSemesterAndYear(subjectId, shift, block, semester, year);
            return ResponseEntity.ok(new Response(LocalDateTime.now(), clazzs, "Lấy lớp học theo thành công", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e){
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }

    @GetMapping("/getAllClazzByStudent")
    public ResponseEntity<Response> getAllClazzByStudent() {
        try {
            List<Map<String, Object>> clazzs= clazzService.getAllClazzByStudent();
            return ResponseEntity.ok(new Response(
                    LocalDateTime.now(),
                    clazzs,
                    "Get All Clazz successfully",
                    HttpStatus.OK.value())
            );
        } catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus()).body(new Response(
                    LocalDateTime.now(),
                    null,
                    e.getMessage(),
                    e.getStatus().value())
            );
        }
    }

    @PostMapping("/registerClazz/{clazzId}")
    public ResponseEntity<Response> registerClazz(@PathVariable Integer clazzId) {
        try {
            StudyInDTO studyInDTO = clazzService.registerClazz(clazzId);
            return ResponseEntity.ok(new Response(
                    LocalDateTime.now(),
                    studyInDTO,
                    "Registered class successfully",
                    HttpStatus.OK.value())
            );
        } catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus()).body(new Response(
                    LocalDateTime.now(),
                    null,
                    e.getMessage(),
                    e.getStatus().value())
            );
        }
    }

}

