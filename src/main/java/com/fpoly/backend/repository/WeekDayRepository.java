package com.fpoly.backend.repository;

import com.fpoly.backend.entities.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeekDayRepository extends JpaRepository<WeekDay,Integer> {
}
