package com.fpoly.backend.repository;

import com.fpoly.backend.entities.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkRepository extends JpaRepository<Mark,Integer> {
}
