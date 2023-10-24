package com.ctb.interfaces;

public interface AdminInterface {
    boolean deleteUserByUsername(final String username);

    void handleManageUsers(final String username);

    void updateUser();

    void deleteUser();

    void displayUserdata(final String username);

    void displayAllUserData();

    void makeUserAdmin(final String username);

    void makeUserCustomerService(final String username);

}
