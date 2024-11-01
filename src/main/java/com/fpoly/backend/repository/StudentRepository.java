package com.fpoly.backend.repository;

import com.fpoly.backend.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {
    boolean existsByEmail(String email);
    Optional<Student> findByEmail(String email);

    boolean existsByCode(String code);
    Optional<Student> findByCode(String code);
}
