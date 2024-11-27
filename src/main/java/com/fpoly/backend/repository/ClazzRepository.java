package com.fpoly.backend.repository;

import com.fpoly.backend.entities.Clazz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    boolean existsByCode(String code);

    boolean existsByIdAndInstructorId(Integer clazzId, Integer instructorId);

    @Query("SELECT c.id as id," +
            "c.code as code, " +
            "c.subject.code as subjectCode,     " +
            "c.subject.name as subject_name," +
            "c.instructor.firstName as instructor_first_name," +
            "c.instructor.lastName as instrutor_last_name, " +
            "c.subject.credits as credits," +
            "c.quantity as quantity," +
            "c.shift.id as shift," +
            "c.shift.startTime as start_time," +
            "c.shift.endTime as end_time " +
            "FROM Clazz c " +
            "WHERE c.block.block = :block " +
            "AND c.semester.semester = :semester " +
            "AND c.year.year = :year " +
            "AND c.subject.id = :subjectId")
    List<Map<String, Object>> findClazzesToRegistByBlockAndYearAndSemesterAndYearAndSubjectId
            (@Param("block") Integer block,
             @Param("semester") String semester,
             @Param("year") Integer year,
             @Param("subjectId") Integer subjectId);

    @Query("SELECT c.id as id," +
            "c.subject.code as subject_code," +
            "c.subject.name as subject_name," +
            "sh.id as shift," +
            "i.code as instructor_code " +
            "FROM Clazz c " +
            "JOIN c.subject s " +
            "JOIN c.shift sh " +
            "JOIN c.instructor i " +
            "JOIN c.studyIns si " +
            "WHERE si.student.id = :studentId " +
            "AND c.block.block = :block " +
            "AND c.semester.semester = :semester " +
            "AND c.year.year = :year")
    List<Map<String,Object>> findCurrentClassesByBlockAndSemesterAndYearAndStudentId(
            @Param("block") Integer block,
            @Param("semester") String semester,
            @Param("year") Integer year,
            @Param("studentId") Integer studentId);

    @Query("SELECT c.id as clazz_id," +
            "c.code as clazz_code," +
            "c.subject.id as subject_id," +
            "c.subject.code as subject_code," +
            "c.shift.id as shift " +
            "FROM Clazz c JOIN c.subject WHERE c.id = :clazzId")
    Map<String,Object> findClazzToChangeByClazzId(@Param("clazzId") Integer clazzId);



    @Query("SELECT c.id as clazz_id," +
            "c.code as clazz_code," +
            "c.room.name as room_name," +
            "c.shift.id as shift " +
            "FROM Clazz c " +
            "WHERE c.subject.id = :subjectId " +
            "AND c.shift.id = :shift " +
            "AND c.block.block = :block " +
            "AND c.semester.semester = :semester " +
            "AND c.year.year = :year")
    List<Map<String,Object>> findClazzesBySubjectIdAndShiftAndBlockAndSemesterAndYear(@Param("subjectId") Integer subjectId,
                                                                                      @Param("shift") Integer shift,
                                                                                      @Param("block") Integer block,
                                                                                      @Param("semester") String semester,
                                                                                      @Param("year") Integer year);

    @Query("SELECT c.id as clazz_id," +
            "c.code as clazz_code," +
            "c.subject.code as subject_code," +
            "c.subject.name as subject_name," +
            "c.room.name as room_name," +
            "c.shift.id as shift " +
            "FROM Clazz c " +
            "JOIN c.subject s " +
            "JOIN c.room r " +
            "JOIN c.shift sh " +
            "WHERE c.instructor.id = :instructorId " +
            "AND c.block.block = :block " +
            "AND c.semester.semester = :semester " +
            "AND c.year.year = :year")
    List<Map<String,Object>> findClazzesByInstructorIdAndBlockAndSemesterAndYear (@Param("instructorId") Integer instructorId,
                                                                                  @Param("block") Integer block,
                                                                                  @Param("semester") String semester,
                                                                                  @Param("year") Integer year);

    @Query("SELECT c.id AS id, c.code AS code, c.onlineLink AS onlineLink, c.quantity AS quantity, " +
            "c.block.block AS block, c.semester.semester AS semester, c.year.year AS year, c.subject.id AS subjectId, " +
            "c.subject.code AS subjectCode, c.subject.name AS subjectName, c.instructor.code AS instructorCode, c.instructor.id AS instructorId, " +
            "c.shift.id AS shiftId, c.room.name AS roomName, c.room.id AS roomId, c.room.building.name AS building, " +
            "w.day AS weekday " +
            "FROM StudyDay sd " +
            "JOIN sd.clazz c " +
            "JOIN sd.weekDay w " +
            "WHERE c.block.block = :block AND c.semester.semester = :semester AND c.year.year = :year")
    List<Map<String,Object>> findAllClazzsByBlockAndSemesterAndYear(@Param("block") Integer block,
                                                                    @Param("semester") String semester,
                                                                    @Param("year") Integer year);
}
