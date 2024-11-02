package com.fpoly.backend.repository;

import com.fpoly.backend.entities.ExamSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface ExamScheduleRepository extends JpaRepository<ExamSchedule,Integer> {

    @Query("SELECT ex.date AS examDate, ex.batch AS examBatch, ex.room.name AS roomName, " +
            "subj.code AS subjectCode, subj.name AS subjectName, " +
            "c.code AS clazzCode, i.code AS instructorCode, " +
            "ex.shift.id AS shiftId, ex.shift.startTime AS startTime, ex.shift.endTime AS endTime " +
            "FROM ExamSchedule ex " +
            "JOIN ex.clazz c " +
            "JOIN StudyIn sti ON sti.clazz.id = c.id " +
            "JOIN sti.student st " +
            "JOIN ArrangeBatch ab ON ab.student.id = st.id " +
            "JOIN c.subject subj " +
            "JOIN ex.room r " +
            "JOIN c.instructor i " +
            "JOIN ex.shift sh " +
            "WHERE st.id = :studentId " +
            "AND ab.batch = ex.batch " +
            "AND ex.date BETWEEN :startDate AND :endDate " +
            "GROUP BY ex.date, ex.batch, r.name, subj.code, subj.name, " +
            "c.code, i.code, sh.id, sh.startTime, sh.endTime")
    List<Map<String, Object>> getExamScheduleByDateRange(@Param("studentId") Integer studentId,
                                                         @Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);
}
