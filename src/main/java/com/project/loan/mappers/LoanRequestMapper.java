package com.project.loan.mappers;

import com.project.loan.dto.CreateLoanRequestDTO;
import com.project.loan.models.LoanRequest;
import com.project.loan.models.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", source = "client")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    LoanRequest toEntity(CreateLoanRequestDTO dto, Client client);
}