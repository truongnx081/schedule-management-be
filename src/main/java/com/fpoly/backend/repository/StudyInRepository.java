package com.fpoly.backend.repository;

import com.fpoly.backend.entities.Block;
import com.fpoly.backend.entities.Semester;
import com.fpoly.backend.entities.StudyIn;
import com.fpoly.backend.entities.Year;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyInRepository extends JpaRepository<StudyIn,Integer> {
    List<StudyIn> findAllByClazzBlockAndClazzSemesterAndClazzYear(Block block, Semester semester, Year year);
}
