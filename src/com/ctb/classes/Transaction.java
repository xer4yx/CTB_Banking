package com.ctb.classes;

import com.ctb.interfaces.TransactionInterface;

import java.time.Instant;

public class Transaction implements TransactionInterface {
    private String transactionID;
    private String transactionType;
    private String description;
    private double amount;
    private final long timeStamp = Instant.now().getEpochSecond();

    @Override
    public void generateTransactionID() {

    }

    @Override
    public boolean depositFunds(String username, double amount) {
        return false;
    }

    @Override
    public boolean withdrawFunds(String username, double amount) {
        return false;
    }

    @Override
    public boolean makePurchase(String username, double amount, String purchaseDescription) {
        return false;
    }

    @Override
    public boolean payBills(String username, double amount, String billDescription) {
        return false;
    }
}
