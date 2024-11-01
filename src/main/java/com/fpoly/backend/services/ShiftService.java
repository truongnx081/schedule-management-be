package com.fpoly.backend.services;

import com.fpoly.backend.entities.Shift;
import org.springframework.stereotype.Service;

@Service
public interface ShiftService {
    Shift findById(Integer id);
}
