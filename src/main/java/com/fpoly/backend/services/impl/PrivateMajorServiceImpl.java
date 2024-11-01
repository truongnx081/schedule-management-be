package com.fpoly.backend.services.impl;

import com.fpoly.backend.entities.PrivateMajor;
import com.fpoly.backend.repository.PrivateMajorRepository;
import com.fpoly.backend.services.PrivateMajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrivateMajorServiceImpl implements PrivateMajorService {
    @Autowired
    PrivateMajorRepository privateMajorRepository;

    @Override
    public PrivateMajor findById(Integer id) {
        return privateMajorRepository.findById(id).orElse(null);
    }
}
