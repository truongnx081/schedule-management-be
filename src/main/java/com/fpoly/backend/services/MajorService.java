package com.fpoly.backend.services;

import com.fpoly.backend.entities.Major;
import org.springframework.stereotype.Service;

@Service
public interface MajorService {
    Major findById(Integer id);
}
