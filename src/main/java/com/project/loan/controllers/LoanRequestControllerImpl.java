package com.project.loan.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.project.loan.dto.ChangeLoanStatusDTO;
import com.project.loan.dto.CreateLoanRequestDTO;
import com.project.loan.models.LoanRequest;
import com.project.loan.models.LoanStatus;
import com.project.loan.services.LoanRequestService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
public class LoanRequestControllerImpl implements LoanRequestController {


    @Autowired
    private LoanRequestService loanRequestService;

    @Override
    public ResponseEntity<List<LoanRequest>> getAllLoanRequests(LoanStatus status, Long userId, String currency) {
        log.info("[GET] getAllLoanRequests called with status={}, userId={}, currency={}", status, userId, currency);
        List<LoanRequest> loanRequests = loanRequestService.getAllLoanRequests(status, userId, currency);
        return ResponseEntity.ok(loanRequests);
    }

    @Override
    public ResponseEntity<LoanRequest> getLoanRequestById(Long id) {
        log.info("[GET] getLoanRequestById called with id={}", id);
        return loanRequestService.getLoanRequestById(id)
                .map(loanRequest -> {                    
                    return ResponseEntity.ok(loanRequest);
                })
                .orElseGet(() -> {                    
                    return ResponseEntity.notFound().build();
                });
    }

    @Override
    public ResponseEntity<LoanRequest> createLoanRequest(@Valid CreateLoanRequestDTO createLoanRequestDTO) {
        log.info("[POST] createLoanRequest called with DTO: {}", createLoanRequestDTO);
        try {
            LoanRequest savedLoanRequest = loanRequestService.createLoanRequest(createLoanRequestDTO);
            log.info("[POST] LoanRequest created with id={}", savedLoanRequest.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedLoanRequest);
        } catch (RuntimeException e) {
            log.error("[POST] Error creating LoanRequest: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<LoanRequest> updateLoanRequestStatus(Long id, ChangeLoanStatusDTO changeLoanStatusDTO) {
        log.info("[PATCH] updateLoanRequestStatus called with id={}, DTO: {}", id, changeLoanStatusDTO);
        try {
            return loanRequestService.updateLoanRequestStatus(id, changeLoanStatusDTO)
                    .map(loanRequest -> {
                        log.info("[PATCH] LoanRequest status updated for id={}", id);
                        return ResponseEntity.ok(loanRequest);
                    })
                    .orElseGet(() -> {
                        log.warn("[PATCH] LoanRequest not found for id={}", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (RuntimeException e) {
            log.error("[PATCH] Error updating LoanRequest status for id={}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
}