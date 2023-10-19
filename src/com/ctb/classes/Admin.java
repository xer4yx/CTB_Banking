package com.ctb.classes;

import com.ctb.interfaces.AdminInterface;

public class Admin implements AdminInterface {
    private String adminID;
    private String name;
    private String username;
    private String password;

    @Override
    public void handleAccSettings(String username) {

    }

    @Override
    public void depositFunds() {

    }

    @Override
    public void withdrawFunds() {

    }

    @Override
    public void makePurchase() {

    }

    @Override
    public void payBills() {

    }

    @Override
    public boolean deleteUserByUsername(String username) {
        return false;
    }

    @Override
    public void handleManageUsers(String username) {

    }

    @Override
    public void updateUser() {

    }

    @Override
    public void deleteUser() {

    }

    @Override
    public void displayUserdata(String username) {

    }

    @Override
    public void displayAllUserData() {

    }

    @Override
    public void makeUserAdmin(String username) {

    }

    @Override
    public void makeUserCustomerService(String username) {

    }
}
