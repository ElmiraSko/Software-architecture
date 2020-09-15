package ru.erasko.rest;

public class ReportBuilder {
    private final Report report;

    public ReportBuilder(Report report) {
        this.report = report;
    }

    public ReportBuilder setUserName(String userName) {
        report.setUserName(userName);
        return this;

    }
    public ReportBuilder setUserRole(String userRole) {
        report.setUserRole(userRole);
        return this;

    }

    public ReportBuilder setAccountNumber(String accountNumber) {
        report.setAccountNumber(accountNumber);
        return this;
    }

    public ReportBuilder setTotalSum(String totalSum) {
        report.setTotalSum(totalSum);
        return this;
    }

    public ReportBuilder setTransactionSum(String transactionSum) {
        report.setTransactionSum(transactionSum);
        return this;
    }

    public ReportBuilder setAction(String action) {
        report.setAction(action);
        return this;
    }

    public Report build() {
        return report;
    }
}
