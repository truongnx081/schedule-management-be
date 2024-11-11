package com.fpoly.backend.repository;

import com.fpoly.backend.entities.SemesterProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SemesterProgressRepository extends JpaRepository<SemesterProgress,Integer> {

    Optional<SemesterProgress> findByIsActive(Boolean isActive);
}
