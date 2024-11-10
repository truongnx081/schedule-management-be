package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.ShiftDTO;
import com.fpoly.backend.mapper.ShiftMapper;
import com.fpoly.backend.repository.ShiftRepository;
import com.fpoly.backend.services.IdentifyUserAccessService;
import com.fpoly.backend.services.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {
    @Autowired
    ShiftRepository shiftRepository;
    @Autowired
    ShiftMapper shiftMapper;
    @Autowired
    IdentifyUserAccessService identifyUserAccessService;

    @Override
    public List<ShiftDTO> getAllShift() {
        return shiftRepository.findAll()
                .stream().map(shiftMapper::toDTO).toList();
    }

    @Override
    public List<ShiftDTO> getAvailableShifts(Integer clazzId) {

        Integer instructorId = identifyUserAccessService.getInstructor().getId();
        List<Integer> studentShifts = shiftRepository.findStudentScheduleShifts(clazzId);
        List<Integer> examShifts = shiftRepository.findStudentExamShifts(clazzId);
        List<Integer> retakeShifts = shiftRepository.findStudentRetakeShifts(clazzId);
        List<Integer> instructorShift = shiftRepository.findInstructorShifts(clazzId,instructorId);
        List<ShiftDTO> allShiftsAvailble = getAllShift();
        Set<Integer> bookedShifts = new HashSet<>();
        
        bookedShifts.addAll(studentShifts);
        bookedShifts.addAll(examShifts);
        bookedShifts.addAll(retakeShifts);
        bookedShifts.addAll(instructorShift);
        
        return allShiftsAvailble.stream()
                .filter(shift -> !bookedShifts.contains(shift.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Set<Integer> getAllShiftsByStudentByClassId(Integer clazzId) {
        List<Integer> studentShifts = shiftRepository.findStudentScheduleShifts(clazzId);
        List<Integer> studentExamShifts = shiftRepository.findStudentExamShifts(clazzId);
        List<Integer> studentRetakeShifts = shiftRepository.findStudentRetakeShifts(clazzId);

        Set<Integer> allStudentShifts = new HashSet<>();
        allStudentShifts.addAll(studentShifts);
        allStudentShifts.addAll(studentExamShifts);
        allStudentShifts.addAll(studentRetakeShifts);

        return allStudentShifts;
    }
}
