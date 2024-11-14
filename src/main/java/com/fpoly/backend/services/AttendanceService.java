package com.fpoly.backend.services;

import com.fpoly.backend.dto.AttendanceDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AttendanceService {
    List<AttendanceDTO> getAttendanceByStudentId(int studentId);
}
