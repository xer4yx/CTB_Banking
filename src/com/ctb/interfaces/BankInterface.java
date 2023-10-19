package com.ctb.interfaces;

public interface BankInterface {
    double calculateEarnedInterest(final String username);
    double calculateTotalPaid(final String username);
    double calculateTotalSpent(final String username);
    double calculateTotalNet(final String username);
    void setCurrentLoggedInUser(final String username);
    boolean isValidProductType(final String productType);
    void setCurrentProductType(final String productType);
    String getCurrentProductType(final String username);
    double getCurrentBalance(final String username);
    boolean isAdmin(final String username);
    boolean isCustomerService(final String username);
    boolean createUser(final String name, final String username, final String password, final String email,
                       final String phoneNum, final char twoFA, final String productType);
    void loadDataToFile();
    void saveDataToFile();

}
