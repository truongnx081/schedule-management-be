package com.fpoly.backend.services;

import com.fpoly.backend.dto.ShiftDTO;
import com.fpoly.backend.entities.Shift;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface ShiftService {

    public List<ShiftDTO> getAllShift();

    public List<ShiftDTO> getAvailableShifts(Integer classId);

    public Set<Integer> getAllShiftsByStudentByClassId(Integer clazzId);
}
