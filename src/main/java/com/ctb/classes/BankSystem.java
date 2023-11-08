package com.ctb.classes;

import com.ctb.exceptions.DataInsertionException;
import com.ctb.exceptions.DataRetrievalException;
import com.ctb.exceptions.InvalidProductTypeException;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class BankSystem {
    static String url = "jdbc:mysql://localhost:3306/ctb_banking";
    static String userDB = "root";
    static String passwordDB = "Vertig@6925";
    private static String driver = "com.mysql.cj.jdbc.Driver";
    private final SecuritySystem system = new SecuritySystem();
    private static long currentUserID;
    private static String currentLoggedInUser;
    private static String currentProductType;
    private static long currentBalance;
    private static String dataFilePath;

    public static final List<User> users = new LinkedList<>();
    public static final List<Profile> profiles = new LinkedList<>();
    private static final List<Transaction> transactionHistory = new LinkedList<>();
    private static final List<ProductApplication> productApplications = new LinkedList<>();
    private static final List<Session> sessions = new LinkedList<>();
    private static final List<Dashboard> dashboards = new LinkedList<>();

    /*----------------------Setter Methods----------------------*/
    protected static void setCurrentUserID(long currentUserID) {
        BankSystem.currentUserID = currentUserID;
    }

    protected static void setCurrentLoggedInUser(String username) {currentLoggedInUser = username;}

    protected static void setCurrentProductType(String productType) {
        try {
            if (!isValidProductType(productType)) {
                throw new InvalidProductTypeException("Invalid Product Type");
            }

            currentProductType = productType;
        } catch (InvalidProductTypeException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /*----------------------Getter Methods----------------------*/
    protected static long getCurrentUserID() {
        return currentUserID;
    }

    protected static String getCurrentLoggedInUser() {
        return currentLoggedInUser;
    }

    protected static String getCurrentProductType(String username) {
        return currentProductType;
    }

    protected static double getCurrentBalance(String username) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;

        try {
            connection = DriverManager.getConnection(url, userDB, passwordDB);

            String query = "SELECT balance FROM users WHERE username = ?";

            statement = connection.prepareStatement(query);
            statement.setString(1, username);

            dataSet = statement.executeQuery();

            if(dataSet.next()) {
                return dataSet.getDouble("balance");
            } else {
                throw new DataRetrievalException("Balance Irretrievable");
            }

        } catch (SQLException e) {
            System.err.print("Error on Data Retrieval: " + e.getMessage());
        } finally {
            closeResources(connection, statement, dataSet);
        }

        return -1.0;
    }

    @Deprecated
    protected static List<User> getUsers() {return users;}

    @Deprecated
    protected List<Profile> getProfiles() {return profiles;}

    @Deprecated
    protected static List<Transaction> getTransactionHistory() {return transactionHistory;}

    @Deprecated
    protected List<ProductApplication> getProductApplications() {return productApplications;}

    @Deprecated
    protected List<Session> getSessions() {return sessions;}

    @Deprecated
    protected List<Dashboard> getDashboards() {return dashboards;}

    /*----------------------Class Methods----------------------*/
    @Deprecated
    public BankSystem(String dataFile) {
        dataFilePath = dataFile;
        loadDataFromFile();
    }

    @Deprecated
    protected static void clearConsole() {
        AnsiConsole.systemInstall();
        Ansi ansi = Ansi.ansi();
        ansi.eraseScreen();
        ansi.cursor(0, 0);
        System.out.print(ansi.toString());
        AnsiConsole.systemUninstall();
    }

    protected static void closeResources(Connection connection, PreparedStatement statement, ResultSet dataSet) {
        try {
            if (dataSet != null) {
                dataSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static void closeResources(Connection connection, PreparedStatement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static double showInterestEarned(){
        double interestRate = 0.05;
        double interestEarned = 0;
        double principal = 0;

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;

        try {
            connection = DriverManager.getConnection(url, userDB, passwordDB);

            String query = "SELECT amount FROM transactions WHERE user_id = ? AND transact_type = ?";

            statement = connection.prepareStatement(query);
            statement.setLong(1, getCurrentUserID());
            statement.setString(2, "Deposit");

            dataSet = statement.executeQuery();
            while(dataSet.next()) {
                principal += dataSet.getDouble("amount");
                Date transactionDate = dataSet.getDate("timestamp");
                long diffInMillis = Math.abs(now.getTime() - transactionDate.getTime());
                long years = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) / 365;
                double amount = principal * Math.pow(1 + interestRate, years);
                double interest = amount - principal;

                interestEarned += interest;
            }

            dataSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return interestEarned;
    }

    protected static double calculateTotalPaid() {
        double totalPaid = 0;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;

        try (Connection connection = DriverManager.getConnection(url, userDB, passwordDB)){
            String query = "SELECT amount FROM transactions WHERE user_id = ? AND transact_type = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, getCurrentUserID());
            statement.setString(2, "Bill Payment");
            ResultSet dataSet = statement.executeQuery();

            while(dataSet.next()) {
                totalPaid += dataSet.getDouble("amount");
            }

            dataSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalPaid;
    }

    protected static double calculateTotalSpent() {
        double totalSpent = 0;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;

        try (Connection connection = DriverManager.getConnection(url, userDB, passwordDB)){
            String query = "SELECT amount FROM transactions WHERE user_id = ? AND transact_type = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, getCurrentUserID());
            statement.setString(2, "Purchase");

            ResultSet dataSet = statement.executeQuery();
            while(dataSet.next()) {
                totalSpent += dataSet.getDouble("amount");
            }

            dataSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalSpent;
    }

    protected static double calculateTotalNet() {
        double totalNet = 0;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;

        try (Connection connection = DriverManager.getConnection(url, userDB, passwordDB)){
            String query = "SELECT amount FROM transactions WHERE user_id = ? AND transact_type = ? ";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, getCurrentUserID());
            statement.setString(2, "Deposit");

            ResultSet dataSet = statement.executeQuery();
            while(dataSet.next()) {
                totalNet += dataSet.getDouble("amount");
            }

            dataSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalNet;
    }

    @Deprecated
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

    @Deprecated
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

    @Deprecated
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

    @Deprecated
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

    protected static boolean isAdmin(String username) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;

        try(Connection connection = DriverManager.getConnection(url, userDB, passwordDB)) {
            String query = "SELECT is_admin FROM users WHERE username = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);

            ResultSet dataSet = statement.executeQuery();
            return dataSet.getBoolean("is_admin");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected static boolean isCustomerService(String username) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;

        try(Connection connection = DriverManager.getConnection(url, userDB, passwordDB)) {
            String query = "SELECT is_customerservice FROM users WHERE username = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);

            ResultSet dataSet = statement.executeQuery();
            return dataSet.getBoolean("is_customerservice");

        } catch (SQLException e) {
            e.printStackTrace();
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
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.print("Driver error: " + e.getMessage());
            throw new RuntimeException(e);
        }

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection(url, userDB, passwordDB);

            String query = "INSERT INTO users (user_id, fname, mname, lname, username, password, email, phone_number, " +
                    "is2fa, is_admin, is_customerservice, product_type, balance) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

            try {
                statement = connection.prepareStatement(query);

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
                System.out.println("Number of affected rows: " + affectedRows); // [DEBUGGER]
            } catch (SQLException e) {
                throw new DataInsertionException("Error while inserting data into users table: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.print("Error connecting to database: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            closeResources(connection, statement);
        }

        System.out.print("\nUser account created successfully.");
        return true;
    }

    @Deprecated
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

    @Deprecated
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


}
