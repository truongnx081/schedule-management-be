package com.fpoly.backend.services;

import com.fpoly.backend.dto.ClazzDTO;
import com.fpoly.backend.dto.StudyInDTO;
import com.fpoly.backend.entities.Clazz;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ClazzService {

    ClazzDTO create(ClazzDTO request);
    ClazzDTO update(ClazzDTO request, Integer id);
    void delete(Integer id);
    ClazzDTO getOne(Integer id);
    List<ClazzDTO> getAll();
    List<Map<String, Object>> findClazzByBlockAndSemesterAndYear();
    void importClazz(MultipartFile file);
}
