package com.fpoly.backend.repository;

import com.fpoly.backend.dto.ShiftDTO;
import com.fpoly.backend.entities.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift,Integer> {



    @Query("SELECT sch.clazz.shift.id " +
            "FROM Schedule sch " +
            "WHERE sch.clazz.id = :classId " +
            "AND sch.clazz.instructor.id = :instructorId")
    List<Integer> findInstructorShifts(@Param("classId") Integer clazzId,
                                       @Param("instructorId") Integer instructorId);

    @Query("SELECT DISTINCT c.shift.id " +
            "FROM Schedule s " +
            "JOIN s.clazz c " +
            "JOIN c.studyIns si " +
            "WHERE c.id = :clazzId")
    List<Integer> findStudentScheduleShifts(@Param("clazzId") Integer clazzId);

    @Query("SELECT DISTINCT ex.shift.id " +
            "FROM ExamSchedule ex " +
            "JOIN ex.clazz c " +
            "JOIN c.studyIns  " +
            "WHERE c.id = :clazzId")
    List<Integer> findStudentExamShifts(@Param("clazzId") Integer clazzId);

    @Query("SELECT DISTINCT rs.shift.id " +
            "FROM RetakeSchedule rs " +
            "JOIN rs.schedule s " +
            "WHERE rs.schedule.clazz.id = :clazzId")
    List<Integer> findStudentRetakeShifts(@Param("clazzId") Integer clazzId);
}
