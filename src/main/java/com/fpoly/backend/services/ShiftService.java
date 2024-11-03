package com.fpoly.backend.services;

import com.fpoly.backend.dto.ShiftDTO;
import com.fpoly.backend.entities.Shift;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShiftService {

    public List<ShiftDTO> getAllShift();
}
