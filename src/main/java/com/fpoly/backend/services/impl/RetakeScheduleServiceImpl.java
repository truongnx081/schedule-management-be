package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.RoomDTO;
import com.fpoly.backend.dto.ShiftDTO;
import com.fpoly.backend.entities.Instructor;
import com.fpoly.backend.entities.RetakeSchedule;
import com.fpoly.backend.entities.Student;
import com.fpoly.backend.repository.InstructorRepository;
import com.fpoly.backend.repository.RetakeScheduleRepository;
import com.fpoly.backend.repository.RoomRepository;
import com.fpoly.backend.repository.StudentRepository;
import com.fpoly.backend.services.IdentifyUserAccessService;
import com.fpoly.backend.services.RetakeScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RetakeScheduleServiceImpl implements RetakeScheduleService {


}
