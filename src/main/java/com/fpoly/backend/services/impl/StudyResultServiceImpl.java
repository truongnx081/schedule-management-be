package com.fpoly.backend.services.impl;

//

import com.fpoly.backend.dto.StudyResultDTO;
import com.fpoly.backend.entities.*;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.mapper.StudyResultMapper;
import com.fpoly.backend.repository.*;
import com.fpoly.backend.services.IdentifyUserAccessService;
import com.fpoly.backend.services.MarkColumnService;
import com.fpoly.backend.services.StudyInService;
import com.fpoly.backend.services.StudyResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class StudyResultServiceImpl implements StudyResultService {

    @Autowired
    StudyResultRepository studyResultRepository;
    @Autowired
    IdentifyUserAccessService identifyUserAccessService;
    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private YearRepository yearRepository;
    @Autowired
    private StudyResultMapper studyResultMapper;
    @Autowired
    private StudyInService studyInService;
    @Autowired
    private ClazzRepository clazzRepository;

    @Autowired
    ApplyForRepository applyForRepository;

    @Autowired
    private MarkColumnRepository markColumnRepository;
    @Autowired
    private StudyInRepository studyInRepository;

    @Override
    public Map<String, Integer> getreportLearningProgressByStudentId() {
        Integer studentId = identifyUserAccessService.getStudent().getId();

        Integer totalSubjects = applyForRepository.countSubjectByStudent(studentId);
        Integer totalSubjectsPass = studyResultRepository.countSubjectPassByStudent(studentId);

        Integer totalUnfinishedSubjects = totalSubjects - totalSubjectsPass;
        return Map.of("passedSubjects", totalSubjectsPass, "unfinishedSubjects", totalUnfinishedSubjects);
    }



    @Override
    public List<Map<String, Object>> getAllStudyResultByStudentId() {
        Integer studentId = identifyUserAccessService.getStudent().getId();
            return studyResultRepository.getAllStudyResultByStudentId(studentId);
    }

    @Override
    public List<Map<String, Object>> findAllByStudyInId(Integer studyInId) {
        return studyResultRepository.findAllByStudyInIdOfStudent(studyInId);
    }

    @Override
    public Map<String, Object> countPassAndFalseByBlockAndSemesterAndYearOfStudent(Integer blockId, String semesterId, Integer yearId) {
        int studentPass = 0;
        int studentFalse = 0;
        Double markAverage = 0.0;
        Map<String, Object> map = new HashMap<>();

        for (Map<String, Object> studyIn : studyInService.getAllIdOfStudyInByBlockAndSemesterAndYearOfStudent(blockId, semesterId, yearId)) {
            Integer id = (Integer) studyIn.get("id");
            Double marked = 0.0;
            Double percentage = 0.0;
            boolean breakOccurred = true; // Biến cờ để đánh dấu

            // Kiểm tra tất cả điểm của mỗi sinh viên
            for (Map<String, Object> studyResult : findAllByStudyInId(id)) {
                Double mark = (Double) studyResult.get("marked");
                Double percent = (Double) studyResult.get("percentage");
                if(mark < 5 && percent >= 30){
                    studentFalse += 1;
                    breakOccurred = false; // Đánh dấu cờ khi break xảy ra
                    break;
                }
                else{
                    marked += mark * (percent / 100);
                    percentage += (percent / 100);
                }
            }

            // Tính điểm trung bình
            if(breakOccurred){
                markAverage = marked / percentage;
                if(markAverage >= 5.0)
                    studentPass += 1;
                else
                    studentFalse += 1;
            }
        }
        map.put("studentPass", studentPass);
        map.put("studentFalse", studentFalse);

        return map;
    }

    @Override
    public List<Map<String, Object>> getAllMarkColumn(Integer clazzId, Integer studentId) {
        // Lấy thông tin instructor đang đăng nhập
        Integer instructorId = identifyUserAccessService.getInstructor().getId();

        // Kiểm tra xem instructor có quản lý lớp này không
        boolean isInstructorOfClass = clazzRepository.existsByIdAndInstructorId(clazzId, instructorId);
        if (!isInstructorOfClass) {
            throw new AppUnCheckedException("Bạn không có quyền xem điểm cho lớp học này!", HttpStatus.FORBIDDEN);
        }

        // Truy vấn dữ liệu điểm của sinh viên
        try {
            return studyResultRepository.getAllMarkColumn(clazzId, studentId);
        } catch (Exception e) {
            // Xử lý lỗi trong quá trình truy vấn
            throw new AppUnCheckedException("Đã xảy ra lỗi khi truy vấn cơ sở dữ liệu: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public StudyResultDTO createStudyResult(Integer studentId, StudyResultDTO studyResultDTO) {
        String instructorCode = identifyUserAccessService.getInstructor().getCode();
        Integer instructorId = identifyUserAccessService.getInstructor().getId();
        StudyResult studyResult = studyResultMapper.toEntity(studyResultDTO);
        if (studyResultDTO.getMarkColumnId() == null || studyResultDTO.getStudyInId() == null) {
            throw new AppUnCheckedException("Dữ liệu đầu vào không hợp lệ!", HttpStatus.BAD_REQUEST);
        }
        boolean exists = studyResultRepository.existsByMarkColumnIdAndStudyInId(
                studyResultDTO.getMarkColumnId(),
                studyResultDTO.getStudyInId()
        );
        if (exists) {
            throw new AppUnCheckedException("Kết quả học tập cho sinh viên này đã tồn tại!", HttpStatus.CONFLICT);
        }
        MarkColumn markColumn = markColumnRepository.findById(studyResultDTO.getMarkColumnId())
                .orElseThrow(()-> new AppUnCheckedException("Mark column không tồn tại !", HttpStatus.NOT_FOUND));
        studyResult.setMarkColumn(markColumn);
        StudyIn studyIn = studyInRepository.findById(studyResultDTO.getStudyInId())
                .orElseThrow(()-> new AppUnCheckedException("Study in không tồn tại !", HttpStatus.NOT_FOUND));
        studyResult.setStudyIn(studyIn);

        if(!instructorId.equals(studyResult.getStudyIn().getClazz().getInstructor().getId())){
            throw new AppUnCheckedException("Bạn không có quyền tạo kết quả học tập cho sinh viên này!", HttpStatus.FORBIDDEN);
        }
        studyResult.setCreatedBy(instructorCode);
        studyResult.setPercentage(studyResultDTO.getPercentage());
        return studyResultMapper.toDTO(studyResultRepository.save(studyResult));
    }

    @Override
    public StudyResultDTO updateStudyResult(Integer studyResultId, StudyResultDTO studyResultDTO) {
        String currentInstructorCode = identifyUserAccessService.getInstructor().getCode();

        // Lấy StudyResult từ DB
        StudyResult studyResult = studyResultRepository.findById(studyResultId)
                .orElseThrow(() -> new AppUnCheckedException("Kết quả học tập không tồn tại!", HttpStatus.NOT_FOUND));

        if (!studyResult.getCreatedBy().equals(currentInstructorCode)) {
            throw new AppUnCheckedException("Bạn không có quyền cập nhật kết quả học tập này!", HttpStatus.FORBIDDEN);
        }
        if (studyResultDTO.getMarkColumnId() != null) {
            MarkColumn markColumn = markColumnRepository.findById(studyResultDTO.getMarkColumnId())
                    .orElseThrow(() -> new AppUnCheckedException("Mark column không tồn tại!", HttpStatus.NOT_FOUND));
            studyResult.setMarkColumn(markColumn);
        }
        if (studyResultDTO.getStudyInId() != null) {
            StudyIn studyIn = studyInRepository.findById(studyResultDTO.getStudyInId())
                    .orElseThrow(() -> new AppUnCheckedException("Study in không tồn tại!", HttpStatus.NOT_FOUND));
            studyResult.setStudyIn(studyIn);
        }
        studyResult.setMarked(studyResultDTO.getMarked());
        studyResult.setPercentage(studyResultDTO.getPercentage());
        studyResult.setUpdatedBy(currentInstructorCode);
        return studyResultMapper.toDTO(studyResultRepository.save(studyResult));
    }

    @Override
    public List<Map<String, Object>> getAllMarkDetail(Integer clazzId, Integer subjectId) {
        Integer studentId = identifyUserAccessService.getStudent().getId();
        return studyResultRepository.getAllMarkDetail(clazzId,subjectId, studentId);
    }


}
