package com.ctb.interfaces;

public interface TransactionInterface {
    void generateTransactionID();
    boolean depositFunds(final String username, final double amount);
    boolean withdrawFunds(final String username, final double amount);
    boolean makePurchase(final String username, final double amount, final String purchaseDescription);
    boolean payBills(final String username, final double amount, final String billDescription);
}
