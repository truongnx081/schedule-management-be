package com.fpoly.backend.repository;

import com.fpoly.backend.entities.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends JpaRepository<Area,Integer> {
}
