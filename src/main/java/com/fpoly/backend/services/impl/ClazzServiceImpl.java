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
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class ClazzServiceImpl implements ClazzService {
    ClazzRepository clazzRepository;
    ClazzMapper clazzMapper;
    BlockRepository blockRepository;
    SemesterRepository semesterRepository;
    YearRepository yearRepository;
    SubjectRepository subjectRepository;
    InstructorRepository instructorRepository;
    IdentifyUserAccessService identifyUserAccessService;
    ShiftRepository shiftRepository;
    RoomRepository roomRepository;
    ExcelUtility excelUtility;
    SemesterProgressRepository semesterProgressRepository;
    StudyInRepository studyInRepository;
    WeekDayRepository weekDayRepository;
    ScheduleRepository scheduleRepository;
    SemesterProgressService semesterProgressService;

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

    @Override
    public List<ClazzDTO> getClazzsByInstructorId() {
        Integer instructorId =identifyUserAccessService.getInstructor().getId();
        List<StudyIn> studyIns = studyInRepository.findByClazz_Instructor_Id(instructorId);
        return studyIns.stream()
                .map(studyIn -> clazzMapper.toDTO(studyIn.getClazz()))
                .distinct()
                .collect(Collectors.toList());
    }


    @Override
    public List<Map<String, Object>> findClazzesForRegisterByBlockAndSemesterAndYearAndStudentId() {
        SemesterProgress semesterProgress = semesterProgressRepository.findActivedProgress();
        Integer block = semesterProgress.getBlock().getBlock();
        String semester = semesterProgress.getSemester().getSemester();
        Integer year = semesterProgress.getYear().getYear();
        Student student = identifyUserAccessService.getStudent();

        //Tìm toàn bộ môn phải học trong CTDT
        List<Integer> allSubjects = subjectRepository.findSubjectsIdByEducationProgram(student.getEducationProgram().getId());
        System.out.println("All: " + allSubjects.toString());

        //Tìm các môn đã pass
        List<Integer> passedSubjects = subjectRepository.findPassedSubjectsIdByStudentId(student.getId());
        System.out.println("Passed: " + passedSubjects.toString());

        //Tìm các môn đăng ký học
        List<Integer> registedSubject = subjectRepository.findRegistedSubjectsIdByStudentIdAndBlockAndSemesterAndYear(
                student.getId(),
                block,
                semester,
                year);
        System.out.println("Registed: " + registedSubject.toString());

        //So sánh nếu pass môn thì cho môn đó là null
        for (int i = 0; i < allSubjects.size(); i++){
            for (int j = 0; j < passedSubjects.size(); j++){
                if (Objects.equals(allSubjects.get(i), passedSubjects.get(j))){
                    allSubjects.set(i, null);
                    break;
                }
            }
        }

        //So sánh nếu đã đăng ký môn này trong kỳ này thì môn đó là null
        for (int i = 0; i < allSubjects.size(); i++){
            for (int j = 0; j < registedSubject.size(); j++){
                if(Objects.equals(allSubjects.get(i), registedSubject.get(j))){
                    allSubjects.set(i, null);
                    break;
                }
            }
        }

        System.out.println(allSubjects.toString());
        List<Map<String, Object>> registerClazzes = new ArrayList<Map<String, Object>>();

        //Nếu môn đó null thì không cần tải lớp có môn đó lên nữa
        for (int i = 0; i < allSubjects.size(); i++){
            if (allSubjects.get(i) != null){
                List<Map<String, Object>> clazzes = clazzRepository.findClazzesToRegistByBlockAndYearAndSemesterAndYearAndSubjectId(
                        block, semester, year, allSubjects.get(i));
                for (int j = 0; j < clazzes.size(); j++){
                    registerClazzes.add(clazzes.get(j));
                }
            }
        }

        for (int i = 0; i <registerClazzes.size(); i++){
            Map<String, Object> clazz = new HashMap<>(registerClazzes.get(i));

            Integer amout = studyInRepository.countStudentByClazzId((Integer) registerClazzes.get(i).get("id"));
            clazz.put("amout", amout);

            List<String> weekDays = weekDayRepository.findWeekDayByClazzId((Integer) clazz.get("id"));
            String studyDays = "";
            for (int j = 0; j < weekDays.size(); j++){
                if (j < weekDays.size() -1){
                    studyDays += weekDays.get(j) + ", ";
                } else {
                    studyDays += weekDays.get(j);
                }
            }
            clazz.put("study_day", studyDays);

            registerClazzes.set(i, clazz);
        }
        return registerClazzes;
    }

    @Override
    public List<Map<String, Object>> findCurrentClazzesByBlockAndSemesterAndYearAndStudentId() {
        SemesterProgress semesterProgress = semesterProgressRepository.findActivedProgress();
        Integer block = semesterProgress.getBlock().getBlock();
        String semester = semesterProgress.getSemester().getSemester();
        Integer year = semesterProgress.getYear().getYear();
        Student student = identifyUserAccessService.getStudent();
        List<Map<String,Object>> clazzes = clazzRepository.findCurrentClassesByBlockAndSemesterAndYearAndStudentId(block, semester, year, student.getId());

        for (int i = 0; i < clazzes.size(); i++){
            HashMap<String, Object> clazz = new HashMap<>(clazzes.get(i));
            List<String> weekDays = weekDayRepository.findWeekDayByClazzId((Integer) clazz.get("id"));
            String studyDays = "";
            for (int j = 0; j < weekDays.size(); j++){
                if (j < weekDays.size() -1){
                    studyDays += weekDays.get(j) + ", ";
                } else {
                    studyDays += weekDays.get(j);
                }
            }
            clazz.put("study_day", studyDays);
            clazzes.set(i,clazz);
        }
        return clazzes;
    }


    @Override
    public Map<String, Object> findClazzToChangeShiftById(Integer id) {
        HashMap<String, Object> clazz = new HashMap<>(clazzRepository.findClazzToChangeByClazzId(id));
        List<String> weekDays = weekDayRepository.findWeekDayByClazzId((Integer) clazz.get("id"));
        String studyDays = "";
        for (int j = 0; j < weekDays.size(); j++){
            if (j < weekDays.size() -1){
                studyDays += weekDays.get(j) + ", ";
            } else {
                studyDays += weekDays.get(j);
            }
        }
        clazz.put("study_day", studyDays);
        return clazz;
    }

    @Override
    public List<Map<String, Object>> findClazzesBySubjectIdAndShiftAndBlockAndSemesterAndYear(Integer subjectId, Integer shift) {
        SemesterProgress semesterProgress = semesterProgressRepository.findActivedProgress();
        Integer block = semesterProgress.getBlock().getBlock();
        String semester = semesterProgress.getSemester().getSemester();
        Integer year = semesterProgress.getYear().getYear();

        List<Map<String,Object>> clazzes = clazzRepository.findClazzesBySubjectIdAndShiftAndBlockAndSemesterAndYear
                (subjectId, shift,block,semester,year);

        for (int i = 0; i <clazzes.size(); i++){
            Map<String, Object> clazz = new HashMap<>(clazzes.get(i));

            Integer amout = studyInRepository.countStudentByClazzId((Integer) clazz.get("clazz_id"));
            clazz.put("amout", amout);

            List<String> weekDays = weekDayRepository.findWeekDayByClazzId((Integer) clazz.get("clazz_id"));
            String studyDays = "";
            for (int j = 0; j < weekDays.size(); j++){
                if (j < weekDays.size() -1){
                    studyDays += weekDays.get(j) + ", ";
                } else {
                    studyDays += weekDays.get(j);
                }
            }
            clazz.put("study_day", studyDays);

            List<LocalDate> dates = scheduleRepository.findDateByClazzId((Integer) clazz.get("clazz_id"));
            if (!dates.isEmpty()) {
                clazz.put("start_date", dates.get(0));
            }


            clazzes.set(i, clazz);
        }


        return clazzes;
    }

    @Override
    public List<Map<String, Object>> findClazzesByInstructorIdAndBlockAndSemesterAndYear(Integer block, String semester, Integer year) {
        Instructor instructor = identifyUserAccessService.getInstructor();
        List<Map<String,Object>> clazzes = clazzRepository.findClazzesByInstructorIdAndBlockAndSemesterAndYear(
                instructor.getId(),
                block,
                semester,
                year);
        for (int i = 0; i < clazzes.size(); i++){
            HashMap<String, Object> clazz = new HashMap<>(clazzes.get(i));
            List<String> weekDays = weekDayRepository.findWeekDayByClazzId((Integer) clazz.get("id"));
            String studyDays = "";
            for (int j = 0; j < weekDays.size(); j++){
                if (j < weekDays.size() -1){
                    studyDays += weekDays.get(j) + ", ";
                } else {
                    studyDays += weekDays.get(j);
                }
            }
            clazz.put("study_day", studyDays);
            clazzes.set(i,clazz);
        }

        return clazzes;
    }
}
