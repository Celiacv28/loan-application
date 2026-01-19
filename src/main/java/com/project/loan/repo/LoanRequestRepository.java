package com.project.loan.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.loan.models.LoanRequest;
import com.project.loan.models.LoanStatus;

@Repository
public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {
    

    @Query("SELECT lr FROM LoanRequest lr WHERE (:status IS NULL OR lr.status = :status) AND (:userId IS NULL OR lr.user.id = :userId) AND (:currency IS NULL OR lr.currency = :currency)")
    List<LoanRequest> findByFilters(@org.springframework.data.repository.query.Param("status") LoanStatus status,
                                    @org.springframework.data.repository.query.Param("userId") Long userId,
                                    @org.springframework.data.repository.query.Param("currency") String currency);   
}
