package org.example.Mapping;

import org.example.Model.TaskStatus;
import org.example.Status;
import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StatusMapper {
    StatusMapper INSTANCE = Mappers.getMapper(StatusMapper.class);

    @ValueMapping(target = "CREATED", source = "CREATED")
    @ValueMapping(target = "IN_PROGRESS", source = "IN_PROGRESS")
    @ValueMapping(target = "DONE", source = "DONE")
    TaskStatus toApi(Status priority);

    @ValueMapping(target = "CREATED", source = "CREATED")
    @ValueMapping(target = "IN_PROGRESS", source = "IN_PROGRESS")
    @ValueMapping(target = "DONE", source = "DONE")
    Status toDomain(TaskStatus priority);
}
