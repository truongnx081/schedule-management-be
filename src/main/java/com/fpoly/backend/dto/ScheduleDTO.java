package com.fpoly.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScheduleDTO {
    private Integer id;

    private LocalDate date;

    private Integer clazzId;

    boolean status;
}
