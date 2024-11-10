package com.fpoly.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Setter
@Getter
@Table(name = "mark_columns")
public class MarkColumn extends AbstractEntity<Integer>{
    @Column(name = "name", columnDefinition = "NVARCHAR(255)")
    private String name;


    @JsonIgnore
    @OneToMany(mappedBy = "markColumn", fetch = FetchType.LAZY)
    private List<StudyResult> studyResults;

    @JsonIgnore
    @OneToMany(mappedBy = "markColumn", fetch = FetchType.LAZY)
    private List<SubjectMark> subjectMarks;
}
