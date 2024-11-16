package com.fpoly.backend.repository;

import com.fpoly.backend.entities.Attendance;
import com.fpoly.backend.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findAllByStudent(Student student);

    public Optional<Attendance> findByStudentIdAndScheduleId(Integer studentId, Integer scheduleId);

    @Query("SELECT a.student.id as studentCode, " +
            "CONCAT(a.student.lastName, ' ', a.student.firstName) as fullName, " +
            "a.student.avatar as imgStudent," +
            "a.present as isPresent " +
            "FROM Attendance a " +
            "JOIN a.schedule s " +
            "WHERE s.date = :date AND s.clazz.id = :clazzId")
    List<Map<String, Object>> findStudentIdsByDateAndClazzId(
            @Param("clazzId") Integer clazzId,
            @Param("date") LocalDate date);



}
