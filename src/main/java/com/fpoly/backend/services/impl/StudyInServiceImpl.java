package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.StudyInDTO;
import com.fpoly.backend.entities.*;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.mapper.StudyInMapper;
import com.fpoly.backend.repository.*;
import com.fpoly.backend.services.IdentifyUserAccessService;
import com.fpoly.backend.services.SemesterProgressService;
import com.fpoly.backend.services.StudyInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudyInServiceImpl implements StudyInService {
    private final SemesterProgressService semesterProgressService;
    private final BlockRepository blockRepository;
    private final SemesterRepository semesterRepository;
    private final YearRepository yearRepository;
    private final StudyInRepository studyInRepository;
    private final StudyInMapper studyInMapper;
    private final IdentifyUserAccessService identifyUserAccessService;
    private final ClazzRepository clazzRepository;
    private final StudentRepository studentRepository;

    @Override
    public List<StudyInDTO> findAllByBlockAndSemesterAnhYear() {
        SemesterProgress semesterProgress = semesterProgressService.findActivedProgressTrue();
        Integer blockId = semesterProgress.getBlock().getBlock();
        String semesterId = semesterProgress.getSemester().getSemester();
        Integer yearId = semesterProgress.getYear().getYear();

        Block block =  blockRepository.findById(blockId).orElseThrow(()->
                new RuntimeException("Block not found"));
        Semester semester =  semesterRepository.findById(semesterId).orElseThrow(()->
                new RuntimeException("Semester not found"));
        Year year =  yearRepository.findById(yearId).orElseThrow(()->
                new RuntimeException("Year not found"));

        return studyInRepository
                .findAllByClazzBlockAndClazzSemesterAndClazzYear(block,semester,year)
                .stream()
                .map(studyInMapper::toDTO).toList();
    }

    @Override
    public List<Map<String, Object>> getAllIdOfStudyInByBlockAndSemesterAndYearOfStudent(Integer blockId, String semesterId, Integer yearId) {
        return studyInRepository.getAllIdOfStudyInByBlockAndSemesterAndYearOfStudent(
                blockId, semesterId, yearId
        );
    }

    @Override
    public List<Map<String, Object>> getAllMarkAverageStudentsByClazzId(Integer clazzId) {

        Integer instructorId = identifyUserAccessService.getInstructor().getId();
        Clazz clazz = clazzRepository.findById(clazzId)
                .orElseThrow(() -> new AppUnCheckedException("Lớp học không tồn tại", HttpStatus.NOT_FOUND));
        if(!instructorId.equals(clazz.getInstructor().getId())) {
            throw new AppUnCheckedException("Bạn không có quyền xem danh sách này !", HttpStatus.FORBIDDEN);
        }
        return studyInRepository.getAllMarkAverageStudentsByClazzId(clazzId);
    }

    @Override
    public StudyInDTO createStudyIn(StudyInDTO studyInDTO) {
        String studentCode = identifyUserAccessService.getStudent().getCode();
        StudyIn studyIn = studyInMapper.toEntity(studyInDTO);
        Student student = studentRepository.findById(studyInDTO.getStudentId())
                .orElseThrow(()-> new AppUnCheckedException("Student not found", HttpStatus.NOT_FOUND));
        studyIn.setStudent(student);
        Clazz clazz = clazzRepository.findById(studyInDTO.getClazzId())
                .orElseThrow(()-> new AppUnCheckedException("Clazz not found", HttpStatus.NOT_FOUND));
        studyIn.setClazz(clazz);
        return studyInMapper.toDTO(studyInRepository.save(studyIn));
    }
}
