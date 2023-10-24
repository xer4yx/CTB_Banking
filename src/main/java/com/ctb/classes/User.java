package com.ctb.classes;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class User {
    private final Scanner userInput = new Scanner(System.in);
    private boolean isAdmin;
    private String userID;
    private String name;
    private String username;
    private String password;
    private String productType;
    private boolean isCustomerService;
    private double balance;
    private List<Profile> userProfile = new LinkedList<>();
    public List<Transaction> userTransaction = new LinkedList<>();
    public List<ProductApplication> userProductApplications = new LinkedList<>();
    public List<Session> userSessions = new LinkedList<>();
    public List<HelpAndResources> userHelpAndResources = new LinkedList<>();
    private List<Dashboard> userDashboard = new LinkedList<>();

    /*----------------------Constructor Methods----------------------*/
    public User() {}

    User(String name, String username, String productType) {
        this.name = name;
        this.username = username;
        this.productType = productType;
    }

    /*----------------------Setter Methods----------------------*/
    public void setUserID(String userID) {this.userID =  userID;}
    public void setName(String name) {this.name = name;}
    public void setUsername(String username) {this.username = username;}
    public void setPassword(String password) {this.password = password;}
    public void setProductType(String productType) {this.productType = productType;}
    public void setCSStatus(boolean isCustomerService) {this.isCustomerService = isCustomerService;}
    public void setAdminStatus(boolean isAdmin) {this.isAdmin = isAdmin;}
    public void setBalance(double balance) {this.balance += balance;}

    /*----------------------Getter Methods----------------------*/
    public String getUserID() {return this.userID;}
    public String getName() {return this.name;}
    public static String getUsername() {return this.username;}
    public String getPassword() {return this.password;}
    public String getProductType() {return this.productType;}
    public double getBalance() {
        return balance;
    }

    /*----------------------Class Methods----------------------*/
    public void displayUserSettings(String username) {
        while (true)
        {
            System.out.println(
                    "\n╭────────────────────────╮\n"
                    + "│     User Settings      │\n"
                    + "├────────────────────────┤\n"
                    + "│ 1. Manage Account      │\n"
                    + "│ 2. Back to Dashboard   │\n"
                    + "╰────────────────────────╯\n");
            System.out.print("Enter: ");
            int settingChoice = userInput.nextInt();
            switch (settingChoice)
            {
                case 1:
                    handleSettings(username);
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        }
    }

    public void displayActivityLog(String username) {
        cout << " " << endl;
        cout << "╭───────────────────────────╮" << endl;
        cout << "│      Activity Log         │" << endl;
        cout << "├───────────────────────────┤" << endl;
        cout << "│ 1. Transaction History    │" << endl;
        cout << "│ 2. Session History        │" << endl;
        cout << "│ 3. Help History           │" << endl;
        cout << "╰───────────────────────────╯" << endl;
        cout << " " << endl;

        int achoice;
        cout << "Enter: ";
        cin >> achoice;

        switch (achoice)
        {
            case 1:
        ::system("cls");
                displayTransactionHistory(username);
                break;
            case 2:
                displaySessions(username);
                break;
            case 3:
        ::system("cls");
                displayHelpHistory(username);
                break;
            default:
                cout << "*Invalid choice. Please select a valid option." << endl;
                break;
        }
    }

    public void displaySessions(String username) {
        for (const User &user : users)
        {
            if (user.username == username)
            {
            ::system("cls");
                cout << " " << endl;
                cout << "╔════════════════════════════════════════════╗    " << endl;
                cout << "║               Session History              ║    " << endl;
                cout << "╚════════════════════════════════════════════╝    " << endl;
                cout << " User: " << user.username << endl;
                cout << "──────────────────────────────────────────────" << endl;
                for (const Session &session : user.sessions)
                {
                    cout << "Session ID: " << session.sessionID << endl;
                    cout << "Username: " << session.username << endl;
                    cout << "Timestamp: " << ctime(&session.timestamp);
                    cout << "──────────────────────────────────────────────" << endl;
                }
            }
        }
    }

    public void handleSettings(String username) {
        String newPassword, newEmail, newPhoneNumber, newUsername;
        char new2FA;
        System.out.println(
                  "\n╔═════════════════════════════════════╗    "
                + "\n║           Manage Account            ║   "
                + "\n╠═════════════════════════════════════╣   "
                + "\n║  1. Change Password                 ║"
                + "\n║  2. Change Email                    ║"
                + "\n║  3. Change Phone                    ║"
                + "\n║  4. Change Username                 ║"
                + "\n║  5. Enable/Disable 2FA              ║"
                + "\n║  6. Show Activity Log               ║"
                + "\n║  7. Back to Profile                 ║"
                + "\n╚═════════════════════════════════════╝");
        System.out.print("\nEnter: ");
        int accChoice = userInput.nextInt();
        switch (accChoice)
        {
            case 1:
                System.out.print("\nEnter new password: ");
                newPassword = userInput.nextLine();
                changePassword(username, newPassword);
                break;

            case 2:
                System.out.print("\nEnter new email: ");
                newEmail = userInput.nextLine();
                changeEmail(username, newEmail);
                break;

            case 3:
                System.out.print("\nEnter new phone: ");
                newPhoneNumber = userInput.nextLine();
                changePhoneNum(username, newPhoneNumber);
                break;

            case 4:
                System.out.print("\nEnter new username: ");
                newUsername = userInput.nextLine();
                changeUsername(username, newUsername);
                break;

            case 5:
                System.out.print("\nDo you want to enable 2FA?(Y/N): ");
                new2FA = userInput.next().charAt(0);
                change2FAStatus(username, new2FA);
                break;

            case 6:
                displayActivityLog(username);
                break;

            case 7:
                return;

            default:
                System.out.println("*Invalid choice. Please select a valid option.");
                break;
        }

    }

    public void processDeposit(String username) {
        double depositAmount;
        cout << "\nEnter the amount to deposit: $";
        cin >> depositAmount;
        cin.ignore(); // Clear the newline character

        if (depositAmount <= 0.0)
        {
            cout << "*Invalid deposit amount. Please enter a positive amount." << endl;
            return;
        }

        if (Transaction.depositFunds(username, depositAmount))
        {
            cout << " " << endl;
            cout << "Deposit of $" << depositAmount << " successful." << endl;
        }
        else
        {
            cout << "*Deposit failed. Please try again." << endl;
        }
        cout << "\nPress Enter to continue...";
        cin.get();
    }

    public void processWithdrawal(String username) {
        double withdrawAmount;
        cout << "\nEnter the amount to withdraw: $";
        cin >> withdrawAmount;
        cin.ignore(); // Clear the newline character

        if (withdrawAmount <= 0.0)
        {
            cout << "*Invalid withdrawal amount. Please enter a positive amount." << endl;
            return;
        }

        if (Transaction.withdrawFunds(username, withdrawAmount))
        {
            cout << "\nWithdrawal of $" << withdrawAmount << " successful." << endl;
        }
        else
        {
            cout << "*Withdrawal failed. Please try again." << endl;
        }
        cout << "\nPress Enter to continue...";
        cin.get();
    }

    public void processPurchase(String username) {
        double purchaseAmount;
        String purchaseDescription;
        cout << "\nEnter the purchase amount: $";
        cin >> purchaseAmount;
        cout << "\nEnter the purchase description: ";
        cin >> purchaseDescription;
        if (cin.fail())
        {
            cin.clear();
            cin.ignore(numeric_limits<streamsize>::max(), '\n');
            cout << "*Invalid amount. Please enter a valid number." << endl;
        }
        cin.ignore(); // Clear the newline character
        if (purchaseAmount <= 0.0)
        {
            cout << "*Invalid transaction amount. Please enter a positive amount." << endl;
        }
        if (Transaction.makePurchase(username, purchaseAmount, purchaseDescription))
        {
            cout << endl;
        }
        else
        {
            cout << endl;
        }
        cout << " " << endl;
        cout << "Press Enter to continue...";
        cin.get();
    }

    public void processBills(String username) {
        double billAmount;
        String billdescription;
        cout << "\nEnter the bill amount: $";
        cin >> billAmount;
        cout << "\nEnter the bill description: ";
        cin >> billdescription;
        if (cin.fail())
        {
            cin.clear();
            cin.ignore(numeric_limits<streamsize>::max(), '\n');
            cout << "*Invalid amount. Please enter a valid number." << endl;
        }
        cin.ignore(); // Clear the newline character
        if (billAmount <= 0.0)
        {
            cout << "*Invalid amount. Please enter a positive amount." << endl;
        }
        if (Transaction.payBills(username, billAmount, billdescription))
        {
            cout << endl;
        }
        else
        {
            cout << endl;
        }

        cout << " " << endl;
        cout << "Press Enter to continue...";
        cin.get();
    }

    public static void applyProduct() {
        String name, username, password, email, phone, accounttype;
        int acctype;
        char enable2FA;
        while (true)
        {
            cout << " " << endl;
            cout << "╔═════════════════════════════════════╗    " << endl;
            cout << "║       Product Application           ║   " << endl;
            cout << "╚═════════════════════════════════════╝    " << endl;
            cout << " " << endl;
            cout << "Enter your full name: ";
            getline(cin, name);

            cout << "Enter username: ";
            getline(cin, username);

            // Check if the username is already taken
            bool usernameTaken = isUsernameTaken(username);
            if (usernameTaken)
            {
                cout << "\n*Username is already taken. Please choose another one." << endl;
                cout << " " << endl;
                cout << "Press Enter to continue...";
                cin.get();
            ::system("cls");
                continue;
            }

            cout << "Enter password: ";
            cin >> password;

            cout << "Enter email: ";
            cin >> email;

            cout << "Enter phone: ";
            cin >> phone;

            cout << "\nDo you want to enable 2FA?(Y/N): ";
            cin >> enable2FA;

            cout << "\nPick account type: " << endl;
            cout << "1. Savings Account" << endl;
            cout << "2. Credit Account" << endl;

            cout << "\nChoose your account type: ";
            cin >> acctype;

            switch (acctype)
            {
                case 1:
                    accounttype = "Savings Account";
                    break;
                case 2:
                    accounttype = "Credit Account";
                    break;
                default:
                    cout << "*Invalid choice. Please select a valid option." << endl;
                    continue;
            }

            // Create a new user account
            bool registrationSuccess = BankSystem.createUser(name, username, password, email, phone, enable2FA, accounttype);
            if (registrationSuccess)
            {
                cout << "Registration successful!" << endl;
                cout << " " << endl;
                cout << "Press Enter to continue...";
                cin.get();
                break;
            }
            else
            {
                cout << "*Registration failed. Please try again." << endl;
                continue;
            }
        }
    }

    public static String generateUserID() {
        // Implement your logic to generate a unique transaction ID
        // Example: You can use a combination of timestamp and a random number
        return "USR" + to_string(time(nullptr)) + to_string(rand());
    }

    public static boolean isUsernameTaken(String username) {
        return std::any_of(users.begin(), users.end(),
                           [&username](const User &user)
        { return user.username == username; });
    }

    public static void changePassword(String username, String password) {
        for (User &user : users)
        {
            if (user.username == username)
            {
                for (Profile &profile : user.profiles)
                {
                    if (profile.isTwoFactorEnabled)
                    {
                        cout << "\nSending an OTP for 2 Factor Authentication." << endl;
                        system.sendOTP();

                        string inputOTP;
                        cout << "\nEnter your OTP: ";
                        cin >> inputOTP;
                        cin.ignore();

                        if (!system.verifyOTP(inputOTP))
                        {
                            cout << "\n*Incorrect OTP. Timeout for 30 seconds..." << endl;
                            sleep_for(seconds(30));
                            return;
                        }
                    }
                    user.password = SecuritySys::encryptPass(password);
                    string decrypass = SecuritySys::decryptPass(user.password);
                    cout << "Password changed to " << decrypass << " successfully." << endl;
                    BankSystem.saveDataToFile();
                }
            }
        }
    }

    public static void changeEmail(String username, String email) {
        for (User &user : users)
        {
            if (user.username == username)
            {
                for (Profile &profile : user.profiles)
                {
                    if (profile.isTwoFactorEnabled)
                    {
                        cout << "\nSending an OTP for 2 Factor Authentication." << endl;
                        system.sendOTP();

                        string inputOTP;
                        cout << "\nEnter your OTP: ";
                        cin >> inputOTP;
                        cin.ignore();

                        if (!system.verifyOTP(inputOTP))
                        {
                            cout << "\n*Incorrect OTP. Timeout for 30 seconds..." << endl;
                            sleep_for(seconds(30));
                            return;
                        }
                    }
                    profile.email = email;
                    cout << "Email changed to " << profile.email << " successfully." << endl;
                    BankSystem.saveDataToFile();
                }
            }
        }
    }

    public static void changePhoneNum(String username, String phoneNum) {
        for (User &user : users)
        {
            if (user.username == username)
            {
                for (Profile &profile : user.profiles)
                {
                    if (profile.isTwoFactorEnabled)
                    {
                        cout << "\nSending an OTP for 2 Factor Authentication." << endl;
                        system.sendOTP();

                        string inputOTP;
                        cout << "\nEnter your OTP: ";
                        cin >> inputOTP;
                        cin.ignore();

                        if (!system.verifyOTP(inputOTP))
                        {
                            cout << "\n*Incorrect OTP. Timeout for 30 seconds..." << endl;
                            sleep_for(seconds(30));
                            return;
                        }
                    }
                    profile.phone = phone;
                    cout << "Phone changed to " << profile.phone << " successfully." << endl;
                    BankSystem.saveDataToFile();
                }
            }
        }
    }

    public static void changeUsername(String username, String newUsername) {
        for (User &user : users)
        {
            if (user.username == username)
            {
                for (Profile &profile : user.profiles)
                {
                    if (profile.isTwoFactorEnabled)
                    {
                        cout << "\nSending an OTP for 2 Factor Authentication." << endl;
                        system.sendOTP();

                        string inputOTP;
                        cout << "\nEnter your OTP: ";
                        cin >> inputOTP;
                        cin.ignore();

                        if (!system.verifyOTP(inputOTP))
                        {
                            cout << "\n*Incorrect OTP. Timeout for 30 seconds..." << endl;
                            sleep_for(seconds(30));
                            return;
                        }
                    }
                    user.username = newusername;
                    cout << "Username changed to " << user.username << " successfully." << endl;
                    BankSystem.saveDataToFile();
                }
            }
        }
    }

    public static void change2FAStatus(String username, char twoFA) {
        for (User &user : users)
        {
            if (user.username == username)
            {
                for (Profile &profile : user.profiles)
                {
                    if (profile.isTwoFactorEnabled)
                    {
                        cout << "\nSending an OTP for 2 Factor Authentication." << endl;
                        system.sendOTP();

                        string inputOTP;
                        cout << "\nEnter your OTP: ";
                        cin >> inputOTP;
                        cin.ignore();

                        if (!system.verifyOTP(inputOTP))
                        {
                            cout << "\n*Incorrect OTP. Timeout for 30 seconds..." << endl;
                            sleep_for(seconds(30));
                            return;
                        }
                    }
                    profile.isTwoFactorEnabled = SecuritySys::enable2FA(twoFA);
                    string show2FAStatus = profile.isTwoFactorEnabled ? "Enabled" : "Disabled";
                    cout << "Two Factor Authentication: " << show2FAStatus << endl;
                    BankSystem.saveDataToFile();
                }
            }
        }
    }

    public void askHelp(String username) {
        String message;
        cout << "Enter your message: ";

        // Clear any remaining newline characters from the input stream
        cin.ignore();

        getline(cin, message);

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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isCustomerService() {
        return isCustomerService;
    }

    public void setCustomerService(boolean customerService) {
        isCustomerService = customerService;
    }
}
