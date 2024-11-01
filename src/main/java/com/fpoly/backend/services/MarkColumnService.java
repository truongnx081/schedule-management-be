package com.fpoly.backend.services;

import com.fpoly.backend.entities.MarkColumn;
import org.springframework.stereotype.Service;

@Service
public interface MarkColumnService {
    MarkColumn findById(Integer id);
}
