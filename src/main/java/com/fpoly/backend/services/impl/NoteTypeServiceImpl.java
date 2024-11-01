package com.fpoly.backend.services.impl;

import com.fpoly.backend.entities.NoteType;
import com.fpoly.backend.repository.NoteTypeRepository;
import com.fpoly.backend.services.NoteTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoteTypeServiceImpl implements NoteTypeService {
    @Autowired
    NoteTypeRepository noteTypeRepository;


    @Override
    public NoteType findById(Integer id) {
        return noteTypeRepository.findById(id).orElse(null);
    }
}
