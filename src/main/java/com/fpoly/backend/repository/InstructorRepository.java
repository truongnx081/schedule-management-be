package com.fpoly.backend.repository;

import com.fpoly.backend.entities.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor,Integer> {
    boolean existsBySchoolEmail(String schoolEmail);
    Optional<Instructor> findBySchoolEmail(String schoolEmail);
}
