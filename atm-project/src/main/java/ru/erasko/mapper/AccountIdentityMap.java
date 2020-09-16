package ru.erasko.mapper;

import org.springframework.stereotype.Component;
import ru.erasko.model.Account;

import java.util.HashMap;
import java.util.Map;

@Component
public class AccountIdentityMap {

    private final Map<Long, Account> accountHash = new HashMap<>();

    public AccountIdentityMap() {
    }

    public Account isContains(long id) {

        return accountHash.get(id);
    }

    public void add(Account account) {
        accountHash.put(account.getId(), account);
    }
}
