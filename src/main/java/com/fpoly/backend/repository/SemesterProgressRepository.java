package com.fpoly.backend.repository;

import com.fpoly.backend.entities.SemesterProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterProgressRepository extends JpaRepository<SemesterProgress,Integer> {
}
