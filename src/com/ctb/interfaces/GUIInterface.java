package com.ctb.interfaces;

public interface GUIInterface {
    void displayMainMenu();
    boolean loginUser(String loggedInUsername);
    void logout(final String username);
    void forgotPassword();
    void displayDashboardMenu(final String username);
    void handleDashboardOptions(final String username);
    void handleProductOptions(final String productType, final String username);
    void displaySavingsMenu(final String username);
    void displayCreditMenu(final String username);
    void displayTransactionMenu(final String username);
    void displayTransactionCredit(final String username);
    void displayTransactionHistory(final String username);
    void handleTransactionCenter(final String username);
    void handleCreditCenter(final String username);
    void handleHelpAndResources(final String username);
}
