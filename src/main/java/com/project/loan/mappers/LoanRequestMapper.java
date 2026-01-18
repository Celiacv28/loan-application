package com.project.loan.mappers;

import com.project.loan.dto.CreateLoanRequestDTO;
import com.project.loan.models.LoanRequest;
import com.project.loan.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    LoanRequest toEntity(CreateLoanRequestDTO dto, User user);
}