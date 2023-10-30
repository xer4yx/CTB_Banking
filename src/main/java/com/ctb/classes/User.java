package com.ctb.classes;

import java.util.*;

public class User {
    private static final Scanner input = new Scanner(System.in);
    private static final Calendar calendar = Calendar.getInstance();
    private static final Random rand = new Random();
    private boolean isAdmin;
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

    User(String name, String username, String productType) {
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
    public void displayUserSettings(String username) {
        while (true)
        {
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
            switch (settingChoice)
            {
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

    public void displayActivityLog(String username) {
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

        System.out.print("Enter: ");
        int choice = input.nextInt();

        switch (choice)
        {
            case 1:
                BankSystem.clearConsole();
                Display.displayTransactionHistory(username);
                break;
            case 2:
                displaySessions(username);
                break;
            case 3:
                BankSystem.clearConsole();
                CustomerService.displayHelpHistory(username);
                break;
            default:
                System.out.print("*Invalid choice. Please select a valid option.");
                break;
        }
    }

    public void displaySessions(String username) {
        for (final User user : BankSystem.users)
        {
            if (Objects.equals(User.username, username))
            {
            BankSystem.clearConsole();
                System.out.print(
                        "\n╔════════════════════════════════════════════╗" +
                        "\n║               Session History              ║" +
                        "\n╚════════════════════════════════════════════╝" +
                        "\n User: " + User.username +
                        "\n──────────────────────────────────────────────"
                );
                for (final Session session : user.userSessions)
                {
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

    public void handleSettings(String username) {
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
        switch (accChoice)
        {
            case 1:
                System.out.print("\nEnter new password: ");
                newPassword = input.nextLine();
                changePassword(username, newPassword);
                break;

            case 2:
                System.out.print("\nEnter new email: ");
                newEmail = input.nextLine();
                changeEmail(username, newEmail);
                break;

            case 3:
                System.out.print("\nEnter new phone: ");
                newPhoneNumber = input.nextLine();
                changePhoneNum(username, newPhoneNumber);
                break;

            case 4:
                System.out.print("\nEnter new username: ");
                newUsername = input.nextLine();
                changeUsername(username, newUsername);
                break;

            case 5:
                System.out.print("\nDo you want to enable 2FA?(Y/N): ");
                new2FA = input.next().charAt(0);
                change2FAStatus(username, new2FA);
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

    public void processDeposit(String username) {
        System.out.print("\nEnter the amount to deposit: $");
        double depositAmount = input.nextDouble();
        input.nextLine();
        if (depositAmount <= 0.0)
        {
            System.out.print("*Invalid deposit amount. Please enter a positive amount.");
            return;
        }

        if (Transaction.depositFunds(username, depositAmount))
        {
            System.out.print("Deposit of $" + depositAmount + " successful.");
        }
        else
        {
            System.out.print("*Deposit failed. Please try again.");
        }
        System.out.print("\nPress Enter to continue...");
        input.nextLine();
    }

    public void processWithdrawal(String username) {
        System.out.print("\nEnter the amount to withdraw: $");
        double withdrawAmount = input.nextDouble(); // Clear the newline character

        if (withdrawAmount <= 0.0)
        {
            System.out.print("*Invalid withdrawal amount. Please enter a positive amount.");
            return;
        }

        if (Transaction.withdrawFunds(username, withdrawAmount))
        {
            System.out.print("\nWithdrawal of $" + withdrawAmount + " successful.");
        }
        else
        {
            System.out.print("*Withdrawal failed. Please try again.");
        }
        System.out.print("\nPress Enter to continue...");
        input.nextLine();
    }

    public void processPurchase(String username) {
        System.out.print("\nEnter the purchase amount: $");
        double purchaseAmount = input.nextDouble();
        System.out.print("\nEnter the purchase description: ");
        String purchaseDescription = input.nextLine();
        if (input.hasNextDouble())
        {
            input.nextDouble();
            System.out.print("*Invalid amount. Please enter a valid number.");
        }
        input.nextLine();
        if (purchaseAmount <= 0.0)
        {
            System.out.print("*Invalid transaction amount. Please enter a positive amount.");
        }
        if (Transaction.makePurchase(username, purchaseAmount, purchaseDescription))
        {
            System.out.print("\nPurchase of $" + purchaseAmount + " successful.");
        }
        else
        {
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
        }

        if (Transaction.payBills(username, billAmount, billDescription)) {
            System.out.print("\nPayment of $" + billAmount + " successful.");
        } else {
            System.out.print("*Payment failed. Please try again.");
        }

        System.out.print("\nPress Enter to continue...");
        input.nextLine();
    }

    public static void applyProduct() {
        String name, username, password, email, phone, accounttype;
        int acctype;
        char enable2FA;
        while (true)
        {
            System.out.print(
                    """

                            ╔═════════════════════════════════════╗
                            ║       Product Application           ║
                            ╚═════════════════════════════════════╝"""
            );
            System.out.print("Enter your full name: ");
            name = input.nextLine();

            System.out.print("Enter username: ");
            username = input.nextLine();

            // Check if the username is already taken
            boolean usernameTaken = isUsernameTaken(username);
            if (usernameTaken)
            {
                System.out.print("\n*Username is already taken. Please choose another one.");
                System.out.print("Press Enter to continue...");
                input.nextLine();
            BankSystem.clearConsole();
                continue;
            }

            System.out.print("Enter password: ");
            password = input.nextLine();

            System.out.print("Enter password: ");
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

            switch (acctype)
            {
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

            boolean registrationSuccess = BankSystem.createUser(name, username, password, email, phone, enable2FA, accounttype);
            if (registrationSuccess)
            {
                System.out.print(
                        """
                                Registration successful!
                                Press Enter to continue..."""
                );
                input.nextLine();
                break;
            }
            else
            {
                System.out.print("*Registration failed. Please try again.");
            }
        }
    }

    public static String generateUserID() {
        long time = calendar.getTimeInMillis();
        int randomNumber = rand.nextInt();
        String timeString = Long.toString(time);
        String randomNumberString = Integer.toString(randomNumber);
        return "USR" + timeString + randomNumberString;
    }


    public static boolean isUsernameTaken(String username) {
        return BankSystem.users.stream()
                .anyMatch(user -> Objects.equals(getUsername(), username));
    }

    public static void changePassword(String username, String password) {
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
                                System.err.print(e.getMessage());
                            }
                            return;
                        }
                    }
                    user.setPassword(SecuritySystem.encrypt(password));
                    String decryptPass = SecuritySystem.decrypt(user.password);
                    System.out.print("Password changed to " + decryptPass + " successfully.");
                    BankSystem.saveDataToFile();
                }
            }
        }
    }

    public static void changeEmail(String username, String email) {
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
                                System.err.print(e.getMessage());
                            }
                            return;
                        }
                    }
                    profile.setEmail(email);
                    System.out.print("Email changed to " + profile.getEmail() + " successfully.");
                    BankSystem.saveDataToFile();
                }
            }
        }
    }

    public static void changePhoneNum(String username, String phoneNum) {
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
                                System.err.print(e.getMessage());
                            }
                            return;
                        }
                    }
                    profile.setPhoneNumber(phoneNum);
                    System.out.print("Phone changed to " + profile.getPhoneNumber() + " successfully.");
                    BankSystem.saveDataToFile();
                }
            }
        }
    }

    public static void changeUsername(String username, String newUsername) {
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
                                System.err.print(e.getMessage());
                            }
                            return;
                        }
                    }
                    user.setUsername(newUsername);
                    System.out.print("Username changed to " + getUsername() + " successfully.");
                    BankSystem.saveDataToFile();
                }
            }
        }
    }

    public static void change2FAStatus(String username, char twoFA) {
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
                                System.err.print(e.getMessage());
                            }
                            return;
                        }
                    }
                    profile.set2FAStatus(SecuritySystem.enable2FA(twoFA));
                    String show2FAStatus = profile.get2FAStatus() ? "Enabled" : "Disabled";
                    System.out.print("Two Factor Authentication: " + show2FAStatus);
                    BankSystem.saveDataToFile();
                }
            }
        }
    }

    public void askHelp(String username) {
        System.out.print("Enter your message: ");
        String message = input.nextLine();

        HelpAndResources.saveHelpAndResources(username, "Help", message, "");
    }

    protected void setUserProfile(List<Profile> userProfile) {
        this.userProfile = userProfile;
    }

    protected void setUserTransaction(List<Transaction> userTransaction) {
        this.userTransaction = userTransaction;
    }

    protected void setUserProductApplications(List<ProductApplication> userProductApplications) {
        this.userProductApplications = userProductApplications;
    }

    protected void setUserSessions(List<Session> userSessions) {
        this.userSessions = userSessions;
    }

    protected void setUserHelpAndResources(List<HelpAndResources> userHelpAndResources) {
        this.userHelpAndResources = userHelpAndResources;
    }

    protected void setUserDashboard(List<Dashboard> userDashboard) {
        this.userDashboard = userDashboard;
    }

    protected List<Profile> getUserProfile() {
        return userProfile;
    }

    protected List<Transaction> getUserTransaction() {
        return userTransaction;
    }

    protected List<ProductApplication> getUserProductApplications() {
        return userProductApplications;
    }

    protected List<Session> getUserSessions() {
        return userSessions;
    }

    protected List<HelpAndResources> getUserHelpAndResources() {
        return userHelpAndResources;
    }

    protected List<Dashboard> getUserDashboard() {
        return userDashboard;
    }

    public static boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public static boolean isCustomerService() {
        return isCustomerService;
    }

    public void setCustomerService(boolean customerService) {
        isCustomerService = customerService;
    }
}
