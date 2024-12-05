package com.fpoly.backend.repository;

import com.fpoly.backend.entities.RetakeSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface RetakeScheduleRepository extends JpaRepository<RetakeSchedule, Integer> {
    @Query("SELECT " +
            "c.id as clazzId, " +
            "re.id as retakescheduleId, " +
            "s.id as scheduleId, " +
            "re.date as date, " +
            "c.code as code, " +
            "c.subject.name as subjectName, " +
            "c.subject.code as subjectCode, " +
            "re.room.name as roomName, " +
            "re.shift.id as shiftId " +
            "FROM Instructor i " +
            "JOIN i.clazzes c " +
            "JOIN c.schedules s " +
            "JOIN s.retakeSchedules re " +
            "JOIN c.shift sh " +
            "WHERE i.id = :instructorId " +
            "AND re.date BETWEEN :startDate AND :endDate " )
    List<Map<String, Object>> getRetakeScheduleByInstructor(
            @Param("instructorId") Integer instructorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
