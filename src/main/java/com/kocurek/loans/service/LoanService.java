package com.kocurek.loans.service;

import com.kocurek.loans.domain.Loan;
import com.kocurek.loans.repository.LoanRepository;
import com.kocurek.loans.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    private final LoanRepository repository;
    private final UserRepository userRepository;

    public LoanService(LoanRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public Loan saveLoan(Loan loan) {
        return repository.save(loan);
    }

    public List<Loan> findLoansToPayByUserLogin(String login, LocalDate actualTime) {
        return repository.findByUserIdAndRepaymentDateIsAfter(login, actualTime);
    }

    public Loan extendTheLoan(Loan loan) {
        LocalDate repDate = loan.getRepaymentDate();
        loan.setRepaymentDate(repDate.plusDays(14));
        repository.save(loan);
        return loan;
    }

    public Loan findLoanByLoanId(Long id) throws EntityNotFoundException {
        Loan loan = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        return loan;
    }
}
