package com.fpoly.backend.services.impl;

import com.fpoly.backend.entities.Year;
import com.fpoly.backend.repository.YearRepository;
import com.fpoly.backend.services.YearService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class YearServiceImpl implements YearService {

    @Autowired
    YearRepository yearRepository;
    @Override
    public Year findById(Integer year) {
        return yearRepository.findById(year).orElse(null);
    }
}
