package com.fpoly.backend.services.impl;

import com.fpoly.backend.entities.MarkColumn;
import com.fpoly.backend.repository.MarkColumnRepository;
import com.fpoly.backend.services.MarkColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarkColumnServiceImpl implements MarkColumnService {

    @Autowired
    MarkColumnRepository markColumnRepository;

    @Override
    public MarkColumn findById(Integer id) {
        return markColumnRepository.findById(id).orElse(null);
    }
}
