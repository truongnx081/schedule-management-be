package com.fpoly.backend.repository;

import com.fpoly.backend.entities.StudyDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyDayRepository extends JpaRepository<StudyDay,Integer> {
}
