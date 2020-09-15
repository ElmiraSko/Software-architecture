package ru.erasko.rest;

public class Report {
    private String userName;
    private String userRole;
    private String accountNumber;
    private String totalSum;
    private String transactionSum;
    private String action;
    private String date;
    private final StringBuilder reportMessage = new StringBuilder("Report:  ");

    public Report() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        reportMessage.append(" UserName -  ");
        reportMessage.append(userName);
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
        reportMessage.append(", userRole -  ");
        reportMessage.append(userRole);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        reportMessage.append(", accountNumber -  ");
        reportMessage.append(accountNumber);
    }

    public String getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(String totalSum) {
        this.totalSum = totalSum;
        reportMessage.append(", totalSum -  ");
        reportMessage.append(totalSum);
    }

    public String getTransactionSum() {
        return transactionSum;
    }

    public void setTransactionSum(String transactionSum) {
        this.transactionSum = transactionSum;
        reportMessage.append(", transactionSum -  ");
        reportMessage.append(transactionSum);
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
        reportMessage.append(", action -  ");
        reportMessage.append(action);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        reportMessage.append(", date -  ");
        reportMessage.append(date);
    }

    public StringBuilder getReportMessage() {
        return reportMessage;
    }
}
