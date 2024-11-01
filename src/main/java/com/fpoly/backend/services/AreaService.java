package com.fpoly.backend.services;

import com.fpoly.backend.entities.Area;
import org.springframework.stereotype.Service;

@Service
public interface AreaService {
    Area findById(Integer id);
}
