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
import ru.erasko.model.Account;
import ru.erasko.model.User;
import ru.erasko.repository.AccountRepository;
import ru.erasko.repository.UserRepository;

import java.util.Optional;

@RequestMapping("/money")
@Controller
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Autowired
    public AccountController(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @GetMapping()
    public String getAccount(Model model) {
        // авторизованный пользователь
        Optional<User> user = userRepository.findByName(getCurrentUsername());
        if (user.isPresent()) {
            String accNumber = user.get().getAccount().getAccountNumber();
            Account usAccount = accountRepository.findByAccountNumber(accNumber).get();
            model.addAttribute("account", usAccount);
        }
        return "money";
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @PostMapping()
    public String depositing(Account account) {
        logger.info("depositing = " + account.getId() + ", "
                + account.getActionSum() + ", " + account.getAction());
        int accSum = account.getActionSum();

        Account accountDb = accountRepository.findById(account.getId()).get();

        if (account.getAction().equals("d")) {
            accountDb.setSum(accountDb.getSum()+accSum);
        } else {
            if (accountDb.getSum() >= accSum)
            accountDb.setSum(accountDb.getSum()-accSum);
        }

        accountRepository.save(accountDb);
        return "redirect:/money";
    }

}
