package com.fpoly.backend.repository;

import com.fpoly.backend.entities.StudyDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyDayRepository extends JpaRepository<StudyDay,Integer> {
}
