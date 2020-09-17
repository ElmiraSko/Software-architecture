package ru.erasko.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.erasko.model.Role;
import ru.erasko.model.User;
import ru.erasko.rest.NotFoundException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserMapper {
    private static final Logger logger = LoggerFactory.getLogger(UserMapper.class);

    private Connection connection;
    private final AccountMapper accountMapper;
    private final RoleMapper roleMapper;
    private final UserIdentityMap userIdentityMap;

    public UserMapper(@Autowired DataSource dataSource,
                      AccountMapper accountMapper,
                      RoleMapper roleMapper, UserIdentityMap userIdentityMap) {
        this.accountMapper = accountMapper;
        this.roleMapper = roleMapper;
        this.userIdentityMap = userIdentityMap;
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }

    public User getUserById(long id) throws Exception {
        User user = userIdentityMap.isContains(id);
        if(user != null)
        {
            return user;
        }
        else {
            User user1 = findById(id);
            userIdentityMap.add(user1);
            return user1;
        }
    }

    private User findById(Long id) throws SQLException, NotFoundException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT id, name, password, account_id FROM users WHERE id = ?");
        statement.setLong(1, id);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = buildUser(resultSet);
                user.setRoles(getUserRolesById(id));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NotFoundException("Not found user");
    }

    private List<Role> getUserRolesById(Long id) {
        List<Role> roles = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT user_id, role_id FROM users_roles WHERE user_id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                roles.add(roleMapper.findById(resultSet.getLong(2)));
            }
            return roles;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        throw new NotFoundException("Not found role");
    }

    public User findByName(String name) throws SQLException, NotFoundException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT id, name, password, account_id FROM users WHERE name = ?");
        statement.setString(1, name);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                return buildUser(resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NotFoundException("Not found user");
    }

    public List<User> findAll() throws Exception {
        List<User> users = new ArrayList<>();
        Statement statement = connection.createStatement();

        try (ResultSet resultSet = statement.executeQuery(
                "SELECT id, name, password, account_id FROM users")) {
            while (resultSet.next()) {
                User user = buildUser(resultSet);
                users.add(user);
            }
            return users;
        }
    }
    private User buildUser(ResultSet resultSet) {
        User user = new User();
        try {
            user.setId(resultSet.getLong(1));
            user.setName(resultSet.getString(2));
            user.setPassword(resultSet.getString(3));
            user.setAccount(accountMapper.getAccountById(resultSet.getLong(4)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
            return user;
    }

    public void insert(User user, Long accId, List<Role> roles) {
        PreparedStatement statement = null;
        String newUserName = user.getName();
        try {
            statement = connection.prepareStatement(
                    "INSERT INTO users(name, password, account_id) VALUE(?, ?, ?)");

            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setLong(3, accId);
            statement.execute();

            Long newUserId = findByName(newUserName).getId();
            saveUserRoles(roles, newUserId);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(User user, Long accId, List<Role> roles, boolean isSave) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "UPDATE users SET name=?, password=?, account_id=? WHERE id=?");

            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setLong(3, accId); // выдает ошибку
            statement.setLong(4, user.getId());
            statement.execute();
            if (roles != null) {
                if (isSave) {
                    saveUserRoles(roles, user.getId());
                }
                deleteUserRoles(roles, user.getId());
            }
            userIdentityMap.add(findById(user.getId()));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void saveUserRoles(List<Role> roles, Long userId) {
        for (Role ro : roles) {
            Long roleId = ro.getId();
            try {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO users_roles(user_id, role_id)  VALUES(?, ?)");
                statement.setLong(1, userId);
                statement.setLong(2, roleId);
                statement.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void deleteUserRoles(List<Role> roles, Long userId) {
        for (Role ro : roles) {
            Long roleId = ro.getId();
            try {
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM users_roles WHERE user_id=? AND role_id=?");
                statement.setLong(1, userId);
                statement.setLong(2, roleId);
                statement.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void delete(Long id) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "DELETE FROM users_roles WHERE user_id = ?");
            statement.setLong(1, id);
            statement.execute();
            statement = connection.prepareStatement(
                    "DELETE FROM users WHERE id = ?");
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }




}
