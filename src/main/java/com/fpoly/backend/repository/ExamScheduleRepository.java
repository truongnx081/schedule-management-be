package com.fpoly.backend.repository;

import com.fpoly.backend.entities.ExamSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamScheduleRepository extends JpaRepository<ExamSchedule,Integer> {
}
