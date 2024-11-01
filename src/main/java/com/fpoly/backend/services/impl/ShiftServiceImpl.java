package com.fpoly.backend.services.impl;

import com.fpoly.backend.entities.Shift;
import com.fpoly.backend.repository.ShiftRepository;
import com.fpoly.backend.services.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {
    @Autowired
    ShiftRepository shiftRepository;

    @Override
    public Shift findById(Integer id) {
        return shiftRepository.findById(id).orElse(null);
    }
}
