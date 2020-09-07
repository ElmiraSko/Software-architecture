package ru.erasko.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.erasko.model.Account;
import ru.erasko.model.User;
import ru.erasko.repository.AccountRepository;
import ru.erasko.repository.RoleRepository;
import ru.erasko.rest.NotFoundException;
import ru.erasko.service.UserService;

import java.util.Optional;


@RequestMapping("/user")
@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public UserController(UserService userService, RoleRepository roleRepository, AccountRepository accountRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.accountRepository = accountRepository;
    }

    @GetMapping
    public String userList(Model model) {
        logger.info("User list");
        model.addAttribute("users", userService.findAll());
        model.addAttribute("account", accountRepository.findAll());
        return "users";
    }

    @GetMapping("new")
    public String createUser(Model model) {
        logger.info("Create user form");
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("account", accountRepository.findAll());
        return "user-form";
    }

    @PostMapping("save")
    public String saveUser(User user) {

        String number = user.getAccount().getAccountNumber();
        logger.info("save = " + user.toString());
        logger.info("save = " + number);
        Optional<Account> saveAccount = Optional.of(accountRepository.findByAccountNumber(number)
                .orElse(new Account(null, number, 20000, user)));

        userService.save(user);
        if (saveAccount.get().getUser() == null) {
            accountRepository.save(saveAccount.get());
        }

        return "redirect:/user";
    }

    @GetMapping("edit")
    public String createUser(@RequestParam("id") Long id, Model model) {
        logger.info("Edit user width id {} ", id);

        model.addAttribute("user", userService.findById(id)
                .orElseThrow(() ->new NotFoundException("Not found user by Id")));
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("account", accountRepository.findAll());
        return "user-form";
    }

    @DeleteMapping
    public String delete(@RequestParam("id") long id) {
        logger.info("Delete user width id {} ", id);

        userService.delete(id);
        return "redirect:/user";
    }

}
