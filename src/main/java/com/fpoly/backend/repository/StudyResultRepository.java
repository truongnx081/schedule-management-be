package com.fpoly.backend.repository;

import com.fpoly.backend.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;


public interface StudyResultRepository extends JpaRepository<StudyResult,Integer> {
    @Query("SELECT clz.id as clazz_id, clz.code as clazz_code, sub.code as subject_code, sub.name as subject_name, sub.credits as subject_credits, " +
            "clz.block.block as block, clz.semester.semester as semester, clz.year.year as year, sub.id as subject_id,  " +
            "SUM(str.marked * str.percentage)/100 as marked_avg   " +
            "FROM StudyResult str " +
            "JOIN str.studyIn sti " +
            "JOIN sti.clazz clz " +
            "JOIN sti.student stu " +
            "JOIN clz.subject sub " +
            "WHERE stu.id = :studentId " +
            "GROUP BY clz.id, clz.code, sub.code, sub.name, sub.credits, clz.block.block , clz.semester.semester , clz.year.year, sub.id  " +
            "order by  clz.id" )
    List<Map<String, Object>> getAllStudyResultByStudentId(@Param("studentId") Integer studentId);

    @Query("SELECT sr.id as id, sr.marked as marked, sr.percentage as percentage, sr.studyIn.id as studyIn " +
            "FROM StudyResult sr JOIN sr.studyIn s " +
            "WHERE sr.studyIn.id = :studyInId")
    List<Map<String, Object>> findAllByStudyInIdOfStudent(
            @Param("studyInId") Integer studyInId
    );


    @Query("SELECT CONCAT(std.lastName, ' ', std.firstName, ' - ', std.code) AS studentName, " +
            "mcs.name AS markColumnName, " +
            "srs.marked AS mark " +
            "FROM StudyResult srs " +
            "JOIN srs.studyIn sis " +
            "JOIN srs.markColumn mcs " +
            "JOIN sis.clazz czs " +
            "JOIN sis.student std " +
            "JOIN czs.subject sus " +
            "JOIN czs.instructor ins " +
            "WHERE czs.id = :clazzId AND std.id = :studentId")
    List<Map<String, Object>> getAllMarkColumn(@Param("clazzId") Integer clazzId,
                                               @Param("studentId") Integer studentId);

    @Query("SELECT COUNT(DISTINCT s.subject.id ) " +
            "FROM StudyResult str " +
            "JOIN str.studyIn st " +
            "JOIN str.markColumn m " +
            "JOIN m.subjectMarks s " +
            "WHERE st.student.id = :studentId " +
            "GROUP BY s.subject.id " +
            "HAVING SUM(str.marked * str.percentage) / 100 >= 5")
    Integer countSubjectPassByStudent(@Param("studentId") Integer studentId);


    boolean existsByMarkColumnIdAndStudyInId(Integer markColumnId, Integer studyInId);

    @Query("SELECT  " +
                  "mcs.name AS markColumnName, " +
                  "srs.marked AS mark, srs.percentage AS percentage " +
                  "FROM StudyResult srs " +
                  "JOIN srs.studyIn sis " +
                  "JOIN srs.markColumn mcs " +
                  "JOIN sis.clazz czs " +
                  "JOIN sis.student std " +
                  "JOIN czs.subject sus " +
                  "JOIN czs.instructor ins " +
                  "WHERE czs.id = :clazzId AND sus.id = :subjectId AND std.id = :studentId")
    List<Map<String, Object>> getAllMarkDetail(@Param("clazzId")Integer clazzId,
                                               @Param("subjectId")Integer subjectId,
                                               @Param("studentId")Integer studentId);
}
