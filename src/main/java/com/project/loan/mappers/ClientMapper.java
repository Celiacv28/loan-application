package com.project.loan.mappers;

import com.project.loan.dto.CreateClientDTO;
import com.project.loan.models.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Client toEntity(CreateClientDTO dto);
}