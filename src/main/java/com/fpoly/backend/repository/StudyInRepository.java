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
}
