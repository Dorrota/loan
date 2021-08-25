package com.kocurek.loans.repository;

import com.kocurek.loans.domain.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("select l from Loan l where l.user.login = ?1 and l.repaymentDate >  ?2")
    List<Loan> findByUserIdAndRepaymentDateIsAfter(String userLogin, LocalDate now);
}
