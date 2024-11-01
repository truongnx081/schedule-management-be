package com.fpoly.backend.services;

import com.fpoly.backend.entities.Year;
import org.springframework.stereotype.Service;

@Service
public interface YearService {
    Year findById(Integer year);
}
