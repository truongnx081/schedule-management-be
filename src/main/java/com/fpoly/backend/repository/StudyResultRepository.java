package com.fpoly.backend.repository;

import com.fpoly.backend.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;


public interface StudyResultRepository extends JpaRepository<StudyResult,Integer> {
    @Query("SELECT clz.code as clazz_code, sub.code as subject_code, sub.name as subject_name, sub.credits as subject_credits, " +
            "clz.block.block as block, clz.semester.semester as semester, clz.year.year as year, " +
            "SUM(str.marked * str.percentage)/100 as marked_avg   " +
            "FROM StudyResult str " +
            "JOIN str.studyIn sti " +
            "JOIN sti.clazz clz " +
            "JOIN sti.student stu " +
            "JOIN clz.subject sub " +
            "WHERE stu.id = :studentId " +
            "GROUP BY clz.code, sub.code, sub.name, sub.credits, clz.block.block , clz.semester.semester , clz.year.year  " +
            "order by  clz.year.year,clz.semester.semester, clz.block.block, sub.name DESC" )
    List<Map<String, Object>> getAllStudyResultByStudentId(Integer studentId);

}
