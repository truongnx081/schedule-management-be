package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.AttendanceDTO;
import com.fpoly.backend.entities.Attendance;
import com.fpoly.backend.entities.Student;
import com.fpoly.backend.mapper.AttendanceMapper;
import com.fpoly.backend.repository.AttendanceRepository;
import com.fpoly.backend.repository.StudentRepository;
import com.fpoly.backend.services.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceMapper attendanceMapper;

    @Override
    public List<AttendanceDTO> getAttendanceByStudentId(int studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(()->
                new RuntimeException("Student not found"));

        return attendanceRepository.findAllByStudent(student).stream()
                .map(attendanceMapper::toDTO).toList();
    }
}
