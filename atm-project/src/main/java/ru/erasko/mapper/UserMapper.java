package ru.erasko.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.erasko.model.User;
import ru.erasko.rest.NotFoundException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserMapper {

    private Connection connection;
    private AccountMapper accountMapper;

    public UserMapper(@Autowired DataSource dataSource, AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }

    public User findById(Long id) throws SQLException, NotFoundException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT id, name, password FROM users WHERE id = ?");
        statement.setLong(1, id);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                user.setPassword(resultSet.getString(3));
                return user;
            }
        }
        throw new NotFoundException("Not found user");
    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        Statement statement = connection.createStatement();

        try (ResultSet resultSet = statement.executeQuery("SELECT id, name, password, account_id FROM users")) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                user.setPassword(resultSet.getString(3));
                Long accountId = resultSet.getLong(4);
                user.setAccount(accountMapper.findById(accountId));
                users.add(user);
            }
            return users;
        }
    }

    public void insert(User user, Long accId) throws SQLException {

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "INSERT INTO users(name, password, account_id) VALUE(?, ?, ?)");

            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setLong(3, accId);
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(User user) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "UPDATE users SET name=?, password=?, account_id=? WHERE id=?");

            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setLong(3, user.getAccount().getId());
            statement.setLong(4, user.getId());
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
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
