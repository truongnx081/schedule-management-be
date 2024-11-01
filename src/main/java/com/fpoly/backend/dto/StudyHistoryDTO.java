package com.fpoly.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudyHistoryDTO {
    private Integer id;

    private Integer block;

    private String semester;

    private Integer year;

    private Integer studentId;

    private Integer subjectId;
}
