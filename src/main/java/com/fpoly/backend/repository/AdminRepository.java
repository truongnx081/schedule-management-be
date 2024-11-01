package com.fpoly.backend.repository;

import com.fpoly.backend.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Integer> {
    boolean existsByEmail(String email);
    Optional<Admin> findByEmail(String email);
}
