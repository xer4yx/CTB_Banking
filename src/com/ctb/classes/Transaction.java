package com.ctb.classes;

import com.ctb.interfaces.TransactionInterface;

import java.time.Instant;

class Transaction {
    private String transactionID;
    private String transactionType;
    private String description;
    private double amount;
    private final long timeStamp = Instant.now().getEpochSecond();

    protected void generateTransactionID() {

    }


    protected static boolean depositFunds(String username, double amount) {
        return false;
    }

    protected static boolean withdrawFunds(String username, double amount) {
        return false;
    }

    protected static boolean makePurchase(String username, double amount, String purchaseDescription) {
        return false;
    }

    protected static boolean payBills(String username, double amount, String billDescription) {
        return false;
    }
}
