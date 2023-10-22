package com.ctb.classes;

import java.lang.Math;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

class BankSystem {
    private final SecuritySystem system = new SecuritySystem();
    private String currentLoggedInUser;
    private String currentProductType;
    private String currentSessionID;
    private String dataFilePath;
    private static final List<User> users = new LinkedList<>();
    private static final List<Profile> profiles = new LinkedList<>();
    private final List<Transaction> transactionHistory = new LinkedList<>();
    private static final List<ProductApplication> productApplications = new LinkedList<>();
    private final List<Session> sessions = new LinkedList<>();
    private final List<Dashboard> dashboards = new LinkedList<>();

    protected static List<User> getUsers() {return users;}
    protected List<Profile> getProfiles() {return profiles;}
    protected List<Transaction> getTransactionHistory() {return transactionHistory;}
    protected List<ProductApplication> getProductApplications() {return productApplications;}
    protected List<Session> getSessions() {return sessions;}
    protected List<Dashboard> getDashboards() {return dashboards;}

    protected static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    protected double calculateEarnedInterest(String username) {
        double interestRate = 0.05;
        double interestEarned = 0;
        LocalDateTime now = LocalDateTime.now();

        for (final User user : users)
        {
            if (Objects.equals(User.getUsername(), username))
            {
                for (final Transaction transaction : getTransactionHistory())
                {
                    if (Objects.equals(transaction.getTransactionType(), "Deposit"))
                    {
                        Duration duration = Duration.between(transaction.getTimeStamp(), now);
                        long seconds = duration.getSeconds();
                        double principal = transaction.getAmount();
                        double years = (double) seconds / (60 * 60 * 24 * 365.25);
                        double amount = principal * Math.pow(1 + interestRate, years);
                        double interest = amount - principal;

                        interestEarned += interest;
                    }
                }
            }
        }

        return interestEarned;
    }

    protected double calculateTotalPaid(String username) {
        double totalPaid = 0;

        for (final User user : users)
        {
            if (Objects.equals(User.getUsername(), username))
            {
                for (final Transaction transaction : getTransactionHistory())
                {
                    if (Objects.equals(transaction.getTransactionType(), "Bill Payment"))
                    {
                        totalPaid += transaction.getAmount();
                    }
                }
            }
        }

        return totalPaid;
    }

    protected double calculateTotalSpent(String username) {
        double totalSpent = 0;

        for (final User user : users)
        {
            if (Objects.equals(User.getUsername(), username))
            {
                for (final Transaction transaction : getTransactionHistory())
                {
                    if (Objects.equals(transaction.getTransactionType(), "Purchase") ||
                            Objects.equals(transaction.getTransactionType(), "Deposit"))
                    {
                        totalSpent += transaction.getAmount();
                    }
                }
            }
        }
        return totalSpent;
    }

    protected double calculateTotalNet(String username) {
        double totalNet = 0;
        for (final User user : users)
        {
            if (Objects.equals(User.getUsername(), username))
            {
                for (final Transaction transaction : getTransactionHistory())
                {
                    if (Objects.equals(transaction.getTransactionType(), "Deposit"))
                    {
                        totalNet += transaction.getAmount();
                    }
                }
            }
        }
        return totalNet;
    }

    protected void setCurrentLoggedInUser(String username) {currentLoggedInUser = username;}

    protected boolean isValidProductType(String productType) {
        List<String> validProductTypes = new LinkedList<>(Arrays.asList("Savings Account", "Credit Account"));
        return validProductTypes.contains(productType);
    }

    protected void setCurrentProductType(String productType) {
        if (!isValidProductType(productType))
        {
            System.out.println("Invalid product type.");
            return;
        }

        currentProductType = productType;
    }

    protected String getCurrentProductType(String username) {
        for (final User user : users)
        {
            if (Objects.equals(User.getUsername(), username))
            {
                return user.getProductType();
            }
        }
        return "Unknown";
    }

    protected double getCurrentBalance(String username) {
        for (final User user : users)
        {
            if (Objects.equals(User.getUsername(), username))
            {
                return user.getBalance();
            }
        }
        return -1.0;
    }

    protected static boolean isAdmin(String username) {
        for (final User user : users)
        {
            if (Objects.equals(User.getUsername(), username))
            {
                return user.isAdmin();
            }
        }
        return false;
    }

    protected static boolean isCustomerService(String username) {
        for (final User user : users)
        {
            if (Objects.equals(User.getUsername(), username))
            {
                return user.isCustomerService();
            }
        }
        return false;
    }

    protected static boolean createUser(String name, String username, String password, String email, String phoneNum, char twoFA, String productType) {
        if (User.isUsernameTaken(username))
        {
            System.out.print("Username is already taken. Please choose another one.");
            return false;
        }

        User newUser = new User();
        newUser.setUserID(User.generateUserID());
        newUser.setName(name);
        newUser.setUsername(username);
        newUser.setPassword(SecuritySystem.encrypt(password));
        newUser.setAdmin(false);
        newUser.setProductType(productType);
        newUser.setBalance(0.0);

        // Create a new profile for the user
        Profile newProfile = new Profile();
        newProfile.setEmail(email);
        newProfile.setPhoneNumber(phoneNum);
        newProfile.set2FAStatus(SecuritySystem.enable2FA(twoFA));

        ProductApplication newProductApplication = new ProductApplication();
        newProductApplication.setProductType(productType);
        newProductApplication.setProductID(ProductApplication.generateProductID(productType));

        productApplications.add(newProductApplication);
        profiles.add(newProfile);

        newUser.setUserProductApplications(productApplications);
        newUser.setUserProfile(profiles);
        users.add(newUser);

        saveDataToFile();
        System.out.print("\nUser account created successfully.");
        return true;
    }

    protected void loadDataToFile() {
        ifstream file(dataFilePath);
        if (!file.is_open())
        {
            cout << "Error: Unable to open data file." << endl;
            system.auditLog(false);
            return;
        }

        json j;
        file >> j;
        file.close();

        for (final auto item : j)
        {
            User user;
            user.userID = item.value("id", "");
            user.name = item.value("name", "");
            user.username = item.value("username", "");
            user.isadmin = item.value("isadmin", false);
            user.iscustomerservice = item.value("iscustomerservice", false);
            user.password = item.value("password", "");
            user.producttype = item.value("producttype", "");
            user.balance = item.value("balance", 0.0);

            for (final auto profileItem : item["profiles"])
            {
                Profile profile;
                profile.email = profileItem.value("email", "");
                profile.phone = profileItem.value("phone", "");
                profile.isTwoFactorEnabled = profileItem.value("isTwoFactorEnabled", false);
                user.profiles.emplace_back(profile);
            }

            if (item.contains("transactionhistory"))
            {
                for (final auto transactionItem : item["transactionhistory"])
                {
                    Transaction transaction;
                    transaction.transactionID = transactionItem.value("transactionID", "");
                    transaction.transactionType = transactionItem.value("transactionType", "");
                    transaction.amount = transactionItem.value("amount", 0.0);
                    transaction.timestamp = transactionItem.value("timestamp", 0);
                    transaction.description = transactionItem.value("description", "");
                    user.transactionhistory.emplace_back(transaction);
                }
            }

            if (item.contains("sessions"))
            {
                for (final auto sessionItem : item["sessions"])
                {
                    Session session;
                    session.sessionID = sessionItem.value("sessionID", "");
                    session.username = sessionItem.value("username", "");
                    session.timestamp = sessionItem.value("timestamp", 0);
                    user.sessions.emplace_back(session);
                }
            }

            if (item.contains("productapplications"))
            {
                for (final auto productapplicationItem : item["productapplications"])
                {
                    ProductApplication productapplication;
                    productapplication.producttype = productapplicationItem.value("producttype", "");
                    productapplication.productID = productapplicationItem.value("productID", "");
                    user.productapplications.emplace_back(productapplication);
                }
            }

            if (item.contains("helpandresources"))
            {
                for (final auto helpandresourcesItem : item["helpandresources"])
                {
                    HelpandResources resources;
                    resources.helpID = helpandresourcesItem.value("helpandresourcesID", "");
                    resources.helpandresourcesType = helpandresourcesItem.value("helpandresourcesType", "");
                    resources.helpandresourcesDescription = helpandresourcesItem.value("helpandresourcesDescription", "");
                    user.helpandresources.emplace_back(resources);
                }
            }
            users.emplace_back(user);
            SecuritySystem.auditLog(true);
        }
    }

    protected static void saveDataToFile() {
        try
        {
            ofstream file(dataFilePath);
            if (!file.is_open())
            {
                cout << "Error: Unable to save data to the file." << endl;
                system.auditLog(true);
                return;
            }
            json j;
            for (final User user : users)
            {
                json userJson;
                userJson["id"] = user.userID;
                userJson["name"] = user.name;
                userJson["username"] = user.username;
                userJson["password"] = user.password;
                userJson["isadmin"] = user.isadmin;
                userJson["iscustomerservice"] = user.iscustomerservice;
                userJson["producttype"] = user.producttype;
                userJson["balance"] = user.balance;

                for (final Profile profile : user.profiles)
                {
                    json profileJson;
                    profileJson["email"] = profile.email;
                    profileJson["phone"] = profile.phone;
                    profileJson["isTwoFactorEnabled"] = profile.isTwoFactorEnabled;
                    userJson["profiles"].push_back(profileJson);
                }
                for (final Transaction transaction : user.transactionhistory)
                {
                    json transactionJson;
                    transactionJson["transactionID"] = transaction.transactionID;
                    transactionJson["transactionType"] = transaction.transactionType;
                    transactionJson["amount"] = transaction.amount;
                    transactionJson["timestamp"] = transaction.timestamp;
                    transactionJson["description"] = transaction.description;
                    userJson["transactionhistory"].push_back(transactionJson);
                }
                for (final Session session : user.sessions)
                {
                    json sessionJson;
                    sessionJson["sessionID"] = session.sessionID;
                    sessionJson["username"] = session.username;
                    sessionJson["timestamp"] = session.timestamp;
                    userJson["sessions"].push_back(sessionJson);
                }
                for (final ProductApplication productapplication : user.productapplications)
                {
                    json productapplicationJson;
                    productapplicationJson["producttype"] = productapplication.producttype;
                    productapplicationJson["productID"] = productapplication.productID;
                    userJson["productapplications"].push_back(productapplicationJson);
                }
                for (final HelpandResources resources : user.helpandresources)
                {
                    json helpandresourcesJson;
                    helpandresourcesJson["helpandresourcesID"] = resources.helpID;
                    helpandresourcesJson["helpandresourcesType"] = resources.helpandresourcesType;
                    helpandresourcesJson["helpandresourcesDescription"] = resources.helpandresourcesDescription;
                    userJson["helpandresources"].push_back(helpandresourcesJson);
                }
                j.push_back(userJson);
            }
            file << j.dump(4);
            file.close();
        }
        catch (final json::exception e) // catching specific exceptions related to the json library
        {
            cout << "JSON error: " << e.what() << endl;
            system.auditLog(false);
        }
        catch (final exception e) // generic C++ exceptions
        {
            cout << "Error saving data to file: " << e.what() << endl;
            system.auditLog(false);
        }
    }
}
