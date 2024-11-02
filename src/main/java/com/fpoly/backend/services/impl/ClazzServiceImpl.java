package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.StudyInDTO;
import com.fpoly.backend.entities.Clazz;
import com.fpoly.backend.entities.Student;
import com.fpoly.backend.entities.StudyIn;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.mapper.StudyInMapper;
import com.fpoly.backend.repository.ClazzRepository;
import com.fpoly.backend.repository.StudyInRepository;
import com.fpoly.backend.services.ClazzService;
import com.fpoly.backend.services.IdentifyUserAccessService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class ClazzServiceImpl implements ClazzService {}
