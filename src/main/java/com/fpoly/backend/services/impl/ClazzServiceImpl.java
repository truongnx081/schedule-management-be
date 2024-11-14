package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.ClazzDTO;
import com.fpoly.backend.dto.StudyInDTO;
import com.fpoly.backend.entities.*;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.mapper.ClazzMapper;
import com.fpoly.backend.mapper.StudyInMapper;
import com.fpoly.backend.repository.*;
import com.fpoly.backend.services.AuthenticationService;
import com.fpoly.backend.services.ClazzService;
import com.fpoly.backend.services.IdentifyUserAccessService;
import com.fpoly.backend.services.SemesterProgressService;
import com.fpoly.backend.until.ExcelUtility;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class ClazzServiceImpl implements ClazzService {
    @Autowired
    ClazzRepository clazzRepository;
    ClazzMapper clazzMapper;
    ClazzRepository clazzRepository;
    BlockRepository blockRepository;
    SemesterRepository semesterRepository;
    YearRepository yearRepository;
    SubjectRepository subjectRepository;
    InstructorRepository instructorRepository;
    IdentifyUserAccessService identifyUserAccessService;
    ShiftRepository shiftRepository;
    RoomRepository roomRepository;
    SemesterProgressService semesterProgressService;
    ExcelUtility excelUtility;

    @Override
    public ClazzDTO create(ClazzDTO request) {
        Clazz clazz =  clazzMapper.toEntity(request);
        // Tìm block
        Block block = blockRepository.findById(request.getBlock()).orElseThrow(() ->
                new RuntimeException("Block not found"));
        // Tìm semester
        Semester semester = semesterRepository.findById(request.getSemester()).orElseThrow(() ->
                new RuntimeException("Semester not found"));
        // Tìm year
        Year year = yearRepository.findById(request.getYear()).orElseThrow(() ->
                new RuntimeException("Year not found"));
        // Tìm subject
        Subject subject = subjectRepository.findById(request.getSubjectId()).orElseThrow(() ->
                new RuntimeException("Subject not found"));
        // Tìm instructor
        Instructor instructor = instructorRepository.findById(request.getInstructorId()).orElseThrow(() ->
                new RuntimeException("Instructor not found"));
        // Tìm admin
        Admin admin = identifyUserAccessService.getAdmin();
        // Tìm shift
        Shift shift = shiftRepository.findById(request.getShiftId()).orElseThrow(() ->
                new RuntimeException("Shift not found"));
        // Tìm room
        Room room = roomRepository.findById(request.getRoomId()).orElseThrow(() ->
                new RuntimeException("Room not found"));

        clazz.setBlock(block);
        clazz.setSemester(semester);
        clazz.setYear(year);
        clazz.setSubject(subject);
        clazz.setInstructor(instructor);
        clazz.setAdmin(admin);
        clazz.setShift(shift);
        clazz.setRoom(room);

        return clazzMapper.toDTO(clazzRepository.save(clazz));
    }

    @Override
    public ClazzDTO update(ClazzDTO request, Integer id) {
        Clazz clazz = clazzRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Clazz not found"));
        clazzMapper.updateClazz(clazz, request);

        // Tìm block
        Block block = blockRepository.findById(request.getBlock()).orElseThrow(() ->
                new RuntimeException("Block not found"));
        // Tìm semester
        Semester semester = semesterRepository.findById(request.getSemester()).orElseThrow(() ->
                new RuntimeException("Semester not found"));
        // Tìm year
        Year year = yearRepository.findById(request.getYear()).orElseThrow(() ->
                new RuntimeException("Year not found"));
        // Tìm subject
        Subject subject = subjectRepository.findById(request.getSubjectId()).orElseThrow(() ->
                new RuntimeException("Subject not found"));
        // Tìm instructor
        Instructor instructor = instructorRepository.findById(request.getInstructorId()).orElseThrow(() ->
                new RuntimeException("Instructor not found"));
        // Tìm admin
        Admin admin = identifyUserAccessService.getAdmin();
        // Tìm shift
        Shift shift = shiftRepository.findById(request.getShiftId()).orElseThrow(() ->
                new RuntimeException("Shift not found"));
        // Tìm room
        Room room = roomRepository.findById(request.getRoomId()).orElseThrow(() ->
                new RuntimeException("Room not found"));

        clazz.setBlock(block);
        clazz.setSemester(semester);
        clazz.setYear(year);
        clazz.setSubject(subject);
        clazz.setInstructor(instructor);
        clazz.setAdmin(admin);
        clazz.setShift(shift);
        clazz.setRoom(room);

        return clazzMapper.toDTO(clazzRepository.save(clazz));
    }

    @Override
    public void delete(Integer id) {
        clazzRepository.deleteById(id);
    }

    @Override
    public ClazzDTO getOne(Integer id) {
        Clazz clazz = clazzRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Clazz not found"));

        return clazzMapper.toDTO(clazz);
    }

    @Override
    public List<ClazzDTO> getAll() {
        return clazzRepository.findAll().stream()
                .map(clazzMapper::toDTO).toList();
    }

    @Override
    public List<Map<String, Object>> findClazzByBlockAndSemesterAndYear() {
        SemesterProgress semesterProgress = semesterProgressService.findActivedProgressTrue();
        Integer block = semesterProgress.getBlock().getBlock();
        String semester = semesterProgress.getSemester().getSemester();
        Integer year = semesterProgress.getYear().getYear();

        return clazzRepository.findClazzByBlockAndSemesterAndYear(block, semester, year);
    }

    @Override
    public void importClazz(MultipartFile file) {
        try {
            List<Clazz> clazzList = excelUtility.excelToClazzList(file.getInputStream());
            clazzRepository.saveAll(clazzList);
        } catch (IOException ex) {
            throw new RuntimeException("Excel data is failed to store: " + ex.getMessage());
        }
    }
}
