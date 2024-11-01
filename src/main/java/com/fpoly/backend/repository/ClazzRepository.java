package com.fpoly.backend.repository;

import com.fpoly.backend.entities.Clazz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ClazzRepository extends JpaRepository<Clazz,Integer> {

}
