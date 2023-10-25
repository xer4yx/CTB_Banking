package com.ctb.classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.fusesource.jansi.AnsiConsole;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class BankSystem {
    private final SecuritySystem system = new SecuritySystem();
    private static Logger logger;
    private String currentLoggedInUser;
    private String currentProductType;
    private String currentSessionID;
    private static String dataFilePath;
    public static final List<User> users = new LinkedList<>();
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
        AnsiConsole.systemInstall();
        System.out.print(AnsiConsole.RESET_LINE + AnsiConsole.);
        AnsiConsole.systemUninstall();
    }

    protected double showInterestEarned(String username, List<User> users) {
        double interestRate = 0.05; // Annual interest rate
        double interestEarned = 0;

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();

        for (User user : users) {
            if (User.getUsername().equals(username)) {
                for (Transaction transaction : user.userTransaction) {
                    if (transaction.getTransactionType().equals("Deposit")) {
                        // Calculate interest for this deposit
                        double principal = transaction.getAmount();
                        long diffInMillis = Math.abs(now.getTime() - transaction.getTimeStamp());
                        long years = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) / 365;
                        double amount = principal * Math.pow(1 + interestRate, years);
                        double interest = amount - principal;

                        // Add to total interest earned
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

    protected void loadDataFromFile() {
        try {
            BufferedReader file = new BufferedReader(new FileReader(dataFilePath));
            StringBuilder data = new StringBuilder();
            String line;
            while ((line = file.readLine()) != null) {
                data.append(line);
            }
            file.close();

            JSONObject j = new JSONObject(new JSONTokener(data.toString()));

            for (int i = 0; i < j.length(); i++) {
                JSONObject item = j.getJSONObject(String.valueOf(i));

                User user = new User();
                user.setUserID(item.optString("id", ""));
                user.setName(item.optString("name", ""));
                user.setUsername(item.optString("username", ""));
                user.setAdmin(item.optBoolean("isadmin", false));
                user.setCustomerService(item.optBoolean("iscustomerservice", false));
                user.setPassword(item.optString("password", ""));
                user.setProductType(item.optString("producttype", ""));
                user.setBalance(item.optDouble("balance", 0.0));

                if (item.has("transactionhistory")) {
                    JSONArray transactionArray = item.getJSONArray("transactionhistory");
                    for (int n = 0; i < transactionArray.length(); n++) {
                        JSONObject transactionItem = transactionArray.getJSONObject(n);

                        Transaction transaction = new Transaction();
                        transaction.setTransactionID(transactionItem.optString("transactionID", ""));
                        transaction.setTransactionType(transactionItem.optString("transactionType", ""));
                        transaction.setAmount(transactionItem.optDouble("amount", 0.0));
                        transaction.setTimeStamp(transactionItem.optLong("timestamp", 0));
                        transaction.setDescription(transactionItem.optString("description", ""));

                        user.userTransaction.add(transaction);
                    }
                }

                if (item.has("sessions")) {
                    JSONArray sessionArray = item.getJSONArray("sessions");
                    for (int k = 0; k < sessionArray.length(); k++) {
                        JSONObject sessionItem = sessionArray.getJSONObject(k);

                        Session session = new Session();
                        session.setSessionID(sessionItem.optString("sessionID", ""));
                        session.setUsername(sessionItem.optString("username", ""));
                        session.setTimeStamp(sessionItem.optLong("timestamp", 0));

                        user.userSessions.add(session);
                    }
                }

                if (item.has("productapplications")) {
                    JSONArray productApplicationArray = item.getJSONArray("productapplications");
                    for (int l = 0; l < productApplicationArray.length(); l++) {
                        JSONObject productApplicationItem = productApplicationArray.getJSONObject(l);

                        ProductApplication productApplication = new ProductApplication();
                        productApplication.setProductType(productApplicationItem.optString("producttype", ""));
                        productApplication.setProductID(productApplicationItem.optString("productID", ""));

                        user.userProductApplications.add(productApplication);
                    }
                }

                if (item.has("helpandresources")) {
                    JSONArray helpandResourcesArray = item.getJSONArray("helpandresources");
                    for (int m = 0; m < helpandResourcesArray.length(); m++) {
                        JSONObject helpandResourcesItem = helpandResourcesArray.getJSONObject(m);

                        HelpAndResources helpAndResource = new HelpAndResources();
                        helpAndResource.setHelpID(helpandResourcesItem.optString("Help ID", ""));
                        helpAndResource.setH_rType(helpandResourcesItem.optString("Type", ""));
                        helpAndResource.setH_rDescription(helpandResourcesItem.optString("Description", ""));

                        user.userHelpAndResources.add(helpAndResource);
                    }
                }
                users.add(user);
            }
        } catch (IOException e) {
            logger.severe("Failed to read from file: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Failed to parse JSON data: " + e.getMessage());
        }
    }

    protected static void saveDataToFile() {
        BufferedWriter bufferedWriter = null;

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(dataFilePath));
            JSONArray jsonArray = new JSONArray();

            for (User user : BankSystem.users) {
                JSONObject userJson = new JSONObject();
                userJson.put("id", user.getUserID());
                userJson.put("name", user.getName());
                userJson.put("username", User.getUsername());
                userJson.put("password", user.getPassword());
                userJson.put("isadmin", user.isAdmin());
                userJson.put("iscustomerservice", user.isCustomerService());
                userJson.put("producttype", user.getProductType());
                userJson.put("balance", user.getBalance());

                JSONArray profilesJsonArray = new JSONArray();
                for (Profile profile : user.userProfile) {
                    JSONObject profileJson = new JSONObject();
                    profileJson.put("email", profile.getEmail());
                    profileJson.put("phone", profile.getPhoneNumber());
                    profileJson.put("isTwoFactorEnabled", profile.get2FAStatus());
                    profilesJsonArray.put(profileJson);
                }
                userJson.put("profiles", profilesJsonArray);

                JSONArray transactionHistoryJsonArray = setUserTransactions(user);
                userJson.put("transactionhistory", transactionHistoryJsonArray);

                JSONArray sessionsJsonArray = new JSONArray();
                for (Session session : user.userSessions) {
                    JSONObject sessionJson = new JSONObject();
                    sessionJson.put("sessionID", session.getSessionID());
                    sessionJson.put("username", User.getUsername());
                    sessionJson.put("timestamp", session.getTimeStamp());
                    sessionsJsonArray.put(sessionJson);
                }
                userJson.put("sessions", sessionsJsonArray);

                JSONArray productApplicationsJsonArray = setUserProductApplications(user);
                userJson.put("productapplications", productApplicationsJsonArray);

                JSONArray helpAndResourcesJsonArray = setUserHelpAndResources(user);
                userJson.put("helpandresources", helpAndResourcesJsonArray);

                jsonArray.put(userJson);
            }

            bufferedWriter.write(jsonArray.toString(4));
            bufferedWriter.close();
        } catch (JSONException e) {
            logger.severe("JSON error: " + e.getMessage());
        } catch (IOException e) {
            logger.severe(("I/O error: " + e.getMessage()));
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    System.out.println("Error closing file: " + e.getMessage());
                }
            }
        }
    }

    private static JSONArray setUserHelpAndResources(User user) {
        JSONArray helpAndResourcesJsonArray = new JSONArray();
        for (HelpAndResources resources : user.userHelpAndResources) {
            JSONObject helpAndResourcesJson = new JSONObject();
            helpAndResourcesJson.put("helpandresourcesID", resources.getHelpID());
            helpAndResourcesJson.put("helpandresourcesType", resources.getH_rType());
            helpAndResourcesJson.put("helpandresourcesDescription", resources.getH_rDescription());
            helpAndResourcesJsonArray.put(helpAndResourcesJson);
        }
        return helpAndResourcesJsonArray;
    }

    private static JSONArray setUserProductApplications(User user) {
        JSONArray productApplicationsJsonArray = new JSONArray();
        for (ProductApplication productApplication : user.userProductApplications) {
            JSONObject productApplicationJson = new JSONObject();
            productApplicationJson.put("producttype", productApplication.getProductType());
            productApplicationJson.put("productID", productApplication.getProductID());
            productApplicationsJsonArray.put(productApplicationJson);
        }
        return productApplicationsJsonArray;
    }

    private static JSONArray setUserTransactions(User user) {
        JSONArray transactionHistoryJsonArray = new JSONArray();
        for (Transaction transaction : user.userTransaction) {
            JSONObject transactionJson = new JSONObject();
            transactionJson.put("transactionID", transaction.getTransactionID());
            transactionJson.put("transactionType", transaction.getTransactionType());
            transactionJson.put("amount", transaction.getAmount());
            transactionJson.put("timestamp", transaction.getTimeStamp());
            transactionJson.put("description", transaction.getDescription());
            transactionHistoryJsonArray.put(transactionJson);
        }
        return transactionHistoryJsonArray;
    }
}
