package com.ctb.classes;

import com.ctb.interfaces.*;

import java.util.LinkedList;
import java.util.List;

public class BankSystem implements BankInterface{
    private final SecuritySystem system = new SecuritySystem();
    private String currentLoggedInUser;
    private String currentProductType;
    private String currentSessionID;
    private String dataFilePath;
    private List<UserInterface> users = new LinkedList<>();
    private List<Profile> profiles = new LinkedList<>();
    private List<Transaction> transactionHistory = new LinkedList<>();
    private List<ProductApplication> productApplications = new LinkedList<>();
    private List<Session> sessions = new LinkedList<>();
    private List<Dashboard> dashboards = new LinkedList<>();

    @Override
    public double calculateEarnedInterest(String username) {
        return 0;
    }

    @Override
    public double calculateTotalPaid(String username) {
        return 0;
    }

    @Override
    public double calculateTotalSpent(String username) {
        return 0;
    }

    @Override
    public double calculateTotalNet(String username) {
        return 0;
    }

    @Override
    public void setCurrentLoggedInUser(String username) {

    }

    @Override
    public boolean isValidProductType(String productType) {
        return false;
    }

    @Override
    public void setCurrentProductType(String productType) {

    }

    @Override
    public String getCurrentProductType(String username) {
        return null;
    }

    @Override
    public double getCurrentBalance(String username) {
        return 0;
    }

    @Override
    public boolean isAdmin(String username) {
        return false;
    }

    @Override
    public boolean isCustomerService(String username) {
        return false;
    }

    @Override
    public boolean createUser(String name, String username, String password, String email, String phoneNum, char twoFA, String productType) {
        return false;
    }

    @Override
    public void loadDataToFile() {

    }

    @Override
    public static void saveDataToFile() {

    }
}
