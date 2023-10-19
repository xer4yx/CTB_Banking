package com.ctb.classes;

import com.ctb.interfaces.*;

import java.util.LinkedList;
import java.util.List;

public class BankSystem {
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
}
