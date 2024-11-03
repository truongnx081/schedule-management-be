package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.ShiftDTO;
import com.fpoly.backend.entities.Shift;
import com.fpoly.backend.mapper.ShiftMapper;
import com.fpoly.backend.repository.ShiftRepository;
import com.fpoly.backend.services.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {
    @Autowired
    ShiftRepository shiftRepository;
    @Autowired
    ShiftMapper shiftMapper;

    @Override
    public List<ShiftDTO> getAllShift() {
        return shiftRepository.findAll()
                .stream().map(shiftMapper::toDTO).toList();
    }
}
