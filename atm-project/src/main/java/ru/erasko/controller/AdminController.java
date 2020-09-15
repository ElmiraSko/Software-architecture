package ru.erasko.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.erasko.forIterator.UserInfo;
import ru.erasko.mapper.RoleMapper;
import ru.erasko.mapper.UserMapper;
import ru.erasko.model.Role;
import ru.erasko.model.User;
import ru.erasko.repository.UserRepository;
import ru.erasko.rest.Report;
import ru.erasko.rest.ReportBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
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
            User authUser = getCurrentUser();
            Report report = new ReportBuilder(new Report())
                    .setUserName(authUser.getName())
                    .setAccountNumber(authUser.getAccount().getAccountNumber())
                    .setUserRole(authUser.getRoles().toString())
                    .setTransactionSum(String.valueOf(authUser.getAccount().getActionSum()))
                    .build();
            logger.info(report.getReportMessage().toString());
            model.addAttribute("report", report);
        return "admin";
    }
    private User getCurrentUser () {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByName(auth.getName());
        return user.orElse(null);
    }

    @PostMapping("/writeInfo")
    public String writeInfoToFile() throws IOException {
            User authUser = getCurrentUser();
            UserInfo.UserId userId = new UserInfo.UserId(String.valueOf(authUser.getId()));
            UserInfo.UserName userName = new UserInfo.UserName(authUser.getName());
            UserInfo.UserRole userRole = new UserInfo.UserRole(authUser.getRoles().toString());
            UserInfo.UserAccount userAccount = new UserInfo.UserAccount(authUser.getAccount().getAccountNumber());

            UserInfo userInfo = new UserInfo(userId, userName, userRole, userAccount);

            File file = new File("Example.txt");
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);

            Iterator userIterator = userInfo.iterator();

            while (userIterator.hasNext()) {
                UserInfo.UserInfoPart userPart = (UserInfo.UserInfoPart) userIterator.next();
                fileWriter.write(userPart.getPartInformationAboutUser() + "; \n");
                fileWriter.flush();
            }
            fileWriter.close();
        return "index";

    }
}
