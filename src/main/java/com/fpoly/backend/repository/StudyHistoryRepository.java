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

    @Query("SELECT s.code as subject_code, s.name as subject_name, s.credits as subject_credits, " +
            "SUM(mkc.percentage * m.marked) / 100 AS DiemTB \n" +
            "FROM StudyHistory st \n" +
            "JOIN st.subject s \n" +
            "JOIN st.student stu \n" +
            "JOIN st.marks m \n" +
            "JOIN m.markColumn mkc \n" +
            "WHERE stu.id = :studentId \n" +
            "GROUP BY s.code, s.name, s.credits")
    List<Map<String, Object>> getAllStudyHistoryByStudentId(@Param("studentId") Integer studentId);

}
