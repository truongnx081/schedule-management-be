package com.fpoly.backend.repository;

import com.fpoly.backend.dto.ShiftDTO;
import com.fpoly.backend.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {
    boolean existsByEmail(String email);
    Optional<Student> findByEmail(String email);

    boolean existsByCode(String code);
    Optional<Student> findByCode(String code);

}
