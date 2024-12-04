package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.SubjectDTO;
import com.fpoly.backend.entities.Event;
import com.fpoly.backend.entities.Specialization;
import com.fpoly.backend.entities.Subject;
import com.fpoly.backend.mapper.SubjectMapper;
import com.fpoly.backend.repository.SpecializationRepository;
import com.fpoly.backend.repository.SubjectRepository;
import com.fpoly.backend.services.IdentifyUserAccessService;
import com.fpoly.backend.services.SubjectService;
import com.fpoly.backend.until.ExcelUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    private SubjectMapper subjectMapper;
    @Autowired
    private SpecializationRepository specializationRepository;
    @Autowired
    private IdentifyUserAccessService identifyUserAccessService;
    @Autowired
    private ExcelUtility excelUtility;

    @Override
    public Subject findById(Integer id) {
        return subjectRepository.findById(id).orElse(null);
    }

    @Override
    public List<Map<String, Object>> getAllSubjectBySpecializationId(Integer specializationId) {
        return subjectRepository.getAllSubjectBySpecializationId(specializationId);
    }

    @Override
    public List<Map<String, Object>> findAllSubject() {
        return subjectRepository.findAllSubject();
    }

    @Override
    public List<Map<String, Object>> getSubjectDetail(Integer subjectId) {
        return subjectRepository.getSubjectDetailById(subjectId);
    }

    @Override
    public List<Map<String, Object>> findAllSubjectByEducationProgramId(Integer educationProgramId) {
        return subjectRepository.findAllSubjectByEducationProgramId(educationProgramId);
    }

    @Override
    public SubjectDTO create(SubjectDTO request) {
        Subject subject = subjectMapper.toEntity(request);

        if(request.getRequiredId() != 0){
            Subject subjectRequired = subjectRepository.findById(request.getRequiredId()).orElseThrow(()->
                    new RuntimeException("Môn học này không tồn tại"));
            subject.setRequired(subjectRequired);
        }

        Specialization specialization = specializationRepository.findById(request.getSpecializationId()).orElseThrow(()->
                new RuntimeException("Bộ môn này không tồn tại"));

        subject.setSpecialization(specialization);
        subject.setId(null);
        subject.setStatus(true);
        subject.setOffline(true);
        subject.setCreatedBy(identifyUserAccessService.getAdmin().getCode());

        return subjectMapper.toDTO(subjectRepository.save(subject));
    }

    @Override
    public SubjectDTO update(SubjectDTO request, Integer subjectId) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(()->
                new RuntimeException("Môn học này không tồn tại"));

        subjectMapper.updateSubject(subject, request);

        if(request.getRequiredId() != 0){
            Subject subjectRequired = subjectRepository.findById(request.getRequiredId()).orElseThrow(()->
                    new RuntimeException("Môn học này không tồn tại"));
            subject.setRequired(subjectRequired);
        }

        subject.setUpdatedAt(new Date());
        subject.setStatus(true);
        subject.setOffline(true);
        subject.setUpdatedBy(identifyUserAccessService.getAdmin().getCode());

        return subjectMapper.toDTO(subjectRepository.save(subject));
    }

    @Override
    public void delete(Integer subjectId) {
        subjectRepository.deleteById(subjectId);
    }

    @Override
    public void importSubject(MultipartFile file) {
        try {
            List<Subject> subjectList = excelUtility.excelToSubjectList(file.getInputStream());
            subjectRepository.saveAll(subjectList);
        } catch (IOException ex) {
            throw new RuntimeException("Excel data is failed to store: " + ex.getMessage());
        }
    }
}
