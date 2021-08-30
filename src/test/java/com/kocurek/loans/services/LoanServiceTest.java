package com.kocurek.loans.services;


import com.kocurek.loans.domain.Loan;
import com.kocurek.loans.domain.User;
import com.kocurek.loans.repository.LoanRepository;
import com.kocurek.loans.repository.UserRepository;
import com.kocurek.loans.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoanService loanService;

    private Loan loan;
    private User user;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        loanService = new LoanService(loanRepository, userRepository);
        LocalDateTime timeNow = LocalDateTime.of(2021, 8, 29, 19, 23);
        LocalDate repaymentDate = LocalDate.of(2021, 10,10);
        BigDecimal amount = BigDecimal.valueOf(233);
        user = new User(1L, "Login", "Name", "LastName", "Password", new HashSet<>());
        loan = new Loan(1L, timeNow,repaymentDate, amount, user);
    }

    @Test
    public void saveLoanTest() {
        when(loanRepository.save(loan)).thenReturn(loan);
        Loan loanExpected = loanService.saveLoan(loan);
        verify(loanRepository, times(1)).save(loan);
        assertNotNull(loanExpected);
    }

    @Test
    public void findLoanByLoanId_Test() {
        Long id = 1L;
        when(loanRepository.findById(id)).thenReturn(Optional.ofNullable(loan));
        Loan expectedLoan = loanService.findLoanByLoanId(id);
        assertEquals(id,expectedLoan.getId());
        verify(loanRepository, times(1)).findById(id);
    }

    @Test
    public void findLoansToPayByUserLogin_Test() {
        List<Loan> loanList = new ArrayList<>();
        loanList.add(loan);
        loanList.add(new Loan(2L, LocalDateTime.of(2021, 8, 30, 10, 00),
                LocalDate.of(2021, 11, 11), BigDecimal.valueOf(301), user));
        when(loanRepository.findByUserIdAndRepaymentDateIsAfter(user.getLogin(), LocalDate.now())).thenReturn(loanList);
        List<Loan> expectedList = loanService.findLoansToPayByUserLogin("Login", LocalDate.now());
        verify(loanRepository, times(1)).findByUserIdAndRepaymentDateIsAfter(user.getLogin(), LocalDate.now());
        assertEquals(2, expectedList.size());
    }

    @Test
    public void extendTheLoan_Test() {
        Loan expectedLoan =
                new Loan(loan.getId(), loan.getActualTime(), loan.getRepaymentDate().plusDays(14), loan.getLoanSum(), loan.getUser());
        when(loanRepository.save(loan)).thenReturn(expectedLoan);
        Loan extendedLoan = loanService.extendTheLoan(loan);
        assertEquals(expectedLoan.getRepaymentDate(), extendedLoan.getRepaymentDate());
    }
}
