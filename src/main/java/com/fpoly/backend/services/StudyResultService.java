package com.fpoly.backend.services;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public interface StudyResultService {

    List<Map<String, Object>> getAllStudyResultByStudentId();
}
