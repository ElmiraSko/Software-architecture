package ru.erasko.mapper;

import org.springframework.stereotype.Component;
import ru.erasko.model.Role;

import java.util.HashMap;
import java.util.Map;

@Component
public class RoleIdentityMap {
    private final Map<Long, Role> roleHash = new HashMap<>();

    public RoleIdentityMap() {
    }

    public Role isContains(long id) throws Exception {

        return roleHash.get(id);
    }

    public void add(Role role) {
        roleHash.put(role.getId(), role);
    }
}
