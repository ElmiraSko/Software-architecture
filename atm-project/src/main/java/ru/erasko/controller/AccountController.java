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
import ru.erasko.mapper.AccountMapper;
import ru.erasko.mapper.UserMapper;
import ru.erasko.model.Account;
import ru.erasko.model.User;

import java.sql.SQLException;

@RequestMapping("/money")
@Controller
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final AccountMapper accountMapper;
    private final UserMapper userMapper;

    @Autowired
    public AccountController(AccountMapper accountMapper, UserMapper userMapper) {
        this.accountMapper = accountMapper;
        this.userMapper = userMapper;
    }

    @GetMapping()
    public String getAccount(Model model) throws SQLException {
        // авторизованный пользователь
        User user = userMapper.findByName(getCurrentUsername());
        if (user != null) {
            String accNumber = user.getAccount().getAccountNumber();
            Account usAccount = accountMapper.findByNumber(accNumber);
            model.addAttribute("account", usAccount);
            }
        return "money";
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @PostMapping()
    public String depositing(Account account) throws Exception {
        logger.info("depositing = " + account.getId() + ", "
                + account.getActionSum() + ", " + account.getAction());
        int accSum = account.getActionSum();

        Account accountDb = accountMapper.getAccountById(account.getId());

        if (account.getAction().equals("d")) {
            accountDb.setSum(accountDb.getSum()+accSum);
        } else {
            if (accountDb.getSum() >= accSum)
            accountDb.setSum(accountDb.getSum()-accSum);
        }

        accountMapper.insert(accountDb);

        return "redirect:/money";
    }

}
