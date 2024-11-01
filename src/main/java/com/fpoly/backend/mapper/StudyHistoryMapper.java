package com.fpoly.backend.mapper;

import com.fpoly.backend.dto.StudyHistoryDTO;
import com.fpoly.backend.entities.StudyHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class StudyHistoryMapper {

    @Mapping(source = "block.block", target = "block")
    @Mapping(source = "semester.semester", target = "semester")
    @Mapping(source = "year.year", target = "year")
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "subject.id", target = "subjectId")
    public abstract StudyHistoryDTO toDTO (StudyHistory studyHistory);

    @Mapping(target = "block", ignore = true)
    @Mapping(target = "semester", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "student",ignore = true)
    @Mapping(target = "subject", ignore = true)
    public abstract StudyHistory toEntity(StudyHistoryDTO studyHistoryDTO);

    @Mapping(target = "block", ignore = true)
    @Mapping(target = "semester", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "subject", ignore = true)
    public abstract void updateStudyHistory(@MappingTarget StudyHistory studyHistory, StudyHistoryDTO request);

}
