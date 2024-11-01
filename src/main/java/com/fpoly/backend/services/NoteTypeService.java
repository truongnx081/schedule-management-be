package com.fpoly.backend.services;

import com.fpoly.backend.entities.NoteType;
import org.springframework.stereotype.Service;

@Service
public interface NoteTypeService {
    NoteType findById(Integer id);
}
