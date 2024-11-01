package com.fpoly.backend.mapper;

import com.fpoly.backend.dto.MarkDTO;
import com.fpoly.backend.entities.Mark;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class MarkMapper {

    @Mapping(source = "studyHistory.id", target = "studyHistoryId")
    @Mapping(source = "markColumn.id", target = "markColumnId")
    public abstract MarkDTO toDTO (Mark mark);

    @Mapping(target = "studyHistory", ignore = true)
    @Mapping(target = "markColumn", ignore = true)
    public abstract Mark toEntity(MarkDTO markDTO);

    @Mapping(target = "studyHistory", ignore = true)
    @Mapping(target = "markColumn", ignore = true)
    public abstract void updateMark(@MappingTarget Mark mark, MarkDTO request);

}
