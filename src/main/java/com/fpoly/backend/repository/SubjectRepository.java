package com.fpoly.backend.repository;

import com.fpoly.backend.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
            "r.name AS required, s.specialization.id AS specializationId " +
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

    @Query("SELECT s.id " +
            "FROM Subject s " +
            "JOIN s.applyFors af " +
            "WHERE af.educationProgram.id = :educationProgramId")
    List<Integer> findSubjectsIdByEducationProgram(@Param("educationProgramId") Integer educationProgramId);

    @Query("SELECT c.subject.id " +
            "FROM Clazz c " +
            "JOIN c.studyIns si " +
            "JOIN si.studyResults sr " +
            "WHERE si.student.id = :studentId " +
            "GROUP BY c.subject.id " +
            "HAVING SUM(sr.marked * sr.percentage)/100 >= 5")
    List<Integer> findPassedSubjectsIdByStudentId(@Param("studentId") Integer studentId);

    @Query("SELECT c.subject.id " +
            "FROM Clazz c " +
            "JOIN c.studyIns si " +
            "WHERE c.block.block = :block AND c.semester.semester = :semester AND c.year.year = :year AND si.student.id = :studentId")
    List<Integer> findRegistedSubjectsIdByStudentIdAndBlockAndSemesterAndYear(
            @Param("studentId") Integer studentId,
            @Param("block") Integer block,
            @Param("semester") String semester,
            @Param("year") Integer year);

    @Query("SELECT s.id FROM Subject s JOIN Clazz c WHERE c.id = :clazzId")
    Integer findSubjectByClazzId (@Param("clazzId") Integer clazzId);

    Optional<Subject> findByCode(String code);

    @Query("SELECT s.id as id, s.name as name " +
            "FROM ApplyFor a " +
            "JOIN a.subject s " +
            "JOIN a.educationProgram e " +
            "WHERE e.id =:educationProgramId")
    List<Map<String, Object>> findAllSubjectByEducationProgramId(@Param("educationProgramId") Integer educationProgramId);
}
