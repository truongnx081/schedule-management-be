package com.fpoly.backend.services;

import com.fpoly.backend.dto.StudentDTO;
import com.fpoly.backend.entities.Student;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface StudentService {

    Student findById(Integer id);

    public StudentDTO getStudentInfor();

    public StudentDTO getStudentById(Integer studentId);

    public StudentDTO createStudent(StudentDTO request, MultipartFile file);


    public StudentDTO updateStudentByStudent(StudentDTO request);

    StudentDTO updateStudentByAdmin(Integer studentId, StudentDTO request, MultipartFile file);

    public StudentDTO deleteStudentById(Integer studentId);

    public void uploadStudentImages(String folderPath, List<Student> students);

    public List<StudentDTO> getAllStudentByCourseAndMajor( String course, Integer majorId);

    public List<StudentDTO> importExcelFile(MultipartFile file);
}
