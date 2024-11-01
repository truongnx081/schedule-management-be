package com.fpoly.backend.repository;

import com.fpoly.backend.entities.MarkColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkColumnRepository extends JpaRepository<MarkColumn,Integer> {
}
