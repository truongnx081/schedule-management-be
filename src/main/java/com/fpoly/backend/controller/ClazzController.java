package com.fpoly.backend.controller;

import com.fpoly.backend.dto.ClazzDTO;
import com.fpoly.backend.dto.Response;
import com.fpoly.backend.dto.StudyInDTO;
import com.fpoly.backend.entities.SemesterProgress;
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

    // Lấy danh sách lớp học
    @GetMapping
    public ResponseEntity<Response> getAll(){
        try {
            List<ClazzDTO> clazzs = clazzService.getAll();
            return ResponseEntity.ok(new Response(LocalDateTime.now(), clazzs, "Lấy danh sách lớp học thành công", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e){
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }

    // Lấy lớp học theo id
    @GetMapping("/{clazzId}")
    public ResponseEntity<Response> getOne(@PathVariable Integer clazzId){
        try {
            ClazzDTO clazz = clazzService.getOne(clazzId);
            return ResponseEntity.ok(new Response(LocalDateTime.now(), clazz, "Lấy lớp học thành công", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e){
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }

    // Xóa lớp học theo id
    @DeleteMapping("/{clazzId}")
    public ResponseEntity<Response> delete(@PathVariable Integer clazzId){
        try {
            clazzService.delete(clazzId);
            return ResponseEntity.ok(new Response(LocalDateTime.now(), null, "Xóa lớp học thành công", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e){
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }

    // Tạo mới 1 lớp học
    @PostMapping
    public ResponseEntity<Response> create(@RequestBody ClazzDTO request){
        try {
            ClazzDTO clazz = clazzService.create(request);
            return ResponseEntity.ok(new Response(LocalDateTime.now(), clazz, "Thêm lớp học thành công", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e){
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }

    // Cập nhật 1 lớp học theo id
    @PutMapping("/{clazzId}")
    public ResponseEntity<Response> update(@RequestBody ClazzDTO request, @PathVariable Integer clazzId){
        try {
            ClazzDTO clazz = clazzService.update(request, clazzId);
            return ResponseEntity.ok(new Response(LocalDateTime.now(), clazz, "Cập nhật lớp học thành công", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e){
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }

    // Lấy danh sách lớp học theo block, semester và year
    @GetMapping("/clazzs-for-register")
    public ResponseEntity<Response> findClazzsByBlockAndSemesterAndYear(){
        try {
            List<Map<String, Object>> clazzs = clazzService.findClazzByBlockAndSemesterAndYear();
            return ResponseEntity.ok(new Response(LocalDateTime.now(), clazzs, "Lấy danh sách lớp học theo block, semester và year thành công", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e){
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }

    // Import clazz by excel
    @PostMapping("/import")
    ResponseEntity<Response> importExcel(@RequestBody List<ClazzDTO> requestDTO){
        try {
            clazzService.importClazz(requestDTO);
            return ResponseEntity.ok(new Response(LocalDateTime.now(), null, "Import excel lớp học thành công", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e){
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }
}

