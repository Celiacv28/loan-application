package com.project.loan.controllers;

import com.project.loan.models.LoanRequest;
import com.project.loan.models.LoanStatus;
import com.project.loan.dto.CreateLoanRequestDTO;
import com.project.loan.services.LoanRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
public class LoanRequestControllerImpl implements LoanRequestController {

    @Autowired
    private LoanRequestService loanRequestService;

    @Override
    public ResponseEntity<List<LoanRequest>> getAllLoanRequests(LoanStatus status, Long userId, String currency) {
        List<LoanRequest> loanRequests = loanRequestService.getAllLoanRequests(status, userId, currency);
        return ResponseEntity.ok(loanRequests);
    }

    @Override
    public ResponseEntity<LoanRequest> getLoanRequestById(Long id) {
        return loanRequestService.getLoanRequestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<LoanRequest> createLoanRequest(@Valid CreateLoanRequestDTO createLoanRequestDTO) {
        try {
            LoanRequest savedLoanRequest = loanRequestService.createLoanRequest(createLoanRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedLoanRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<LoanRequest> updateLoanRequestStatus(Long id, LoanStatus newStatus) {
        try {
            return loanRequestService.updateLoanRequestStatus(id, newStatus)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}