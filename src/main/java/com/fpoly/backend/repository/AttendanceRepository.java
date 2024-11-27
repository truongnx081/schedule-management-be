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

    boolean existsByScheduleIdAndStudentId(Integer scheduleId, Integer studentId);


    public Optional<Attendance> findByStudentIdAndScheduleId(Integer studentId, Integer scheduleId);

    @Query("SELECT a.student.id as studentId, " +
            "a.student.code as studentCode, " +
            "CONCAT(a.student.lastName, ' ', a.student.firstName) as fullName, " +
            "a.student.avatar as avatar," +
            "a.present as isPresent " +
            "FROM Attendance a " +
            "JOIN a.schedule s " +
            "WHERE s.clazz.id = :clazzId " +
            "AND s.id =:scheduleId")
    List<Map<String, Object>> findStudentIdsByScheduleIdAndClazzId(
            @Param("clazzId") Integer clazzId,
            @Param("scheduleId") Integer scheduleId);



}
