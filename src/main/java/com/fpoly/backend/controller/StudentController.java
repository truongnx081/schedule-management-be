package com.fpoly.backend.controller;

import com.fpoly.backend.dto.Response;
import com.fpoly.backend.dto.StudentDTO;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@Validated
public class StudentController {

    @Autowired
    private StudentService studentService;

    //API lay thong tin Student dang dang nhap
    @GetMapping("/studentInfor")
    public ResponseEntity<Response> getStudentInfor() {
        try {
            return ResponseEntity.ok(new Response(
                    LocalDateTime.now(),
                    studentService.getStudentInfor(),
                    "Get Student Infor Succesful",
                    HttpStatus.OK.value())
            );
        } catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }

    //API student tu cap nhat thong tin
    @PutMapping("/updateStudentByStudent")
    public ResponseEntity<Response> updateStudentByStudent(
            @RequestPart("request") StudentDTO request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            return ResponseEntity.ok(new Response(
                    LocalDateTime.now(),
                    studentService.updateStudentByStudent(request, file),
                    "Update student successfully",
                    HttpStatus.OK.value())
            );
        } catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus())
                    .body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }


    //API Admin xem thong tin student
    @GetMapping("/studentByID/{studentId}")
    public  ResponseEntity<Response> getStudentById(@PathVariable  Integer studentId){
        try {
            return ResponseEntity.ok(new Response(
                    LocalDateTime.now(),
                    studentService.getStudentById(studentId),
                    "Get student successfully",
                    HttpStatus.OK.value())
            );
        }catch (AppUnCheckedException e){
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }

    //Api Admin update Student
    @PutMapping("/updateStudentByAdmin/{studentId}")
    public ResponseEntity<Response> updateStudentByAdmin(
            @PathVariable("studentId") Integer studentId,
            @RequestPart("student") StudentDTO request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            // Gọi service để cập nhật thông tin sinh viên
            return ResponseEntity.ok(new Response(
                    LocalDateTime.now(),
                    studentService.updateStudentByAdmin(studentId, request, file),
                    "Update student successfully",
                    HttpStatus.OK.value())
            );
        } catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus()).body(
                    new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value())
            );
        }
    }

    //API create Student
    @PostMapping("/createStudent")
    public ResponseEntity<Response> createStudent(@RequestPart("student") StudentDTO request,
                                                  @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            StudentDTO createdStudent = studentService.createStudent(request, file);
            return ResponseEntity.ok(new Response(
                    LocalDateTime.now(),
                    createdStudent,
                    "Student created successfully",
                    HttpStatus.OK.value())
            );
        } catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }

    //API Delete Student
    @DeleteMapping("deleteStudent/{id}")
    public ResponseEntity<Response> deleteStudent(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(new Response(
                    LocalDateTime.now(),
                    studentService.deleteStudentById(id),
                    "Student deleted successfully",
                    HttpStatus.OK.value())
            );
        } catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }

    //Get all student by admin
    @GetMapping("/getAllStudentByCourseAndMajor")
    public  ResponseEntity<Response> getAllStudentByCourseAndMajor(
            @RequestParam(value = "course", required = false) String course,
            @RequestParam(value = "majorId", required = false) Integer majorId){

        try {
            return ResponseEntity.ok(new Response(
                    LocalDateTime.now(),
                    studentService.getAllStudentByCourseAndMajor(course,majorId),
                    "Get All Student successfully",
                    HttpStatus.OK.value())
            );
        } catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }

    @PostMapping("/import")
    public ResponseEntity<Response> importStudents(@RequestParam("file") MultipartFile file) {
        try {
            List<StudentDTO> students = studentService.importExcelFile(file);
            return ResponseEntity.ok(new Response(
                    LocalDateTime.now(),
                    students,
                    "Import Students successfully",
                    HttpStatus.OK.value())
            );
        } catch (AppUnCheckedException e) {
            return ResponseEntity.status(e.getStatus()).body(new Response(
                    LocalDateTime.now(),
                    null,
                    e.getMessage(),
                    e.getStatus().value())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(
                    LocalDateTime.now(),
                    null,
                    "Lỗi không xác định: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value())
            );
        }
    }

}


