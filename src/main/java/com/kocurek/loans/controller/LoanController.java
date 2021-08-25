package com.kocurek.loans.controller;

import com.kocurek.loans.domain.Loan;
import com.kocurek.loans.domain.User;
import com.kocurek.loans.service.LoanService;
import com.kocurek.loans.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/loan")
public class LoanController {

    private final LoanService loanService;
    private final UserService userService;
    @Value("${sb.min.hour.of.loan}")
    private int minHour;
    @Value("${sb.max.hour.of.loan}")
    private int maxHour;
    @Value("${sb.max.value.of.loan}")
    private int maxValueOfLoan;

    public LoanController(LoanService loanService, UserService userService) {
        this.loanService = loanService;
        this.userService = userService;

    }

    @GetMapping("/form")
    public String getLoanForm(Model model, Authentication authentication) {
        String login = authentication.getName();
        User user = userService.getUserByLogin(login);
        model.addAttribute("user", user);
        model.addAttribute("loan", new Loan());
        return "loanrequest";
    }
    @PostMapping("/form")
    public String saveLoanRequest(@ModelAttribute Loan loan, Authentication authentication, Model model) {
        String login = authentication.getName();
        User user = userService.getUserByLogin(login);
        model.addAttribute("user", user);
        System.out.println("llllllogin " + user.getLogin());
        LocalDateTime dateTimeNow = LocalDateTime.now();
        // Set<Loan> listOfLoans = user.getLoans();
        // List<Loan> filteredListOfLoans =  listOfLoans.stream()
        // .filter(l -> l.getRepaymentDate().isAfter(dateTime.toLocalDate())).collect(Collectors.toList());
        int hour = dateTimeNow.getHour();
        System.out.println("Hourrrr " + hour);
        if (loan.getActualTime()==null){
            loan.setActualTime(dateTimeNow);
        }

        int valueOfLoan = loan.getLoanSum().intValue();

        System.out.println("Value of loan " + valueOfLoan);

        if (hour <= minHour && hour >= maxHour || valueOfLoan > maxValueOfLoan) {
            return "loanrejected";
        } else {
            loan.setUser(user);
            loanService.saveLoan(loan);
        }
        return "redirect:/loan/loandetails";
    }
    @GetMapping("/loandetails")
    public String getLoanDetails(Authentication authentication, Model model) {
        //authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        System.out.println("Loginrrrrr " + login);
        User user = userService.getUserByLogin(login);
        model.addAttribute("user", user);
        model.addAttribute("loans", user.getLoans());
        LocalDate actualDate = LocalDate.now();
        List<Loan> loansToPay = loanService.findLoansToPayByUserLogin(login, actualDate);
        model.addAttribute("loanToPay", loansToPay);
        return "loandetails";
    }

    @PostMapping("/toextend")
    public String getLoanToExtend(
            @RequestParam(value = "toPayId") Long id, Model model) {
        Loan loan = loanService.findLoanByLoanId(id);
        model.addAttribute("loan", loan);
        System.out.println("Loan id " + loan.getId());
        loanService.extendTheLoan(loan);
        return "redirect:/loan/loandetails";
    }

   /* @PostMapping("/toextend")
    public String saveLoanExtension(@ModelAttribute Loan loan) {
        System.out.println("Post loan Id " + loan.getId());
        loanService.extendTheLoan(loan);
        loanService.saveLoan(loan);
        return "redirect:loan/loandetails";
    }*/
}
