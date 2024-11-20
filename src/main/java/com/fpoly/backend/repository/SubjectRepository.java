package com.fpoly.backend.repository;

import com.fpoly.backend.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SubjectRepository extends JpaRepository<Subject,Integer> {

    @Query("SELECT s.id as id, s.code as code, s.name as name," +
            " s.credits as credist, s.total_hours as total_hours," +
            "s.mission as mission, s.note as note, s.description as description , s.required.name as required " +
            "FROM Subject s " +
            "WHERE s.specialization.id = :specializationId " )
    List<Map<String, Object>> getAllSubjectBySpecializationId(@Param("specializationId") Integer specializationId);

    @Query("SELECT s.id AS id, s.code AS code, s.name AS name, " +
            "s.credits AS credits, s.total_hours AS total_hours, " +
            "s.mission AS mission, s.note AS note, s.description AS description, " +
            "r.name AS required " +
            "FROM Subject s " +
            "LEFT JOIN s.required r")
    List<Map<String, Object>> findAllSubject();

    @Query("SELECT sub.name as sub_name, sub.credits as sub_credits, sub.total_hours as total_hours, " +
            "sub.code as sub_code, sub.offline as offine, sub.description as description, " +
            "sub.mission as mission, sub.note as note, r.name as required_sub, spe.name as specializaton_name " +
            "FROM Subject sub " +
            "LEFT JOIN sub.required r " +
            "JOIN sub.specialization spe " +
            "where sub.id = :subjectId")
    List<Map<String, Object>> getSubjectDetailById(@Param("subjectId") Integer subjectId);
}
