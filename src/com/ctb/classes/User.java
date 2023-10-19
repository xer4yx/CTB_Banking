package com.ctb.classes;

import com.ctb.interfaces.UserInterface;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class User implements UserInterface {
    private Scanner choice = new Scanner(System.in);
    private String userID;
    private String name;
    private String username;
    private String password;
    private String productType;
    private boolean isCustomerService;
    private boolean isAdmin;
    private double balance;
    private List<Profile> userProfile = new LinkedList<>();
    private List<Transaction> userTransaction = new LinkedList<>();
    private List<ProductApplication> userProductApplications = new LinkedList<>();
    private List<Session> userSessions = new LinkedList<>();
    private List<HelpAndResources> userHelpAndResources = new LinkedList<>();
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
    public String getUsername() {return this.username;}
    public String getPassword() {return this.password;}
    public String getProductType() {return this.productType;}


    /*----------------------Class Methods----------------------*/
    public void displaySettings(String username) {
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
            int settingChoice = choice.nextInt();
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

    @Override
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

    @Override
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

    @Override
    public void displayDashboard(String username) {
        for (const User &user : users)
        {
            if (user.username == username)
            {
                if (isadmin(username))
                {
                ::system("cls");
                    cout << "╔═════════════════════════════════════╗    " << endl;
                    cout << "║            Administrator            ║    " << endl;
                    cout << "╚═════════════════════════════════════╝    " << endl;
                    cout << "                                           " << endl;
                    cout << "╔═════════════════════════════════════╗    " << endl;
                    cout << "║         Dashboard Options:          ║     " << endl;
                    cout << "╠═════════════════════════════════════╣    " << endl;
                    cout << "║  1. Manage Users                    ║     " << endl;
                    cout << "║  2. Help & Resources                ║     " << endl;
                    cout << "║  3. Logout                          ║     " << endl;
                    cout << "╚═════════════════════════════════════╝" << endl;
                    cout << " " << endl;
                    cout << "Enter your choice: ";
                }
                else if (iscustomerservice(username))
                {
                ::system("cls");
                    cout << "╔═════════════════════════════════════╗    " << endl;
                    cout << "║          Customer Service           ║    " << endl;
                    cout << "╚═════════════════════════════════════╝    " << endl;
                    cout << "                                           " << endl;
                    cout << "╔═════════════════════════════════════╗    " << endl;
                    cout << "║         Dashboard Options:          ║     " << endl;
                    cout << "╠═════════════════════════════════════╣    " << endl;
                    cout << "║  1. Messages                        ║     " << endl;
                    cout << "║  2. Logout                          ║     " << endl;
                    cout << "╚═════════════════════════════════════╝" << endl;
                    cout << " " << endl;
                    cout << "Enter your choice: ";
                }
                else
                {
                ::system("cls");
                    SetConsoleOutputCP(CP_UTF8);
                    cout << " " << endl;
                    cout << "╭─────────────────────────────────────╮" << endl;
                    cout << "│         CENTRAL TRUST BANK          │" << endl;
                    cout << "╰─────────────────────────────────────╯" << endl;
                    cout << " " << endl;
                    cout << " Welcome " << user.name << "!" << endl;
                    cout << "  " << endl;
                    cout << " Current Balance: $" << getCurrentBalance(username) << endl;
                    cout << "                                           " << endl;
                    cout << "╔═════════════════════════════════════╗    " << endl;
                    cout << "║         Dashboard Options:          ║     " << endl;
                    cout << "╠═════════════════════════════════════╣    " << endl;
                    cout << "║  1. Transaction Center              ║     " << endl;
                    cout << "║  2. User Profile                    ║     " << endl;
                    cout << "║  3. Data Analytics Dashboard        ║     " << endl;
                    cout << "║  4. Help & Resources                ║     " << endl;
                    cout << "║  5. Logout                          ║     " << endl;
                    cout << "╚═════════════════════════════════════╝" << endl;
                    cout << " " << endl;
                    cout << "Enter your choice: ";
                }
            }
        }
    }

    @Override
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
        int accChoice = choice.nextInt();
        switch (accChoice)
        {
            case 1:
                System.out.print("\nEnter new password: ");
                newPassword = choice.nextLine();
                changePassword(username, newPassword);
                break;

            case 2:
                System.out.print("\nEnter new email: ");
                newEmail = choice.nextLine();
                changeEmail(username, newEmail);
                break;

            case 3:
                System.out.print("\nEnter new phone: ");
                newPhoneNumber = choice.nextLine();
                changePhoneNum(username, newPhoneNumber);
                break;

            case 4:
                System.out.print("\nEnter new username: ");
                newUsername = choice.nextLine();
                changeUsername(username, newUsername);
                break;

            case 5:
                System.out.print("\nDo you want to enable 2FA?(Y/N): ");
                new2FA = choice.next().charAt(0);
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

    @Override
    public void processDeposit(String username) {

    }

    @Override
    public void processWithdrawal(String username) {

    }

    @Override
    public void processPurchase(String username) {

    }

    @Override
    public void processBills(String username) {

    }

    @Override
    public void applyProduct() {

    }

    @Override
    public String generateUserID() {
        return null;
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return false;
    }

    @Override
    public void changePassword(String username, String password) {

    }

    @Override
    public void changeEmail(String username, String email) {

    }

    @Override
    public void changePhoneNum(String username, String phoneNum) {

    }

    @Override
    public void changeUsername(String username, String newUsername) {

    }

    @Override
    public void change2FAStatus(String username, char twoFA) {

    }

    @Override
    public void askHelp(String username) {

    }

    public double getBalance() {
        return balance;
    }
}
