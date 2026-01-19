package com.project.loan.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.loan.dto.ChangeLoanStatusDTO;
import com.project.loan.dto.CreateLoanRequestDTO;
import com.project.loan.mappers.LoanRequestMapper;
import com.project.loan.models.LoanRequest;
import com.project.loan.models.LoanStatus;
import com.project.loan.models.User;
import com.project.loan.repo.LoanRequestRepository;
import com.project.loan.repo.UserRepository;



@Service
public class LoanRequestServiceImpl implements LoanRequestService {

    @Autowired
    private LoanRequestRepository loanRequestRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanRequestMapper loanRequestMapper;

    @Override
    public List<LoanRequest> getAllLoanRequests(LoanStatus status, Long userId, String currency) {
        User user = null;
        if (userId != null) {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        }
        return loanRequestRepository.findByFilters(status, user, currency);
    }

    @Override
    public Optional<LoanRequest> getLoanRequestById(Long id) {
        return loanRequestRepository.findById(id);
    }

    @Override
    public LoanRequest createLoanRequest(CreateLoanRequestDTO createLoanRequestDTO) {
        User user = userRepository.findById(createLoanRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        LoanRequest loanRequest = loanRequestMapper.toEntity(createLoanRequestDTO, user);
        loanRequest.setStatus(LoanStatus.PENDING);
        loanRequest.setCreatedAt(java.time.LocalDateTime.now());
        return loanRequestRepository.save(loanRequest);
    }

    @Override
    public Optional<LoanRequest> updateLoanRequestStatus(Long id, ChangeLoanStatusDTO newStatus) {
        return loanRequestRepository.findById(id)
                .map(loanRequest -> {
                    LoanStatus currentStatus = loanRequest.getStatus();
                    
                    if (!isValidStatusTransition(currentStatus, newStatus.getStatus())) {
                        throw new RuntimeException("Transición de estado no permitida: " + 
                                currentStatus + " -> " + newStatus.getStatus());
                    }

                    loanRequest.setStatus(newStatus.getStatus());
                    
                    return loanRequestRepository.save(loanRequest);
                });
    }

    private boolean isValidStatusTransition(LoanStatus current, LoanStatus newStatus) {
        switch (current) {
            case PENDING:
                return newStatus == LoanStatus.APPROVED || newStatus == LoanStatus.REJECTED;
            case APPROVED:
                return newStatus == LoanStatus.CANCELLED;
            case REJECTED:
            case CANCELLED:
                return false; 
            default:
                return false;
        }
    }
}