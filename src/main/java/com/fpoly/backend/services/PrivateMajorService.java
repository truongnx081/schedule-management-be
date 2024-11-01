package com.fpoly.backend.services;

import com.fpoly.backend.entities.PrivateMajor;
import org.springframework.stereotype.Service;

@Service
public interface PrivateMajorService {
    PrivateMajor findById(Integer id);
}
