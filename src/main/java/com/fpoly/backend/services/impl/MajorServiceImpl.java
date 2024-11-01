package com.fpoly.backend.services.impl;

import com.fpoly.backend.entities.Major;
import com.fpoly.backend.repository.MajorRepository;
import com.fpoly.backend.services.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MajorServiceImpl implements MajorService {

    @Autowired
    MajorRepository majorRepository;

    @Override
    public Major findById(Integer id) {
        return majorRepository.findById(id).orElse(null);
    }
}
