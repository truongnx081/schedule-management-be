package com.fpoly.backend.repository;

import com.fpoly.backend.entities.StudyIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyInRepository extends JpaRepository<StudyIn,Integer> {
}
