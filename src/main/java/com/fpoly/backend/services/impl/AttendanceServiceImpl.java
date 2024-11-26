package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.AttendanceDTO;
import com.fpoly.backend.entities.Attendance;
import com.fpoly.backend.entities.Student;
import com.fpoly.backend.mapper.AttendanceMapper;
import com.fpoly.backend.repository.AttendanceRepository;
import com.fpoly.backend.repository.RetakeScheduleRepository;
import com.fpoly.backend.repository.ScheduleRepository;
import com.fpoly.backend.repository.StudentRepository;
import com.fpoly.backend.services.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceMapper attendanceMapper;
    private final ScheduleRepository scheduleRepository;
    private final RetakeScheduleRepository retakeScheduleRepository;

    @Override
    public List<AttendanceDTO> getAttendanceByStudentId(int studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(()->
                new RuntimeException("Student not found"));

        return attendanceRepository.findAllByStudent(student).stream()
                .map(attendanceMapper::toDTO).toList();
    }

    @Override
    public List<AttendanceDTO> markAttendance(List<AttendanceDTO> request) {
        List<Attendance> savedAttendances = new ArrayList<>();

        for(AttendanceDTO attendanceDTO:request ){
            Attendance attendance = attendanceMapper.toEntity(attendanceDTO);

            attendance.setSchedule(scheduleRepository.findById(attendanceDTO.getScheduleId())
                    .orElseThrow(() -> new RuntimeException("Schedule not found")));
            attendance.setStudent(studentRepository.findById(attendanceDTO.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found")));
            if (attendanceDTO.getRetakeScheduleId() != null) {
                attendance.setRetakeSchedule(retakeScheduleRepository.findById(attendanceDTO.getRetakeScheduleId())
                        .orElseThrow(() -> new RuntimeException("Retake Schedule not found")));
            }else {
                attendance.setRetakeSchedule(null);
            }

            savedAttendances.add(attendanceRepository.save(attendance));
        }
        return savedAttendances.stream()
                .map(attendanceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String,Object>> getAttendanceByClazzId(Integer clazzId, Integer scheduleId){
        return attendanceRepository.findStudentIdsByScheduleIdAndClazzId(clazzId,scheduleId);
    }

    @Override
    public List<AttendanceDTO> updateAttendance(List<AttendanceDTO> attendanceDTOs) {
        List<Attendance> updatedAttendances = new ArrayList<>();

        for (AttendanceDTO attendanceDTO : attendanceDTOs) {
            Attendance attendance = attendanceMapper.toEntity(attendanceDTO);

            Attendance existingAttendance = attendanceRepository.findByStudentIdAndScheduleId(attendanceDTO.getStudentId(), attendanceDTO.getScheduleId())
                    .orElseThrow(() -> new RuntimeException("Attendance not found"));

            existingAttendance.setPresent(attendanceDTO.getPresent());

            updatedAttendances.add(attendanceRepository.save(existingAttendance));
        }

        return updatedAttendances.stream()
                .map(attendanceMapper::toDTO)
                .collect(Collectors.toList());
    }
}
