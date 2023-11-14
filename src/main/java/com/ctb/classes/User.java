package com.ctb.classes;

import java.text.SimpleDateFormat;
import java.util.*;

public class User {
    private static final Scanner input = new Scanner(System.in);
    private static final Calendar calendar = Calendar.getInstance();
    private static final Date date = calendar.getTime();
    private static final Random rand = new Random();
    private static boolean isAdmin;
    private String userID;
    private String name;
    private static String username;
    private String password;
    private String productType;
    private static boolean isCustomerService;
    private double balance;
    public List<Profile> userProfile = new LinkedList<>();
    public List<Transaction> userTransaction = new LinkedList<>();
    public List<ProductApplication> userProductApplications = new LinkedList<>();
    public List<Session> userSessions = new LinkedList<>();
    public List<HelpAndResources> userHelpAndResources = new LinkedList<>();
    private List<Dashboard> userDashboard = new LinkedList<>();

    /*----------------------Constructor Methods----------------------*/
    public User() {}

    @Deprecated
    User(String name, String username, String productType) { //TODO: delete this
        this.name = name;
        User.username = username;
        this.productType = productType;
    }

    /*----------------------Setter Methods----------------------*/
    public void setUserID(String userID) {this.userID =  userID;}
    public void setName(String name) {this.name = name;}
    public void setUsername(String username) {
        User.username = username;}
    public void setPassword(String password) {this.password = password;}
    public void setProductType(String productType) {this.productType = productType;}
    public void setCSStatus(boolean isCustomerService) {
        User.isCustomerService = isCustomerService;}
    public void setAdminStatus(boolean isAdmin) {this.isAdmin = isAdmin;}
    public void setBalance(double balance) {this.balance += balance;}

    /*----------------------Getter Methods----------------------*/
    public String getUserID() {return this.userID;}
    public String getName() {return this.name;}
    public static String getUsername() {return username;}
    public String getPassword() {return this.password;}
    public String getProductType() {return this.productType;}
    public double getBalance() {
        return balance;
    }

    /*----------------------Class Methods----------------------*/
    protected static void displayDashboardMenu(final String username) {
        if (Objects.equals(BankSystem.getCurrentLoggedInUser(), username)) {
            if (User.isAdmin()) {
                Admin.displayDashboardMenu();
            } else if (User.isCustomerService()) {
                CustomerService.displayDashboardMenu(BankSystem.getCurrentLoggedInUser());
            } else {
                BankSystem.clearConsole(); //TODO: delete this
                System.out.print(
                        """
                                
                                ╭─────────────────────────────────────╮
                                │         CENTRAL TRUST BANK          │
                                ╰─────────────────────────────────────╯
                                """
                );
                System.out.print("Welcome " + BankSystem.getCurrentLoggedInUser() + "!");
                System.out.print("\nCurrent Balance: $" + BankSystem.getCurrentBalance(BankSystem.getCurrentLoggedInUser()));
                System.out.print(
                        """
                                
                                ╔═════════════════════════════════════╗
                                ║         Dashboard Options:          ║
                                ╠═════════════════════════════════════╣
                                ║  1. Transaction Center              ║
                                ║  2. User Profile                    ║
                                ║  3. Data Analytics Dashboard        ║
                                ║  4. Help  Resources                 ║
                                ║  5. Logout                          ║
                                ╚═════════════════════════════════════╝
                                Enter your choice:\s"""
                );

            }
        }
    }

    public void displayUserSettings(String username) {
        while (true) {
            System.out.print(
                    """

                            ╭────────────────────────╮
                            │     User Settings      │
                            ├────────────────────────┤
                            │ 1. Manage Account      │
                            │ 2. Back to Dashboard   │
                            ╰────────────────────────╯
                            """);
            System.out.print("Enter: ");
            int settingChoice = input.nextInt();
            switch (settingChoice) {
                case 1:
                    handleSettings(username);
                    break;
                case 2:
                    return;
                default:
                    System.out.print("Invalid choice. Please select a valid option.");
                    break;
            }
        }
    }

    public static void displayActivityLog(String username) {
        System.out.print(
                """

                        ╭───────────────────────────╮
                        │      Activity Log         │
                        ├───────────────────────────┤
                        │ 1. Transaction History    │
                        │ 2. Session History        │
                        │ 3. Help History           │
                        ╰───────────────────────────╯"""
        );

        System.out.print("\nEnter: ");
        int choice = input.nextInt();

        switch (choice) {
            case 1:
                BankSystem.clearConsole(); //TODO: delete this
                Display.displayTransactionHistory(username);
                break;
            case 2:
                displaySessions(username);
                break;
            case 3:
                BankSystem.clearConsole(); //TODO: delete this
                CustomerService.displayHelpHistory(username);
                break;
            default:
                System.out.print("*Invalid choice. Please select a valid option.");
                break;
        }
    }

    public static void displaySessions(String username) {
        //TODO: Separate method of interface and data retrieval
        //CONVERT: List -> Database
        for (final User user : BankSystem.users) {
            if (Objects.equals(User.username, username)) {
            BankSystem.clearConsole(); //TODO: delete this
                System.out.print(
                        "\n╔════════════════════════════════════════════╗" +
                        "\n║               Session History              ║" +
                        "\n╚════════════════════════════════════════════╝" +
                        "\n User: " + User.username +
                        "\n──────────────────────────────────────────────"
                );
                for (final Session session : user.userSessions) {
                    System.out.print(
                            "\nSession ID: " + session.getSessionID() +
                            "\nUsername: " + getUsername() +
                            "\nTimestamp: " + session.getTimeStamp() +
                            "\n──────────────────────────────────────────────"
                    );
                }
            }
        }
    }

    public static void handleSettings(String username) {
        String newPassword, newEmail, newPhoneNumber, newUsername;
        char new2FA;
        System.out.print(
                """

                        ╔═════════════════════════════════════╗
                        ║           Manage Account            ║
                        ╠═════════════════════════════════════╣
                        ║  1. Change Password                 ║
                        ║  2. Change Email                    ║
                        ║  3. Change Phone                    ║
                        ║  4. Change Username                 ║
                        ║  5. Enable/Disable 2FA              ║
                        ║  6. Show Activity Log               ║
                        ║  7. Back to Profile                 ║
                        ╚═════════════════════════════════════╝""");
        System.out.print("\nEnter: ");
        int accChoice = input.nextInt();
        switch (accChoice) {
            case 1:
                System.out.print("\nEnter new password: ");
                newPassword = input.nextLine();
                changePassword(username);
                break;

            case 2:
                System.out.print("\nEnter new email: ");
                newEmail = input.nextLine();
                changeEmail(username);
                break;

            case 3:
                System.out.print("\nEnter new phone: ");
                newPhoneNumber = input.nextLine();
                changePhoneNum(username);
                break;

            case 4:
                System.out.print("\nEnter new username: ");
                newUsername = input.nextLine();
                changeUsername(username);
                break;

            case 5:
                System.out.print("\nDo you want to enable 2FA?(Y/N): ");
                new2FA = input.next().charAt(0);
                change2FAStatus(username);
                break;

            case 6:
                displayActivityLog(username);
                break;

            case 7:
                return;

            default:
                System.out.print("*Invalid choice. Please select a valid option.");
                break;
        }

    }

    public static void processDeposit(String username) {
        System.out.print("\nEnter the amount to deposit: $");
        double depositAmount = input.nextDouble();
        input.nextLine();
        if (depositAmount <= 0.0) {
            System.out.print("*Invalid deposit amount. Please enter a positive amount.");
            return;
        } else if (Transaction.depositFunds(username, depositAmount)) {
            System.out.print("Deposit of $" + depositAmount + " successful.");
        } else {
            System.out.print("*Deposit failed. Please try again.");
        }
        System.out.print("\nPress Enter to continue...");
        input.nextLine();
    }

    public static void processWithdrawal(String username) {
        System.out.print("\nEnter the amount to withdraw: $");
        double withdrawAmount = input.nextDouble(); // Clear the newline character

        if (withdrawAmount <= 0.0) {
            System.out.print("*Invalid withdrawal amount. Please enter a positive amount.");
            return;
        } else if (Transaction.withdrawFunds(username, withdrawAmount)) {
            System.out.print("\nWithdrawal of $" + withdrawAmount + " successful.");
        } else {
            System.out.print("*Withdrawal failed. Please try again.");
        }
        System.out.print("\nPress Enter to continue...");
        input.nextLine();
    }

    public static void processPurchase(String username) {
        System.out.print("\nEnter the purchase amount: $");
        double purchaseAmount = input.nextDouble();
        System.out.print("\nEnter the purchase description: ");
        String purchaseDescription = input.nextLine();
        if (input.hasNextDouble()) {
            input.nextDouble();
            System.out.print("*Invalid amount. Please enter a valid number.");
        }
        input.nextLine();

        if (purchaseAmount <= 0.0) {
            System.out.print("*Invalid transaction amount. Please enter a positive amount.");
        } else if (Transaction.makePurchase(username, purchaseAmount, purchaseDescription)) {
            System.out.print("\nPurchase of $" + purchaseAmount + " successful.");
        } else {
            System.out.print("*Purchase failed. Please try again.");
        }
        System.out.print("Press Enter to continue...");
        input.nextLine();
    }

    public static void processBills(String username) {
        System.out.print("\nEnter the bill amount: $");
        double billAmount = input.nextDouble();

        System.out.print("\nEnter the bill description: ");
        String billDescription = input.nextLine();

        if (billAmount <= 0.0) {
            System.out.print("*Invalid amount. Please enter a positive amount.");
        } else if (Transaction.payBills(username, billAmount, billDescription)) {
            System.out.print("\nPayment of $" + billAmount + " successful.");
        } else {
            System.out.print("*Payment failed. Please try again.");
        }

        System.out.print("\nPress Enter to continue...");
        input.nextLine();
    }

    public static void applyProduct() {
        String fname, mname, lname;
        String username, password, email, phone, accounttype;
        int acctype;
        char enable2FA;
        while (true) {
            System.out.print(
                    """

                            ╔═════════════════════════════════════╗
                            ║       Product Application           ║
                            ╚═════════════════════════════════════╝"""
            );

            System.out.print("\nEnter your given name: ");
            fname = input.nextLine();
            System.out.print("Enter your middle name: ");
            mname = input.nextLine();
            System.out.print("Enter your surname: ");
            lname = input.nextLine();
            System.out.print("Enter username: ");
            username = input.nextLine();
            boolean usernameTaken = isUsernameTaken(username);
            if (usernameTaken) {
                System.out.print("\n*Username is already taken. Please choose another one.");
                System.out.print("Press Enter to continue...");
                input.nextLine();
                BankSystem.clearConsole(); //TODO: delete this
                continue;
            }

            System.out.print("Enter password: ");
            password = input.nextLine();
            System.out.print("Enter email: ");
            email = input.nextLine();
            System.out.print("Enter phone: ");
            phone = input.nextLine();
            System.out.print("\nDo you want to enable 2FA?(Y/N): ");
            enable2FA = input.next().charAt(0);
            System.out.print(
                    """

                            Pick account type:
                            1. Savings Account
                            2. Credit Account"""
            );
            System.out.print("\nChoose your account type: ");
            acctype = input.nextInt();

            switch (acctype) {
                case 1:
                    accounttype = "Savings Account";
                    break;
                case 2:
                    accounttype = "Credit Account";
                    break;
                default:
                    System.out.print("*Invalid choice. Please select a valid option.");
                    continue;
            }

            boolean registrationSuccess = BankSystem.createUser(fname, mname, lname,username, password, email, phone, enable2FA, accounttype);
            if (registrationSuccess) {
                System.out.print(
                        """
                                Registration successful!
                                Press Enter to continue..."""
                );
                input.nextLine();
                break;
            } else {
                System.out.print("*Registration failed. Please try again.");
            }
        }
    }

    public static long generateUserID() {
        String timeString = new SimpleDateFormat("YYYYMMDDHHmm").format(date);
        String lastTwoDigitsOfTime = timeString.substring(timeString.length() - 2);

        String userID = timeString + lastTwoDigitsOfTime;
        return Long.parseLong(userID);
    }

    public static boolean isUsernameTaken(String username) {
        //CONVERT: List -> Database
        return BankSystem.users.stream()
                .anyMatch(user -> Objects.equals(getUsername(), username));
    }

    public static void changePassword(String username) {
        //CONVERT: List -> Database
        for (User user : BankSystem.users) {
            if (Objects.equals(getUsername(), username)) {
                for (Profile profile : user.userProfile) {
                    if (profile.get2FAStatus())
                    {
                        System.out.print("\nSending an OTP for 2 Factor Authentication.");
                        SecuritySystem.sendOTP();

                        System.out.print("\nEnter your OTP: ");
                        String inputOTP = input.nextLine();

                        if (!SecuritySystem.verifyOTP(inputOTP))
                        {
                            System.out.print("\n*Incorrect OTP. Timeout for 30 seconds...");
                            try {
                                Thread.sleep(30000);
                            } catch (InterruptedException e) {
                                System.err.print("\n" + e.getMessage());
                            }
                            return;
                        }
                    }

                    String password = input.nextLine();

                    user.setPassword(SecuritySystem.encrypt(password));
                    String decryptPass = SecuritySystem.decrypt(user.password);
                    System.out.print("Password changed to " + decryptPass + " successfully.");
                    BankSystem.saveDataToFile();
                }
            }
        }
    }

    public static void changeEmail(String username) {
        //CONVERT: List -> Database
        for (User user : BankSystem.users)
        {
            if (Objects.equals(getUsername(), username))
            {
                for (Profile profile : user.userProfile)
                {
                    if (profile.get2FAStatus())
                    {
                        System.out.print("\nSending an OTP for 2 Factor Authentication.");
                        SecuritySystem.sendOTP();
                        System.out.print("\nEnter your OTP: ");
                        String inputOTP = input.nextLine();

                        if (!SecuritySystem.verifyOTP(inputOTP))
                        {
                            System.out.print("\n*Incorrect OTP. Timeout for 30 seconds...");
                            try {
                                Thread.sleep(30000);
                            } catch (InterruptedException e) {
                                System.err.print("\n" + e.getMessage());
                            }
                            return;
                        }
                    }

                    String email = input.nextLine();

                    profile.setEmail(email);
                    System.out.print("Email changed to " + profile.getEmail() + " successfully.");
                    BankSystem.saveDataToFile();
                }
            }
        }
    }

    public static void changePhoneNum(String username) {
        //CONVERT: List -> Database
        for (User user : BankSystem.users)
        {
            if (Objects.equals(getUsername(), username))
            {
                for (Profile profile : user.userProfile)
                {
                    if (profile.get2FAStatus())
                    {
                        System.out.print("\nSending an OTP for 2 Factor Authentication.");
                        SecuritySystem.sendOTP();

                        System.out.print("\nEnter your OTP: ");
                        String inputOTP = input.nextLine();

                        if (!SecuritySystem.verifyOTP(inputOTP))
                        {
                            System.out.print("\n*Incorrect OTP. Timeout for 30 seconds...");
                            try {
                                Thread.sleep(30000);
                            } catch (InterruptedException e) {
                                System.err.print("\n" + e.getMessage());
                            }
                            return;
                        }
                    }

                    String phoneNum = input.nextLine();

                    profile.setPhoneNumber(phoneNum);
                    System.out.print("Phone changed to " + profile.getPhoneNumber() + " successfully.");
                    BankSystem.saveDataToFile();
                }
            }
        }
    }

    public static void changeUsername(String username) {
        //CONVERT: List -> Database
        for (User user : BankSystem.users)
        {
            if (Objects.equals(getUsername(), username))
            {
                for (Profile profile : user.userProfile)
                {
                    if (profile.get2FAStatus())
                    {
                        System.out.print("\nSending an OTP for 2 Factor Authentication.");
                        SecuritySystem.sendOTP();

                        System.out.print("\nEnter your OTP: ");
                        String inputOTP = input.nextLine();
                        input.nextLine();

                        if (!SecuritySystem.verifyOTP(inputOTP))
                        {
                            System.out.print("\n*Incorrect OTP. Timeout for 30 seconds...");
                            try {
                                Thread.sleep(30000);
                            } catch (InterruptedException e) {
                                System.err.print("\n" + e.getMessage());
                            }
                            return;
                        }
                    }

                    String newUsername = input.nextLine();

                    user.setUsername(newUsername);
                    System.out.print("Username changed to " + getUsername() + " successfully.");
                    BankSystem.saveDataToFile();
                }
            }
        }
    }

    public static void change2FAStatus(String username) {
        //CONVERT: List -> Database
        for (User user : BankSystem.users)
        {
            if (Objects.equals(getUsername(), username))
            {
                for (Profile profile : user.userProfile)
                {
                    if (profile.get2FAStatus())
                    {
                        System.out.print("\nSending an OTP for 2 Factor Authentication.");
                        SecuritySystem.sendOTP();

                        System.out.print("\nEnter your OTP: ");
                        String inputOTP = input.nextLine();
                        input.nextLine();

                        if (!SecuritySystem.verifyOTP(inputOTP))
                        {
                            System.out.print("\n*Incorrect OTP. Timeout for 30 seconds...");
                            try {
                                Thread.sleep(30000);
                            } catch (InterruptedException e) {
                                System.err.print("\n" + e.getMessage());
                            }
                            return;
                        }
                    }

                    char twoFA = input.next().charAt(0);

                    profile.set2FAStatus(SecuritySystem.enable2FA(twoFA));
                    String show2FAStatus = profile.get2FAStatus() ? "Enabled" : "Disabled";
                    System.out.print("Two Factor Authentication: " + show2FAStatus);
                    BankSystem.saveDataToFile();
                }
            }
        }
    }

    public static void askHelp(String username) {
        System.out.print("Enter your message: ");
        String message = input.nextLine();

        HelpAndResources.saveHelpAndResources(username, "Help", message, "");
    }

    @Deprecated
    protected void setUserProfile(List<Profile> userProfile) {
        this.userProfile = userProfile;
    }

    @Deprecated
    protected void setUserTransaction(List<Transaction> userTransaction) {
        this.userTransaction = userTransaction;
    }

    @Deprecated
    protected void setUserProductApplications(List<ProductApplication> userProductApplications) {
        this.userProductApplications = userProductApplications;
    }

    @Deprecated
    protected void setUserSessions(List<Session> userSessions) {
        this.userSessions = userSessions;
    }

    @Deprecated
    protected void setUserHelpAndResources(List<HelpAndResources> userHelpAndResources) {
        this.userHelpAndResources = userHelpAndResources;
    }

    @Deprecated
    protected void setUserDashboard(List<Dashboard> userDashboard) {
        this.userDashboard = userDashboard;
    }

    @Deprecated
    protected List<Profile> getUserProfile() {
        return userProfile;
    }

    @Deprecated
    protected List<Transaction> getUserTransaction() {
        return userTransaction;
    }

    @Deprecated
    protected List<ProductApplication> getUserProductApplications() {
        return userProductApplications;
    }

    @Deprecated
    protected List<Session> getUserSessions() {
        return userSessions;
    }

    @Deprecated
    protected List<HelpAndResources> getUserHelpAndResources() {
        return userHelpAndResources;
    }

    @Deprecated
    protected List<Dashboard> getUserDashboard() {
        return userDashboard;
    }


    public static boolean isAdmin() { //CONVERT Database
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public static boolean isCustomerService() { //CONVERT Database
        return isCustomerService;
    }

    public void setCustomerService(boolean customerService) {
        isCustomerService = customerService;
    }
}
