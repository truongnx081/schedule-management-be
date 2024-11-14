package com.fpoly.backend.repository;

import com.fpoly.backend.entities.Attendance;
import com.fpoly.backend.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findAllByStudent(Student student);
}
