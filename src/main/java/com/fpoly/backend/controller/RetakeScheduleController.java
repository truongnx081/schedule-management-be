package com.fpoly.backend.controller;

import com.fpoly.backend.dto.Response;
import com.fpoly.backend.dto.ShiftDTO;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.services.RetakeScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/retakeschedules")
@Validated
public class RetakeScheduleController {

    @Autowired
    private RetakeScheduleService retakeScheduleService;


}
