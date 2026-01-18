package com.project.loan.mappers;

import com.project.loan.dto.CreateUserDTO;
import com.project.loan.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(CreateUserDTO dto);
}