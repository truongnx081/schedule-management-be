package com.fpoly.backend.repository;

import com.fpoly.backend.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Integer> {
    @Query("SELECT s.date AS studyDate, " +
            "r.name AS roomName, " +
            "subj.code AS subjectCode, " +
            "subj.name AS subjectName, " +
            "c.code AS clazzCode, " +
            "i.code AS instructorCode, " +
            "sh.id AS shiftId, " +
            "sh.startTime AS start_time, " +
            "sh.endTime AS end_time " +
            "FROM Schedule s " +
            "JOIN s.clazz c " +
            "JOIN c.room r " +
            "JOIN c.subject subj " +
            "JOIN c.instructor i " +
            "JOIN c.shift sh " +
            "JOIN c.studyDays stday " +
            "JOIN stday.weekDay wd " +
            "JOIN c.studyIns si " +
            "JOIN si.student stu " +
            "WHERE stu.id = :studentId " +
            "AND s.date BETWEEN :startDate AND :endDate " +
            "GROUP BY s.date, r.name, subj.code, subj.name, c.code, i.code, sh.id, sh.startTime, sh.endTime " +
            "ORDER BY s.date ASC")
    List<Map<String, Object>> getScheduleByDateRange(Integer studentId, LocalDate startDate, LocalDate endDate);
}
