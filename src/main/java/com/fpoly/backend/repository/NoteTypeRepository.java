package com.fpoly.backend.repository;

import com.fpoly.backend.entities.NoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteTypeRepository extends JpaRepository<NoteType,Integer> {
}
