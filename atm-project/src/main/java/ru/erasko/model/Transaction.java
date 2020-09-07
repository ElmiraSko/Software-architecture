package ru.erasko.model;

public class Transaction {

    private int transactionSum;
    private String transactionType;
    private boolean status;

    public Transaction() {
    }

    public Transaction(int transactionSum, String transactionType, boolean status) {
        this.transactionSum = transactionSum;
        this.transactionType = transactionType;
        this.status = status;
    }

    public int getTransactionSum() {
        return transactionSum;
    }

    public void setTransactionSum(int transactionSum) {
        this.transactionSum = transactionSum;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
