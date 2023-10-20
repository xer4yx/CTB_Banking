package com.ctb.interfaces;

public interface UserInterface {
    void displayActivityLog(final String username);
    void displaySessions(final String username);
    void handleSettings(final String username);
    void processDeposit(final String username);
    void processWithdrawal(final String username);
    void processPurchase(final String username);
    void processBills(final String username);
    void applyProduct();
    String generateUserID();
    boolean isUsernameTaken(final String username);
    void changePassword(final String username, final String password);
    void changeEmail(final String username, final String email);
    void changePhoneNum(final String username, final String phoneNum);
    void changeUsername(final String username, final String newUsername);
    void change2FAStatus(final String username, final char twoFA);
    void askHelp(final String username);
}
