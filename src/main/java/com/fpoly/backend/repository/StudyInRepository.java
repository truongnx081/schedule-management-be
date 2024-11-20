package com.fpoly.backend.repository;

import com.fpoly.backend.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface StudyInRepository extends JpaRepository<StudyIn,Integer> {
    List<StudyIn> findAllByClazzBlockAndClazzSemesterAndClazzYear(Block block, Semester semester, Year year);

    @Query("SELECT s.id as id " +
            "FROM StudyIn s JOIN s.clazz c " +
            "WHERE c.block.block = :blockId " +
            "AND c.semester.semester = :semesterId " +
            "AND c.year.year = :yearId" )
    List<Map<String, Object>> getAllIdOfStudyInByBlockAndSemesterAndYearOfStudent(
            @Param("blockId") Integer blockId,
            @Param("semesterId") String semesterId,
            @Param("yearId") Integer yearId
    );

    List<StudyIn> findByClazz_Instructor_Id(Integer instructorId);

    @Query("SELECT clz.id as clazz_id ,clz.code as clazz_code, stu.code as student_code ,CONCAT(stu.lastName ,' ', stu.firstName) as fullname,\n" +
            "sub.name as subject_name,sum(srs.marked * srs.percentage)/100 as AVG " +
            "FROM StudyIn sti " +
            "JOIN sti.clazz clz " +
            "JOIN sti.student stu " +
            "JOIN sti.studyResults srs " +
            "JOIN clz.instructor ins " +
            "JOIN clz.subject sub " +
            "WHERE clz.id = :clazzId " +
            "group by clz.id, stu.code, CONCAT(stu.lastName ,' ', stu.firstName), clz.code, sub.name " +
            "order by CONCAT(stu.lastName ,' ', stu.firstName)")
    List<Map<String, Object>> getAllMarkAverageStudentsByClazzId(@Param("clazzId")Integer clazzId);


    boolean existsByStudentIdAndClazzId(Integer studentId, Integer clazzId);


    @Query("SELECT COUNT(si) FROM StudyIn si WHERE si.clazz.id = :clazzId")
    Integer countStudentByClazzId(@Param("clazzId") Integer clazzId);

}
