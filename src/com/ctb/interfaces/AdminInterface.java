package com.ctb.interfaces;

public interface AdminInterface {
    void handleAccSettings(final String username);

    void depositFunds();

    void withdrawFunds();

    void makePurchase();

    void payBills();

    boolean deleteUserByUsername(final String username);

    void handleManageUsers(final String username);

    void updateUser();

    void deleteUser();

    void displayUserdata(final String username);

    void displayAllUserData();

    void makeUserAdmin(final String username);

    void makeUserCustomerService(final String username);

}
