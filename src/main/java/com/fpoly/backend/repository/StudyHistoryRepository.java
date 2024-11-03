package com.fpoly.backend.repository;

import com.fpoly.backend.entities.StudyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface StudyHistoryRepository extends JpaRepository<StudyHistory,Integer> {
    List<StudyHistory> findStudyHistoryByStudentId(Integer studenId);

    @Query("SELECT COUNT(DISTINCT stu.subject.id) " +
            "FROM StudyHistory stu " +
            "JOIN stu.marks m " +
            "JOIN m.markColumn mc " +
            "WHERE stu.student.id = :studentId " +
            "GROUP BY stu.subject " +
            "HAVING SUM(m.marked * mc.percentage) / 100 >= 5")
    Integer countSubjectPassByStudent(@Param("studentId") Integer studentId);
}
