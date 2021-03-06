package com.kocurek.loans.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime actualTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "The repayment date should be in future")
    private LocalDate repaymentDate;
    private BigDecimal loanSum;

    @ManyToOne
    private User user;
}
