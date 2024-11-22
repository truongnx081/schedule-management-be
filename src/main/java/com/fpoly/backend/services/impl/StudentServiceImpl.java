package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.CloudinaryResponse;
import com.fpoly.backend.dto.StudentDTO;
import com.fpoly.backend.entities.*;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.mapper.StudentMapper;
import com.fpoly.backend.repository.*;
import com.fpoly.backend.services.CloudinaryService;
import com.fpoly.backend.services.IdentifyUserAccessService;
import com.fpoly.backend.services.StudentService;
import com.fpoly.backend.until.FileUpload;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StudentServiceImpl implements StudentService {

    StudentRepository studentRepository;
    StudentMapper studentMapper;
    IdentifyUserAccessService identifyUserAccessService;
    CloudinaryService cloudinaryService;
    EducationProgramRepository educationProgramRepository;
    MajorRepository majorRepository;
    YearRepository yearRepository;
    SemesterRepository semesterRepository;
    StudyInRepository studyInRepository;

    @Override
    public Student findById(Integer id) {
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    public StudentDTO getStudentInfor() {
        Student student = identifyUserAccessService.getStudent();
        return studentMapper.toDTO(student);
    }

    @Override
    public StudentDTO getStudentById(Integer studentId) {
        return studentMapper.toDTO(studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentId)));
    }

    @Override
    public StudentDTO createStudent(StudentDTO request, MultipartFile file) {
        if (studentRepository.existsByCode(request.getCode())) {
            throw new AppUnCheckedException("Code existed", HttpStatus.CONFLICT);
        }
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new AppUnCheckedException("Email existed", HttpStatus.CONFLICT);
        }

        String adminCode = identifyUserAccessService.getAdmin().getCode();
        Student student = studentMapper.toEntity(request);
        if (file != null && !file.isEmpty()) {
            FileUpload.assertAllowed(file, FileUpload.IMAGE_PATTERN);
            final String fileName = FileUpload.getFileName(file.getOriginalFilename());
            final CloudinaryResponse response = cloudinaryService.uploadFile(file, fileName);
            student.setAvatar(response.getPublicId());
        }
        student.setCreatedBy(adminCode);
        student.setEducationProgram(educationProgramRepository.findById(request.getEducationProgramId()).orElseThrow(() ->
                new AppUnCheckedException("Education Program not found", HttpStatus.NOT_FOUND)
        ));

        Semester semester = semesterRepository.findById(request.getSemester()).orElseThrow(() ->
                new AppUnCheckedException("Semester not found", HttpStatus.NOT_FOUND)
        );
        student.setSemester(semester);

        Year year = yearRepository.findById(request.getYear()).orElseThrow(() ->
                new AppUnCheckedException("Year not found", HttpStatus.NOT_FOUND)
        );
        student.setYear(year);

        Major major = majorRepository.findById(request.getMajorId()).orElseThrow(() ->
                new AppUnCheckedException("Major not found", HttpStatus.NOT_FOUND)
        );
        student.setMajor(major);
        return studentMapper.toDTO(studentRepository.save(student));
    }

    @Override
    public StudentDTO updateStudentByStudent(StudentDTO request, MultipartFile file) {
        Student student = identifyUserAccessService.getStudent();
        student.setUpdatedBy(student.getCode());
        studentMapper.updateStudent(student, request);

        if (file != null && !file.isEmpty()) {
            FileUpload.assertAllowed(file, FileUpload.IMAGE_PATTERN);
            final String fileName = FileUpload.getFileName(file.getOriginalFilename());
            final CloudinaryResponse response = cloudinaryService.uploadFile(file, fileName);
            student.setAvatar(response.getPublicId());
        }

        return studentMapper.toDTO(studentRepository.save(student));
    }


    @Override
    public StudentDTO updateStudentByAdmin(Integer studentId, StudentDTO request, MultipartFile file) {
        if (studentRepository.existsByCode(request.getCode())) {
            throw new AppUnCheckedException("Code existed", HttpStatus.CONFLICT);
        }
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new AppUnCheckedException("Email existed", HttpStatus.CONFLICT);
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(()-> new RuntimeException("Student not found"));

        studentMapper.updateStudent(student,request);

        if(file!=null||!file.isEmpty()){
            FileUpload.assertAllowed(file, FileUpload.IMAGE_PATTERN);
            final String fileName= FileUpload.getFileName(file.getOriginalFilename());
            final CloudinaryResponse response = cloudinaryService.uploadFile(file, fileName);
            student.setAvatar(response.getPublicId());
        }
        String adminCode = identifyUserAccessService.getAdmin().getCode();
        student.setUpdatedBy(adminCode);

        return studentMapper.toDTO(studentRepository.save(student));
    }


    @Override
    public StudentDTO deleteStudentById(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentId));

        if (student.getAvatar() != null) {
            cloudinaryService.deleteFile(student.getAvatar());
        }

        studentRepository.delete(student);
        return studentMapper.toDTO(student);
    }

    @Override
    public void uploadStudentImages(String folderPath, List<Student> students) {
    }

    @Override
    public List<StudentDTO> getAllStudentByCourseAndMajor(String course, Integer majorId) {
        List<Student> students = studentRepository.findAll();
        System.out.println("Total students found: " + students.size());

        return students.stream()
                .filter(student ->
                        (course == null || student.getCourse().equalsIgnoreCase(course)) &&
                                (majorId == null || (student.getMajor() != null && student.getMajor().getId().equals(majorId)))
                )
                .map(studentMapper::toDTO)
                .toList();
    }

    public List<StudentDTO> importExcelFile(MultipartFile file) {
        List<StudentDTO> studentsDTO = new ArrayList<>();

        try (InputStream is = file.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Student student = new Student();
                    
                    String studentCode;
                    if (row.getCell(1).getCellType() == CellType.STRING) {
                        studentCode = row.getCell(1).getStringCellValue();
                    } else if (row.getCell(1).getCellType() == CellType.NUMERIC) {
                        studentCode = String.valueOf((int) row.getCell(1).getNumericCellValue());
                    } else {
                        throw new RuntimeException("Invalid code format in row " + (i + 1));
                    }

                    Optional<Student> existingStudent = studentRepository.findByCode(studentCode);
                    if (existingStudent.isPresent()) {
                        System.out.println("Student existed: " + studentCode);
                        continue;
                    }
                    student.setCode(studentCode);

                    student.setFirstName(getCellStringValue(row.getCell(2)));
                    student.setLastName(getCellStringValue(row.getCell(3)));

                    if (row.getCell(4).getCellType() == CellType.NUMERIC) {
                        student.setBirthday(row.getCell(4).getLocalDateTimeCellValue().toLocalDate());
                    } else {
                        student.setBirthday(LocalDate.now());
                    }

                    String genderStr = getCellStringValue(row.getCell(5));
                    if ("Nam".equalsIgnoreCase(genderStr)) {
                        student.setGender(true);
                    } else if ("Nữ".equalsIgnoreCase(genderStr)) {
                        student.setGender(false);
                    } else {
                        throw new RuntimeException("Gender not existed:: " + genderStr);
                    }

                    student.setAddress(getCellStringValue(row.getCell(6)));
                    student.setEmail(getCellStringValue(row.getCell(7)));
                    student.setPhone(getCellStringValue(row.getCell(8)));
                    student.setDescription(getCellStringValue(row.getCell(9)));
                    student.setAvatar(getCellStringValue(row.getCell(10)));

                    if (row.getCell(11).getCellType() == CellType.NUMERIC) {
                        student.setCourse(String.valueOf((int) row.getCell(11).getNumericCellValue()));
                    } else {
                        student.setCourse(getCellStringValue(row.getCell(11)));
                    }

                    String majorName = getCellStringValue(row.getCell(12));
                    Major major = majorRepository.findByName(majorName);
                    if (major != null) {
                        student.setMajor(major);
                    } else {
                        throw new RuntimeException("Major not existed:: " + majorName);
                    }

                    String semesterValue = getCellStringValue(row.getCell(13));
                    Semester semester = semesterRepository.findById(semesterValue)
                            .orElseThrow(() -> new RuntimeException("Semester not existed:: " + semesterValue));
                    student.setSemester(semester);

                    if (row.getCell(14).getCellType() == CellType.NUMERIC) {
                        Integer yearValue = (int) row.getCell(14).getNumericCellValue();
                        Year year = yearRepository.findByYear(yearValue)
                                .orElseThrow(() -> new RuntimeException("Year not existed: " + yearValue));
                        student.setYear(year);
                    } else {
                        throw new RuntimeException("Year not existed: " + getCellStringValue(row.getCell(1)));
                    }

                    String educationProgramName = getCellStringValue(row.getCell(15));
                    EducationProgram educationProgram = educationProgramRepository.findByName(educationProgramName);
                    if (educationProgram != null) {
                        student.setEducationProgram(educationProgram);
                    } else {
                        throw new RuntimeException("EducationProgram not existed: " + educationProgramName);
                    }

                    studentRepository.save(student);
                    studentsDTO.add(studentMapper.toDTO(student));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error import Excel: " + e.getMessage());
        }

        return studentsDTO;
    }

    @Override
    public List<Map<String, Object>> getStudentsByClazzId(Integer clazzId) {
        Integer instructorId = identifyUserAccessService.getInstructor().getId();
        return studentRepository.findStudentByClazzId(clazzId, instructorId);
    }




    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((int) cell.getNumericCellValue());
        }
        return "";
    }

}
