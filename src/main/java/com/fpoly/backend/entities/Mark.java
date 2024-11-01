package com.fpoly.backend.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Setter
@Getter
@Table(name = "marks")
public class Mark extends AbstractEntity<Integer>{
    @Column(name = "marked")
    private Double marked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mark_column_id")
    private MarkColumn markColumn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_history_id")
    private StudyHistory studyHistory;
}
