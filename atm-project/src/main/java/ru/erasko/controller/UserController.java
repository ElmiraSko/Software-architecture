package ru.erasko.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.erasko.mapper.AccountMapper;
import ru.erasko.mapper.RoleMapper;
import ru.erasko.mapper.UserMapper;
import ru.erasko.model.Account;
import ru.erasko.model.Role;
import ru.erasko.model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequestMapping("/user")
@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final AccountMapper accountMapper;

    @Autowired
    public UserController(UserMapper userMapper, RoleMapper roleMapper, AccountMapper accountMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.accountMapper = accountMapper;
    }

    @GetMapping
    public String userList(Model model) throws Exception {
        logger.info("User list");
        model.addAttribute("users", userMapper.findAll());
        model.addAttribute("account", accountMapper.findAll());
        return "users";
    }

    @GetMapping("new")
    public String createUser(Model model) throws SQLException {
        logger.info("Create user form");
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleMapper.findAll());
        model.addAttribute("account", accountMapper.findAll());
        return "user-form";
    }

    @PostMapping("save")
    public String saveUser(User user) throws SQLException {

        boolean isSave = true;
        String number = user.getAccount().getAccountNumber();
        List<Role> newRoles = user.getRoles();

        // проверка ролей
        List<Role> oldRoles = null;
        try {
            oldRoles = userMapper.getUserById(user.getId()).getRoles();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (oldRoles != null) {
            if (newRoles.size() < oldRoles.size()) {
                isSave = false;
            }

            Set<Role> roles = new HashSet<>(newRoles);
            for (Role role : oldRoles) {
                if (roles.contains(role)) {
                    roles.remove(role);
                } else {
                    roles.add(role);
                }
            }
            newRoles = new ArrayList<>(roles);
        }

        if (accountMapper.findByNumber(number) == null) {
            accountMapper.insert(new Account(-1L, number, 0, user));
            Long accId = accountMapper.findByNumber(number).getId();
            userMapper.insert(user, accId, newRoles);
        } else {
            Long accId = accountMapper.findByNumber(number).getId();
            userMapper.update(user, accId, newRoles, isSave);
        }
        return "redirect:/user";
    }

    @GetMapping("edit")
    public String createUser(@RequestParam("id") Long id, Model model) throws Exception {
        logger.info("Edit user width id {} ", id);

        model.addAttribute("user", userMapper.getUserById(id));
        model.addAttribute("roles", roleMapper.findAll());
        model.addAttribute("account", accountMapper.findAll());
        return "user-form";
    }

    @DeleteMapping
    public String delete(@RequestParam("id") long id) {
        logger.info("Delete user width id {} ", id);

        userMapper.delete(id);
        return "redirect:/user";
    }

}
