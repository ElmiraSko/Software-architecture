package ru.erasko.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.erasko.model.Account;
import ru.erasko.rest.NotFoundException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountMapper {

    private Connection connection;
    private final AccountIdentityMap accountIdentityMap;

    public AccountMapper(@Autowired DataSource dataSource,
                         AccountIdentityMap accountIdentityMap) {
        this.accountIdentityMap = accountIdentityMap;
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }

    public Account getAccountById(long id) throws Exception {
        Account account = accountIdentityMap.isContains(id);
        if(account != null)
        {
            return account;
        }
        else {
            Account account1 = findById(id);
            accountIdentityMap.add(account1);
            return account1;
        }
    }

    public Account findById(Long id) throws SQLException, NotFoundException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT id, account_number, sum FROM accounts WHERE id = ?");
        statement.setLong(1, id);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Account account = new Account();
                account.setId(resultSet.getLong(1));
                account.setAccountNumber(resultSet.getString(2));
                account.setSum(resultSet.getInt(3));
                return account;
            }
        }
        throw new NotFoundException("Not found account");
    }

    public Account findByNumber(String account_number) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT id, account_number, sum FROM accounts WHERE account_number = ?");
        statement.setString(1, account_number);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Account account = new Account();
                account.setId(resultSet.getLong(1));
                account.setAccountNumber(resultSet.getString(2));
                account.setSum(resultSet.getInt(3));
                return account;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return  null;
    }

    public List<Account> findAll() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        Statement statement = connection.createStatement();

        try (ResultSet resultSet = statement.executeQuery("SELECT id, account_number, sum FROM accounts")) {
            while (resultSet.next()) {
                Account account = new Account();
                account.setId(resultSet.getLong(1));
                account.setAccountNumber(resultSet.getString(2));
                account.setSum(resultSet.getInt(3));
                accounts.add(account);
            }
            return accounts;
        }
    }


    public void insert(Account account) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "INSERT INTO accounts(account_number, sum) VALUE(?, ?)");

            statement.setString(1, account.getAccountNumber());
            statement.setInt(2, account.getSum());
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(Account account) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "UPDATE accounts SET account_number=?, sum=? WHERE id = ?");

            statement.setString(1, account.getAccountNumber());
            statement.setInt(2, account.getSum());
            statement.setLong(3, account.getId());
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(Long id) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "DELETE FROM accounts WHERE id = ?");
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
