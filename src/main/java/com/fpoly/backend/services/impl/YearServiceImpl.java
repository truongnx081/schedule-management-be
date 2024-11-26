package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.YearDTO;
import com.fpoly.backend.entities.Year;
import com.fpoly.backend.mapper.YearMapper;
import com.fpoly.backend.repository.YearRepository;
import com.fpoly.backend.services.YearService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class YearServiceImpl implements YearService {

    @Autowired
    YearRepository yearRepository;
    @Autowired
    private YearMapper yearMapper;

    @Override
    public Year findById(Integer year) {
        return yearRepository.findById(year).orElse(null);
    }

    @Override
    public List<YearDTO> findAll() {
        return yearRepository.findAll()
                .stream()
                .map(yearMapper::toDTO).toList();
    }
}
