package com.fpoly.backend.repository;

import com.fpoly.backend.entities.ApplyFor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyForRepository extends JpaRepository<ApplyFor,Integer> {
}
