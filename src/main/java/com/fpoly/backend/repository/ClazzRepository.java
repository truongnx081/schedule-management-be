package com.fpoly.backend.repository;

import com.fpoly.backend.entities.Clazz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ClazzRepository extends JpaRepository<Clazz,Integer> {
    @Query("SELECT c.code as code," +
            "c.subject.name as subject_name," +
            "c.instructor.firstName as instructor_first_name," +
            "c.instructor.lastName as instrutor_last_name " +
            "FROM Clazz c " +
            "WHERE c.block.block = :block " +
            "AND c.semester.semester = :semester " +
            "AND c.year.year = :year")
    List<Map<String, Object>> findClazzByBlockAndSemesterAndYear (@Param("block") Integer block,
                                                                  @Param("semester") String semester,
                                                                  @Param("year") Integer year);


    @Query("SELECT c.code as code, " +
            "c.room.name as room, " +
            "c.shift.id as shift " +
            "FROM Clazz c " +
            "WHERE c.subject.id = :subjectId " +
            "AND c.shift.id = :shift " +
            "AND c.block.block = :block " +
            "AND c.semester.semester = :semester " +
            "AND c.year.year = :year")
    List<Map<String, Object>> findClazzsBySubjectIdAndShiftAndBlockAndSemesterAndYear (@Param("subjectId") Integer subjectId,
                                                                                       @Param("shift") Integer shift,
                                                                                       @Param("block") Integer block,
                                                                                       @Param("semester") String semester,@Param("year") Integer year);

    @Query("SELECT DISTINCT " +
            "su.code as codeSubject," +
            "su.name as nameSubject," +
            "su.credits as creditSubject," +
            "c.code AS code, "+
            "c.room.name AS roomName, " +
            "c.quantity AS quantity," +
            "c.shift.id AS shiftId, "+
            //"w.id as weekDayId " +
            "c.shift.startTime as shiftStartTime," +
            "c.shift.endTime as shiftEndTime,"+
            "c.instructor.code AS instructorCode, " +
            "c.id AS clazzId," +
            "c.onlineLink AS onlineLink, " +
            "c.block.block AS block, " +
            "c.instructor.id as instructorId, " +
            "c.room.id as roomId, " +
            "c.subject.id as subjectId ," +
            "c.semester.semester AS semester, " +
            "c.year.year AS year " +
            "FROM EducationProgram ed " +
            "JOIN ed.applyFors ap " +
            "JOIN ap.subject su " +
            "JOIN su.clazzes c " +
            "JOIN c.studyDays std " +
            "JOIN std.weekDay w " +
            "JOIN ed.students st " +
            "LEFT JOIN st.studyHistories study ON study.subject.id = su.id " +
            "LEFT JOIN study.marks m " +
            "WHERE (m.marked < 5 OR m.marked IS NULL) AND st.id = :studentId")
    List<Map<String, Object>> getAllClazzByStudent(@Param("studentId") Integer studentId);






}
