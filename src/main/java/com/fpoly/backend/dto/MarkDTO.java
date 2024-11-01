package com.fpoly.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MarkDTO {
    private Long id;

    private Double marked;

    private Integer markColumnId;

    private Integer studyHistoryId;
}
