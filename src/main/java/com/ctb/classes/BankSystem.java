package com.ctb.classes;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class BankSystem {
    private static String url = "jdbc:mysql://localhost:3306/ctb_banking";
    private static String user = "root";
    private static String password = "Vertig@6925";
    private static String driver = "com.mysql.cj.jdbc.Driver";
    private final SecuritySystem system = new SecuritySystem();
    private static String currentLoggedInUser;
    private static String currentProductType;
    private String currentSessionID;
    private static String dataFilePath;
    public static final List<User> users = new LinkedList<>();
    public static final List<Profile> profiles = new LinkedList<>();
    private static final List<Transaction> transactionHistory = new LinkedList<>();
    private static final List<ProductApplication> productApplications = new LinkedList<>();
    private static final List<Session> sessions = new LinkedList<>();
    private static final List<Dashboard> dashboards = new LinkedList<>();

    protected static List<User> getUsers() {return users;}
    protected List<Profile> getProfiles() {return profiles;}
    protected static List<Transaction> getTransactionHistory() {return transactionHistory;}
    protected List<ProductApplication> getProductApplications() {return productApplications;}
    protected List<Session> getSessions() {return sessions;}
    protected List<Dashboard> getDashboards() {return dashboards;}

    public BankSystem(String dataFile) {
        dataFilePath = dataFile;
        loadDataFromFile();
    }

    protected static void clearConsole() {
        AnsiConsole.systemInstall();
        Ansi ansi = Ansi.ansi();
        ansi.eraseScreen();
        ansi.cursor(0, 0);
        System.out.print(ansi.toString());
        AnsiConsole.systemUninstall();
    }

    protected static double showInterestEarned(String username) {
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

    protected static double calculateTotalPaid(String username) {
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

    protected static double calculateTotalSpent(String username) {
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

    protected static double calculateTotalNet(String username) {
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

    protected static boolean isValidProductType(String productType) {
        List<String> validProductTypes = new LinkedList<>(Arrays.asList("Savings Account", "Credit Account"));
        return validProductTypes.contains(productType);
    }

    protected static void setCurrentLoggedInUser(String username) {currentLoggedInUser = username;}

    protected static void setCurrentProductType(String productType) {
        if (!isValidProductType(productType))
        {
            System.out.print("Invalid product type.");
            return;
        }

        currentProductType = productType;
    }

    protected static String getCurrentLoggedInUser() {
        return currentLoggedInUser;
    }

    protected static String getCurrentProductType(String username) {
        for (final User user : users)
        {
            if (Objects.equals(User.getUsername(), username))
            {
                return user.getProductType();
            }
        }
        return "Unknown";
    }

    protected static double getCurrentBalance(String username) {
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

    protected static boolean createUser(String fname, String mname, String lname,String username, String password, String email, String phoneNum, char twoFA, String productType) {
        if (User.isUsernameTaken(username))
        {
            System.out.print("Username is already taken. Please choose another one.");
            return false;
        }

        User newUser = new User();
        /*newUser.setUserID(User.generateUserID());
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

        newUser.userProfile.add(newProfile);

        ProductApplication newProductApplication = new ProductApplication();
        newProductApplication.setProductType(productType);
        newProductApplication.setProductID(ProductApplication.generateProductID(productType));

        newUser.userProductApplications.add(newProductApplication);

        users.add(newUser);

        saveDataToFile();*/ /*[Commented code block ends here]*/

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.print("Driver error: " + e.getMessage());
            throw new RuntimeException(e);
        }

        try(Connection connection = DriverManager.getConnection(url, user, password)) {

            String query = "INSERT INTO users (user_id, fname, mname, lname, username, password, email, phone_number, " +
                    "is2fa, is_admin, is_customerservice, product_type, balance) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, newUser.generateUserID());
                statement.setString(2, fname);
                statement.setString(3, mname);
                statement.setString(4, lname);
                statement.setString(5, username);
                statement.setString(6, SecuritySystem.encrypt(password));
                statement.setString(7, email);
                statement.setString(8, phoneNum);
                statement.setBoolean(9, SecuritySystem.enable2FA(twoFA));
                statement.setBoolean(10, false);
                statement.setBoolean(11,false);
                statement.setString(12, productType);
                statement.setDouble(13, 0.00);

                int affectedRows = statement.executeUpdate();
                System.out.println("Number of affected rows: " + affectedRows);
            } catch (SQLException e) {
                System.err.println("Error while inserting data into users table: " + e.getMessage());
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            System.err.print("Error connecting to database: " + e.getMessage());
            throw new RuntimeException(e);
        }

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

            JSONArray jsonArray = new JSONArray(data.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                User user = new User();
                user.setUserID(item.optString("id", ""));
                user.setName(item.optString("name", ""));
                user.setUsername(item.optString("username", ""));
                user.setAdmin(item.optBoolean("isadmin", false));
                user.setCustomerService(item.optBoolean("iscustomerservice", false));
                user.setPassword(item.optString("password", ""));
                user.setProductType(item.optString("producttype", ""));
                user.setBalance(item.optDouble("balance", 0.0));

                if(item.has("profiles")) {
                    JSONArray profileArray = item.getJSONArray(("profiles"));
                    for(int n = 0; n < profileArray.length(); n++) {
                        JSONObject profileItem = profileArray.getJSONObject(n);

                        Profile profile = new Profile();
                        profile.setEmail(profileItem.optString("email", ""));
                        profile.setPhoneNumber(profileItem.optString("phone"));
                        profile.set2FAStatus(profileItem.optBoolean("isTwoFactorEnabled", false));

                        user.userProfile.add(profile);
                    }
                }

                if (item.has("transactionhistory")) {
                    JSONArray transactionArray = item.getJSONArray("transactionhistory");
                    for (int n = 0; n < transactionArray.length(); n++) {
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
                        JSONObject helpAndResourcesItem = helpandResourcesArray.getJSONObject(m);

                        HelpAndResources helpAndResource = new HelpAndResources();
                        helpAndResource.setHelpID(helpAndResourcesItem.optString("Help ID", ""));
                        helpAndResource.setH_rType(helpAndResourcesItem.optString("Type", ""));
                        helpAndResource.setH_rDescription(helpAndResourcesItem.optString("Description", ""));

                        user.userHelpAndResources.add(helpAndResource);
                    }
                }
                users.add(user);
            }
        } catch (IOException e) {
            System.err.print("Failed to read from file: " + e.getMessage());
        } catch (Exception e) {
            System.err.print("Failed to parse JSON data: " + e.getMessage());
        }
    }

    protected static void saveDataToFile() {
        BufferedWriter dataWriter = null;

        try {
            dataWriter = new BufferedWriter(new FileWriter(dataFilePath));
            JSONArray dataArray = new JSONArray();

            for (User user : users) {
                JSONObject dataClient = new JSONObject();
                dataClient.put("id", user.getUserID());
                dataClient.put("name", user.getName());
                dataClient.put("username", user.getUsername());  // Use the user's username
                dataClient.put("password", user.getPassword());
                dataClient.put("isadmin", user.isAdmin());
                dataClient.put("iscustomerservice", user.isCustomerService());
                dataClient.put("producttype", user.getProductType());
                dataClient.put("balance", user.getBalance());

                JSONArray profilesJsonArray = new JSONArray();
                for (Profile profile : user.userProfile) {
                    JSONObject dataProfile = new JSONObject();
                    dataProfile.put("email", profile.getEmail());
                    dataProfile.put("phone", profile.getPhoneNumber());
                    dataProfile.put("isTwoFactorEnabled", profile.get2FAStatus());
                    profilesJsonArray.put(dataProfile);
                }
                dataClient.put("profiles", profilesJsonArray);

                JSONArray transactionHistoryJsonArray = new JSONArray();
                for (Transaction transaction : user.userTransaction) {
                    JSONObject dataTransact = new JSONObject();
                    dataTransact.put("transactionID", transaction.getTransactionID());
                    dataTransact.put("transactionType", transaction.getTransactionType());
                    dataTransact.put("amount", transaction.getAmount());
                    dataTransact.put("timestamp", transaction.getTimeStamp());
                    dataTransact.put("description", transaction.getDescription());
                    transactionHistoryJsonArray.put(dataTransact);
                }
                dataClient.put("transactionhistory", transactionHistoryJsonArray);

                JSONArray sessionsJsonArray = new JSONArray();
                for (Session session : user.userSessions) {
                    JSONObject dataSession = new JSONObject();
                    dataSession.put("sessionID", session.getSessionID());
                    dataSession.put("username", user.getUsername());  // Use the user's username
                    dataSession.put("timestamp", session.getTimeStamp());
                    sessionsJsonArray.put(dataSession);
                }
                dataClient.put("sessions", sessionsJsonArray);

                JSONArray productApplicationsJsonArray = new JSONArray();
                for (ProductApplication productApplication : user.userProductApplications) {
                    JSONObject dataProduct = new JSONObject();
                    dataProduct.put("producttype", productApplication.getProductType());
                    dataProduct.put("productID", productApplication.getProductID());
                    productApplicationsJsonArray.put(dataProduct);
                }
                dataClient.put("productapplications", productApplicationsJsonArray);

                JSONArray helpAndResourcesJsonArray = new JSONArray();
                for (HelpAndResources resources : user.userHelpAndResources) {
                    JSONObject dataHR = new JSONObject();
                    dataHR.put("helpandresourcesID", resources.getHelpID());
                    dataHR.put("helpandresourcesType", resources.getH_rType());
                    dataHR.put("helpandresourcesDescription", resources.getH_rDescription());
                    helpAndResourcesJsonArray.put(dataHR);
                }
                dataClient.put("helpandresources", helpAndResourcesJsonArray);

                dataArray.put(dataClient);
            }

            dataWriter.write(dataArray.toString(4));
            dataWriter.close();
        } catch (JSONException e) {
            System.err.print("JSON error: " + e.getMessage());
        } catch (IOException e) {
            System.err.print(("I/O error: " + e.getMessage()));
        } finally {
            if (dataWriter != null) {
                try {
                    dataWriter.close();
                } catch (IOException e) {
                    System.err.print("Error closing file: " + e.getMessage());
                }
            }
        }
    }

    /*protected static void saveDataToDB() {
        User client = new User();

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.print("Driver error: " + e.getMessage());
            throw new RuntimeException(e);z
        }

        try(Connection connection = DriverManager.getConnection(url, user, password)) {

            String query = "INSERT INTO users (user_id, fname, mname, lname, username, password, email, phone_number, is2fa, is_admin, is_customerservice, product_type, balance) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);" +
                    "INSERT INTO transactions (user_id, transaction_id, transact_type, amount, timestamp) VALUES (?,?,?,?,?);" +
                    "INSERT INTO sessions (user_id, session_id, timestamp) VALUES (?,?,?);" +
                    "INSERT INTO  help_resources (user_id, hr_id, hr_type, hr_description) VALUES (?,?,?,?)";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, client.getUserID());
                statement.setString(2, client.getFname());
                statement.setString(3, client.getMname());
                statement.setString(4, client.getLname());
                statement.setString(5, client.getUsername());
                statement.setString(6, client.getPassword());

            } catch (SQLException e) {
                System.err.println("Error while inserting data into users table: " + e.getMessage());
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            System.err.print("Error connecting to database: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }*/ /*[Commented code block ends here]*/
}
