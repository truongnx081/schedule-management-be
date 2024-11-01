package com.fpoly.backend.repository;

import com.fpoly.backend.entities.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block,Integer> {
}
