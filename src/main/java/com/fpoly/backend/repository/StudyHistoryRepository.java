package com.fpoly.backend.repository;

import com.fpoly.backend.entities.StudyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyHistoryRepository extends JpaRepository<StudyHistory,Integer> {
    List<StudyHistory> findStudyHistoryByStudentId(Integer studenId);
}
