package com.fpoly.backend.services;

import com.fpoly.backend.dto.YearDTO;
import com.fpoly.backend.entities.Year;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface YearService {
    Year findById(Integer year);
    List<YearDTO> findAll();
}
