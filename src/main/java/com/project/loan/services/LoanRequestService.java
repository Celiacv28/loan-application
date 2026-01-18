package com.project.loan.services;

import com.project.loan.models.LoanRequest;
import com.project.loan.models.LoanStatus;
import com.project.loan.dto.CreateLoanRequestDTO;
import com.project.loan.dto.ChangeLoanStatusDTO;
import java.util.List;
import java.util.Optional;

public interface LoanRequestService {
    
    List<LoanRequest> getAllLoanRequests(LoanStatus status, Long userId, String currency);
    
    Optional<LoanRequest> getLoanRequestById(Long id);
    
    LoanRequest createLoanRequest(CreateLoanRequestDTO createLoanRequestDTO);
    
    Optional<LoanRequest> updateLoanRequestStatus(Long id, ChangeLoanStatusDTO changeLoanStatusDTO);
}