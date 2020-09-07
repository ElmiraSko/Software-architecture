package ru.erasko.model;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false)
    private String accountNumber;

    @Column(length = 64, nullable = false)
    private int sum;

    @OneToOne(mappedBy = "account", cascade=CascadeType.ALL)
    private User user;

    @Transient
    private int actionSum;
    @Transient
    private String action;

    public Account() {
    }

    public Account(Long id, String accountNumber, int sum, User user) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.sum = sum;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return accountNumber;
    }

    public int getActionSum() {
        return actionSum;
    }

    public void setActionSum(int actionSum) {
        this.actionSum = actionSum;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
