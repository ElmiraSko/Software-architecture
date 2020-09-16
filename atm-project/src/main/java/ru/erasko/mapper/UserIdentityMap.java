package ru.erasko.mapper;

import org.springframework.stereotype.Component;
import ru.erasko.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserIdentityMap {

    private final Map<Long, User> userHash = new HashMap<>();

    public UserIdentityMap() {
    }

    public User isContains(long id) {

        return userHash.get(id);
    }

    public void add(User user) {
        userHash.put(user.getId(), user);
    }
}
