package com.fpoly.backend.controller;

import com.fpoly.backend.dto.ClazzDTO;
import com.fpoly.backend.dto.Response;
import com.fpoly.backend.dto.ScheduleDTO;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.services.ScheduleService;
import com.fpoly.backend.until.ExcelUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    // Lấy danh sách tất cả lịch học
    @GetMapping
    public ResponseEntity<Response> getAll(){
        try {
            List<ScheduleDTO> scheduleDTOS = scheduleService.getAll();
            return ResponseEntity.ok(new Response(LocalDateTime.now(), scheduleDTOS, "Lấy danh sách tất cả lịch học thành công", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e){
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }

    // Lấy lịch học theo id
    @GetMapping("/{scheduleId}")
    public ResponseEntity<Response> getOne(@PathVariable Integer scheduleId){
        try {
            ScheduleDTO scheduleDTO = scheduleService.getOne(scheduleId);
            return ResponseEntity.ok(new Response(LocalDateTime.now(), scheduleDTO, "Lấy lịch học thành công", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e){
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }

    // Xóa lịch học theo id
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Response> delete(@PathVariable Integer scheduleId){
        try {
            scheduleService.delete(scheduleId);
            return ResponseEntity.ok(new Response(LocalDateTime.now(), null, "Xóa lịch học thành công", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e){
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }

    // Tạo mới 1 lịch học
    @PostMapping
    public ResponseEntity<Response> create(@RequestBody ScheduleDTO request){
        try {
            ScheduleDTO scheduleDTO = scheduleService.create(request);
            return ResponseEntity.ok(new Response(LocalDateTime.now(), scheduleDTO, "Thêm lịch học thành công", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e){
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }

    // Cập nhật 1 lịch học theo id
    @PutMapping("/{scheduleId}")
    public ResponseEntity<Response> update(@RequestBody ScheduleDTO request, @PathVariable Integer scheduleId){
        try {
            ScheduleDTO scheduleDTO = scheduleService.update(request, scheduleId);
            return ResponseEntity.ok(new Response(LocalDateTime.now(), scheduleDTO, "Cập nhật lịch học thành công", HttpStatus.OK.value()));
        } catch (AppUnCheckedException e){
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(), null, e.getMessage(), e.getStatus().value()));
        }
    }

    // Import Study Schedule by excel
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/excel/upload")
    ResponseEntity<Response> uploadFileExcel(@RequestParam("file") MultipartFile file){

        String message = "";
        if (ExcelUtility.hasExcelFormat(file)) {
            try {
                scheduleService.importStudySchedule(file);
                message = "The Excel file is uploaded: " + file.getOriginalFilename();
                return ResponseEntity.ok(new Response(LocalDateTime.now(), null, message, HttpStatus.OK.value()));
            } catch (Exception exp) {
                message = "The Excel file is not upload: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new Response(LocalDateTime.now(), null, exp.getMessage(), HttpStatus.EXPECTATION_FAILED.value()));
            }
        }
        message = "Please upload an excel file!";
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new Response(LocalDateTime.now(), null, message, HttpStatus.BAD_REQUEST.value()));
    }
}
