package org.example.Mapping;

import org.example.Model.TaskImportance;
import org.example.Priority;
import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ImportanceMapper {
    ImportanceMapper INSTANCE = Mappers.getMapper(ImportanceMapper.class);

    @ValueMapping(target = "Low", source = "Low")
    @ValueMapping(target = "Medium", source = "Medium")
    @ValueMapping(target = "High", source = "High")
    TaskImportance toApi(Priority priority);

    @ValueMapping(target = "Low", source = "Low")
    @ValueMapping(target = "Medium", source = "Medium")
    @ValueMapping(target = "High", source = "High")
    Priority toDomain(TaskImportance importance);
}
