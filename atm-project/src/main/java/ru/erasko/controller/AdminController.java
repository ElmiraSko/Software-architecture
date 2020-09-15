package ru.erasko.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.erasko.model.User;
import ru.erasko.repository.AccountRepository;
import ru.erasko.repository.UserRepository;
import ru.erasko.rest.Report;
import ru.erasko.rest.ReportBuilder;

import java.util.Optional;

@Controller
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final UserRepository userRepository;

    @Autowired
    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        Optional<User> user = userRepository.findByName(getCurrentUsername());
        if (user.isPresent()) {
            User authUser = user.get();
            Report report = new ReportBuilder(new Report())
                    .setUserName(authUser.getName())
                    .setAccountNumber(authUser.getAccount().getAccountNumber())
                    .setUserRole(authUser.getRoles().toString())
                    .setTransactionSum(String.valueOf(authUser.getAccount().getActionSum()))
                    .build();
            logger.info(report.getReportMessage().toString());
            model.addAttribute("report", report);
        }
        return "admin";
    }
    private String getCurrentUsername () {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
