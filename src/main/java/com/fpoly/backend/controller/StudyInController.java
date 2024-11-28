package com.fpoly.backend.controller;

import com.fpoly.backend.dto.Response;
import com.fpoly.backend.dto.StudyInDTO;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.services.StudyInService;
import org.apache.hc.core5.http.impl.io.AbstractMessageParser;
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

    // Lấy điểm trung bình của tất cả sinh viên theo clazz id
    @GetMapping("/getAllAverage")
    public ResponseEntity<Response> getAllMarkAverageStudentsByClazzId(@RequestParam Integer clazzId){
        try {
            List<Map<String, Object>> getAllMarkAverageStudentsByClazzId = studyInService.getAllMarkAverageStudentsByClazzId(clazzId);
            return ResponseEntity.ok(new Response(LocalDateTime.now(),getAllMarkAverageStudentsByClazzId , "Lấy danh sách điểm trung bình của tất cả sinh viên theo id của lớp học thành công !", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e){
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }
    // dang ky mon hoc
//    @PostMapping("/register-clazz")
//    public ResponseEntity<Response> create(StudyInDTO studyInDTO){
//        try {
//            StudyInDTO createStudyIn = studyInService.createStudyIn(studyInDTO);
//            return ResponseEntity.ok(new Response(LocalDateTime.now(),createStudyIn , "Đăng ký thành công lớp học !", HttpStatus.OK.value()));
//        } catch (AppUnCheckedException e){
//            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
//        }
//    }
//    public ResponseEntity<Response> create(@RequestParam Integer clazzId){
//        try {
//            StudyInDTO createStudyIn = studyInService.createStudyIn2(clazzId);
//            return ResponseEntity.ok(new Response(LocalDateTime.now(),createStudyIn , "Đăng ký thành công lớp học !", HttpStatus.OK.value()));
//        } catch (AppUnCheckedException e){
//            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
//        }
//    }
    // đổi lớp học
//    @PutMapping("/change-clazz")
//    public ResponseEntity<Response> update(@RequestParam Integer studyInId, @RequestParam Integer newClazzId){
////        try {
////            StudyInDTO updateStudyIn = studyInService.updateStudyIn(studyInId, studyInDTO);
////            return ResponseEntity.ok(new Response(LocalDateTime.now(),updateStudyIn , "Đổi thành công lớp học !", HttpStatus.OK.value()));
////        } catch (AppUnCheckedException e){
////            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
////        }
//        try {
//            // Gọi service để cập nhật thông tin
//            StudyInDTO updatedStudyIn = studyInService.updateStudyIn2(studyInId, newClazzId);
//            return ResponseEntity.ok(new Response(LocalDateTime.now(),updatedStudyIn , "Đổi thành công lớp học !", HttpStatus.OK.value()));
//        } catch (AppUnCheckedException e) {
//            // Xử lý lỗi nếu có
//            return new ResponseEntity<>(null, e.getStatus());
//        }
//    }

    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    @PostMapping("/register-clazz")
    public ResponseEntity<Response> create(@RequestBody StudyInDTO studyInDTO){
        try {
            StudyInDTO createStudyIn = studyInService.createStudyIn(studyInDTO);
            return ResponseEntity.ok(new Response(LocalDateTime.now(),createStudyIn , "Đăng ký thành công lớp học !", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e){
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }
}
