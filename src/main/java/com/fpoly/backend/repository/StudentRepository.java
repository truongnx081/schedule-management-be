package com.fpoly.backend.repository;

import com.fpoly.backend.dto.ShiftDTO;
import com.fpoly.backend.dto.StudentDTO;
import com.fpoly.backend.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {
    boolean existsByEmail(String email);
    Optional<Student> findByEmail(String email);

    boolean existsByCode(String code);
    Optional<Student> findByCode(String code);

    @Query("SELECT s.id AS studentId, " +
            "s.code AS studentCode, " +
            "CONCAT(s.lastName, ' ', s.firstName) AS studentName, " +
            "s.email AS studentEmail," +
            "s.avatar as avatar " +
            "FROM StudyIn si " +
            "JOIN si.student s " +
            "JOIN si.clazz c " +
            "WHERE c.id = :clazzId AND c.instructor.id = :instructorId")
    List<Map<String, Object>> findStudentByClazzId(
            @Param("clazzId") Integer clazzId,
            @Param("instructorId") Integer instructorId);

    @Query("SELECT st.course as course FROM Student st group by st.course order by st.course desc ")
    List<Map<String, Object>> getAllStudentByCourse();
}
