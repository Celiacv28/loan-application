package com.project.loan.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.loan.models.LoanRequest;
import com.project.loan.models.LoanStatus;
import com.project.loan.models.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {
    
    List<LoanRequest> findByUser(User user);
    
    List<LoanRequest> findByStatus(LoanStatus status);
    
    List<LoanRequest> findByUserAndStatus(User user, LoanStatus status);
    
    List<LoanRequest> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<LoanRequest> findByAmountBetween(Double minAmount, Double maxAmount);
    
    List<LoanRequest> findByCurrency(String currency);
    
    Long countByStatus(LoanStatus status);
}
