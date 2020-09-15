package ru.erasko.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.erasko.model.Account;
import ru.erasko.model.Role;
import ru.erasko.rest.NotFoundException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoleMapper {

    private Connection connection;

    public RoleMapper(@Autowired DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }

    public Role findById(Long id) throws SQLException, NotFoundException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT id, name FROM roles WHERE id = ?");
        statement.setLong(1, id);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Role role = new Role();
                role.setId(resultSet.getLong(1));
                role.setName(resultSet.getString(2));
                return role;
            }
        }
        throw new NotFoundException("Not found role");
    }

    public List<Role> findAll() throws SQLException {
        List<Role> roles = new ArrayList<>();
        Statement statement = connection.createStatement();

        try (ResultSet resultSet = statement.executeQuery("SELECT id, name FROM roles")) {
            while (resultSet.next()) {
                Role role = new Role();
                role.setId(resultSet.getLong(1));
                role.setName(resultSet.getString(2));
                roles.add(role);
            }
            return roles;
        }
    }

    public void insert(Role role) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "INSERT INTO roles VALUE(?)");

            statement.setString(1, role.getName());
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(Role role) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "UPDATE roles SET name=? WHERE id = ?");

            statement.setString(1, role.getName());
            statement.setLong(2, role.getId());
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(Long id) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "DELETE FROM roles WHERE id = ?");
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
