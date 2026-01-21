package com.project.loan.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.loan.models.LoanRequest;
import com.project.loan.models.LoanStatus;

@Repository
public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {
    

    @Query("SELECT lr FROM LoanRequest lr WHERE (:status IS NULL OR lr.status = :status) AND (:clientId IS NULL OR lr.client.id = :clientId) AND (:currency IS NULL OR lr.currency = :currency)")
    List<LoanRequest> findByFilters(@Param("status") LoanStatus status,
                                    @Param("clientId") Long clientId,
                                    @Param("currency") String currency);   
}
