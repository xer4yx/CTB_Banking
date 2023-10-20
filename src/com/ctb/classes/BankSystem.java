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
    private final List<User> users = new LinkedList<>();
    private final List<Profile> profiles = new LinkedList<>();
    private final List<Transaction> transactionHistory = new LinkedList<>();
    private final List<ProductApplication> productApplications = new LinkedList<>();
    private final List<Session> sessions = new LinkedList<>();
    private final List<Dashboard> dashboards = new LinkedList<>();

    public static List<User> getUsers() {
        return users;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    protected static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public double calculateEarnedInterest(String username) {
        double interestRate = 0.05; // Annual interest rate
        double interestEarned = 0;

        time_t now = time(nullptr); // get current time

        for (const User &user : users)
        {
            if (user.username == username)
            {
                for (const Transaction &transaction : user.transactionhistory)
                {
                    if (transaction.transactionType == "Deposit")
                    {
                        // Calculate interest for this deposit
                        double principal = transaction.amount;
                        int years = difftime(now, transaction.timestamp) / (60 * 60 * 24 * 365.25); // convert seconds to years
                        double amount = principal * pow(1 + interestRate, years);
                        double interest = amount - principal;

                        // Add to total interest earned
                        interestEarned += interest;
                    }
                }
            }
        }

        return interestEarned;
    }

    public double calculateTotalPaid(String username) {
        double totalPaid = 0;

        for (const User &user : users)
        {
            if (user.username == username)
            {
                for (const Transaction &transaction : user.transactionhistory)
                {
                    if (transaction.transactionType == "Bill Payment")
                    {
                        totalPaid += transaction.amount;
                    }
                }
            }
        }

        return totalPaid;
    }

    public double calculateTotalSpent(String username) {
        double totalSpent = 0;

        for (const User &user : users)
        {
            if (user.username == username)
            {
                for (const Transaction &transaction : user.transactionhistory)
                {
                    if (transaction.transactionType == "Purchase" || transaction.transactionType == "Deposit")
                    {
                        totalSpent += transaction.amount;
                    }
                }
            }
        }

        return totalSpent;
    }

    public double calculateTotalNet(String username) {
        double totalNet = 0;

        for (const User &user : users)
        {
            if (user.username == username)
            {
                for (const Transaction &transaction : user.transactionhistory)
                {
                    if (transaction.transactionType == "Deposit")
                    {
                        totalNet += transaction.amount;
                    }
                }
            }
        }

        return totalNet;
    }

    public void setCurrentLoggedInUser(String username) {currentLoggedInUser = username;}

    public boolean isValidProductType(String productType) {
        vector<string> validProductTypes = {"Savings Account", "Credit Account"};

        // Check if the provided product type is in the list of valid types
        return find(validProductTypes.begin(), validProductTypes.end(), producttype) != validProductTypes.end();
    }

    public void setCurrentProductType(String productType) {
        if (!isValidProductType(producttype))
        {
            cout << "Invalid product type." << endl;
            // Handle the error or return an error code if necessary
            return;
        }

        currentProductType = producttype;
    }

    public String getCurrentProductType(String username) {
        for (const User &user : users)
        {
            if (user.username == username)
            {
                return user.producttype;
            }
        }
        // If we've reached here, the user wasn't found
        return "Unknown"; // You can choose a different indicator if needed
    }

    public double getCurrentBalance(String username) {
        for (const User &user : users)
        {
            if (user.username == username)
            {
                return user.balance;
            }
        }

        // If we've reached here, the user wasn't found
        return -1.0; // Choose a different indicator if needed
    }

    public static boolean isAdmin(String username) {
        for (const User &user : users)
        {
            if (user.username == username)
            {
                return user.isadmin;
            }
        }
        return false;
    }

    public static boolean isCustomerService(String username) {
        for (const User &user : users)
        {
            if (user.username == username)
            {
                return user.iscustomerservice;
            }
        }
        return false;
    }

    public static boolean createUser(String name, String username, String password, String email, String phoneNum, char twoFA, String productType) {
        if (isUsernameTaken(username))
        {
            cout << "Username is already taken. Please choose another one." << endl;
            return false;
        }

        // Create a new user account
        User newUser;
        newUser.userID = generateUserID(); // Call a function to generate a unique user ID
        newUser.name = name;
        newUser.username = username;
        newUser.password = SecuritySys::encryptPass(password);
        newUser.isadmin = false;
        newUser.producttype = producttype;
        newUser.balance = 0.0;

        // Create a new profile for the user
        Profile newProfile;
        newProfile.email = email;
        newProfile.phone = phone;
        newProfile.isTwoFactorEnabled = SecuritySys::enable2FA(twoFA);

        ProductApplication newProductApplication;
        newProductApplication.producttype = producttype;
        newProductApplication.productID = generateProductID(producttype);

        newUser.productapplications.push_back(newProductApplication);
        // Add the new profile to the user's profiles vector
        newUser.profiles.push_back(newProfile);

        // Add the new user to the vector of users
        users.push_back(newUser);

        // Save the updated user data to the file
        saveDataToFile();

        cout << "\nUser account created successfully." << endl;
        return true;
    }

    public void loadDataToFile() {
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

        for (const auto &item : j)
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

            for (const auto &profileItem : item["profiles"])
            {
                Profile profile;
                profile.email = profileItem.value("email", "");
                profile.phone = profileItem.value("phone", "");
                profile.isTwoFactorEnabled = profileItem.value("isTwoFactorEnabled", false);
                user.profiles.emplace_back(profile);
            }

            if (item.contains("transactionhistory"))
            {
                for (const auto &transactionItem : item["transactionhistory"])
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
                for (const auto &sessionItem : item["sessions"])
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
                for (const auto &productapplicationItem : item["productapplications"])
                {
                    ProductApplication productapplication;
                    productapplication.producttype = productapplicationItem.value("producttype", "");
                    productapplication.productID = productapplicationItem.value("productID", "");
                    user.productapplications.emplace_back(productapplication);
                }
            }

            if (item.contains("helpandresources"))
            {
                for (const auto &helpandresourcesItem : item["helpandresources"])
                {
                    HelpandResources resources;
                    resources.helpID = helpandresourcesItem.value("helpandresourcesID", "");
                    resources.helpandresourcesType = helpandresourcesItem.value("helpandresourcesType", "");
                    resources.helpandresourcesDescription = helpandresourcesItem.value("helpandresourcesDescription", "");
                    user.helpandresources.emplace_back(resources);
                }
            }
            users.emplace_back(user);
            system.auditLog(true);
        }
    }

    public static void saveDataToFile() {
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
            for (const User &user : users)
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

                for (const Profile &profile : user.profiles)
                {
                    json profileJson;
                    profileJson["email"] = profile.email;
                    profileJson["phone"] = profile.phone;
                    profileJson["isTwoFactorEnabled"] = profile.isTwoFactorEnabled;
                    userJson["profiles"].push_back(profileJson);
                }
                for (const Transaction &transaction : user.transactionhistory)
                {
                    json transactionJson;
                    transactionJson["transactionID"] = transaction.transactionID;
                    transactionJson["transactionType"] = transaction.transactionType;
                    transactionJson["amount"] = transaction.amount;
                    transactionJson["timestamp"] = transaction.timestamp;
                    transactionJson["description"] = transaction.description;
                    userJson["transactionhistory"].push_back(transactionJson);
                }
                for (const Session &session : user.sessions)
                {
                    json sessionJson;
                    sessionJson["sessionID"] = session.sessionID;
                    sessionJson["username"] = session.username;
                    sessionJson["timestamp"] = session.timestamp;
                    userJson["sessions"].push_back(sessionJson);
                }
                for (const ProductApplication &productapplication : user.productapplications)
                {
                    json productapplicationJson;
                    productapplicationJson["producttype"] = productapplication.producttype;
                    productapplicationJson["productID"] = productapplication.productID;
                    userJson["productapplications"].push_back(productapplicationJson);
                }
                for (const HelpandResources &resources : user.helpandresources)
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
        catch (const json::exception &e) // catching specific exceptions related to the json library
        {
            cout << "JSON error: " << e.what() << endl;
            system.auditLog(false);
        }
        catch (const exception &e) // generic C++ exceptions
        {
            cout << "Error saving data to file: " << e.what() << endl;
            system.auditLog(false);
        }
    }
}
