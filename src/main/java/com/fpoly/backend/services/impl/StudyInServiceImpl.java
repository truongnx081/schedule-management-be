package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.Response;
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
import org.springframework.web.server.ResponseStatusException;

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
        Integer studentId = identifyUserAccessService.getStudent().getId();
        StudyIn studyIn = studyInMapper.toEntity(studyInDTO);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(()-> new AppUnCheckedException("Student not found", HttpStatus.NOT_FOUND));

        Clazz clazz = clazzRepository.findById(studyInDTO.getClazzId())
                .orElseThrow(()-> new AppUnCheckedException("Clazz not found", HttpStatus.NOT_FOUND));
        studyIn.setStudent(student);
        studyIn.setClazz(clazz);
        studyIn.setCreatedBy(studentCode);
        return studyInMapper.toDTO(studyInRepository.save(studyIn));
    }

    @Override
    public StudyInDTO updateStudyIn(Integer studyInId, StudyInDTO studyInDTO) {
        return null;
    }


    @Override
    public StudyInDTO createStudyIn2(Integer clazzId) {
        String studentCode = identifyUserAccessService.getStudent().getCode();
        Integer studentId = identifyUserAccessService.getStudent().getId();
        StudyIn studyIn = new StudyIn();
        Student student = studentRepository.findById(studentId)
                .orElseThrow(()-> new AppUnCheckedException("Student not found", HttpStatus.NOT_FOUND));
        Clazz clazz = clazzRepository.findById(clazzId)
                .orElseThrow(()-> new AppUnCheckedException("Clazz not found", HttpStatus.NOT_FOUND));

        // Kiểm tra xem sinh viên đã đăng ký lớp học này chưa
        boolean isAlreadyRegistered = studyInRepository.existsByStudentIdAndClazzId(studentId, clazzId);
        if (isAlreadyRegistered) {
            throw new AppUnCheckedException("Student has already registered for this class", HttpStatus.CONFLICT);
        }

        studyIn.setStudent(student);
        studyIn.setClazz(clazz);
        studyIn.setCreatedBy(studentCode);
        return studyInMapper.toDTO(studyInRepository.save(studyIn));
    }

    @Override
    public StudyInDTO updateStudyIn2(Integer studyInId, Integer newClazzId) {
        String studentCode = identifyUserAccessService.getStudent().getCode();
        Integer studentId = identifyUserAccessService.getStudent().getId();

        // Kiểm tra xem StudyIn có tồn tại không
        StudyIn existingStudyIn = studyInRepository.findById(studyInId)
                .orElseThrow(() -> new AppUnCheckedException("StudyIn not found", HttpStatus.NOT_FOUND));

        if (!existingStudyIn.getStudent().getId().equals(studentId)) {
            throw new AppUnCheckedException("You cannot update this StudyIn", HttpStatus.FORBIDDEN);
        }

        // Kiểm tra lớp học mới có tồn tại không
        Clazz newClazz = clazzRepository.findById(newClazzId)
                .orElseThrow(() -> new AppUnCheckedException("Clazz not found", HttpStatus.NOT_FOUND));

        // Kiểm tra xem sinh viên đã đăng ký lớp học mới chưa
        boolean isAlreadyRegistered = studyInRepository.existsByStudentIdAndClazzId(studentId, newClazzId);
        if (isAlreadyRegistered) {
            throw new AppUnCheckedException("Student has already registered for this class", HttpStatus.CONFLICT);
        }

        // Cập nhật lớp học mới cho StudyIn
        existingStudyIn.setClazz(newClazz);
        existingStudyIn.setUpdatedBy(studentCode);
        return studyInMapper.toDTO(studyInRepository.save(existingStudyIn));
    }
}
