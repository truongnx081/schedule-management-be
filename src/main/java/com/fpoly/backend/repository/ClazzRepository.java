package com.fpoly.backend.repository;

import com.fpoly.backend.entities.Clazz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ClazzRepository extends JpaRepository<Clazz,Integer> {
    @Query("SELECT c.code as code," +
            "c.subject.name as subject_name," +
            "c.instructor.firstName as instructor_first_name," +
            "c.instructor.lastName as instrutor_last_name " +
            "FROM Clazz c " +
            "WHERE c.block.block = :block " +
            "AND c.semester.semester = :semester " +
            "AND c.year.year = :year")
    List<Map<String, Object>> findClazzByBlockAndSemesterAndYear (@Param("block") Integer block,
                                                                  @Param("semester") String semester,
                                                                  @Param("year") Integer year);

    boolean existsByCode(String code);

    boolean existsByIdAndInstructorId(Integer clazzId, Integer instructorId);
}
