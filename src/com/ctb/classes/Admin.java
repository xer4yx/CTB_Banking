package com.ctb.classes;

import java.util.Objects;
import java.util.Scanner;

class Admin extends User{
    private String adminID;
    private String name;
    private String username;
    private String password;
    private final Scanner input = new Scanner(System.in);

    /*----------------------Setter Methods----------------------*/
    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    /*----------------------Getter Methods----------------------*/
    public String getAdminID() {return adminID;}

    /*----------------------Class Methods----------------------*/
    public boolean deleteUserByUsername(String userToDelete) {
        var userIterator = BankSystem.getUsers().iterator();
        while(userIterator.hasNext()) {
            User user = userIterator.next();
            if (user.getUsername().equals(userToDelete)) {
                userIterator.remove();
                BankSystem.saveDataToFile(); // Save data after deletion
                return true;
            }
        }
        return false;
    }

    public void handleManageUsers(String username) {
        BankSystem.clearConsole();
        System.out.println(
                "\n╔═══════════════════════════════╗" +
                "\n║          Manage Users         ║" +
                "\n╠═══════════════════════════════╣" +
                "\n║  1. View Users Data           ║" +
                "\n║  2. Add Users                 ║" +
                "\n║  3. Delete Users              ║" +
                "\n║  4. Update Users              ║" +
                "\n║  5. Exit                      ║" +
                "\n╚═══════════════════════════════╝"
        );

        System.out.print("Enter your choice: ");
        int choice = input.nextInt();
        input.nextLine();
        switch (choice) {
            case 1:
                displayAllUserData();
                break;
            case 2:
                User.applyProduct();
                break;
            case 3:
                deleteUser();
                break;
            case 4:
                updateUser();
                break;
            case 5:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    public void updateUser() {
        BankSystem.clearConsole();
        System.out.println(
                "\n╭────────────────────────────────────────────────────────────────╮" +
                "\n│                         Update User                            │" +
                "\n╰────────────────────────────────────────────────────────────────╯"
        );
        System.out.print("\nEnter the username of the user you want to update: ");
        String pickedUsername = input.nextLine();
        input.nextLine();
        handleSettings(pickedUsername);
    }

    public void deleteUser() {
        BankSystem.clearConsole();
        System.out.println(
                "\n╭────────────────────────────────────────────────────────────────╮" +
                "\n│                         Delete User                            │" +
                "\n╰────────────────────────────────────────────────────────────────╯"
        );
        System.out.print("\nEnter the username of the user to delete: ");
        String user = input.nextLine();
        input.nextLine();
        if (deleteUserByUsername(user)) {
            System.out.printf("User with username '%s' has been deleted.", user);
        } else {
            System.out.printf("User with username '%s' not found.", user);
        }
    }
    public void displayUserData(String username) {
        for (final User user : BankSystem.getUsers())
        {
            if (Objects.equals(user.getUsername(), username))
            {
                System.out.println(
                        "\n──────────────────────────────────────────────────────────────────" +
                        "\n                          Information:                            " +
                        "\n──────────────────────────────────────────────────────────────────" +
                        "\n  User ID                : " + user.getUserID() +
                        "\n  Name                   : " + user.getName() +
                        "\n  Username               : " + user.getUsername() +
                        "\n  Is Admin               : " + (BankSystem.isAdmin(user.getUsername()) ? "Yes" : "No") +
                        "\n  Is Customer Service    : " + (BankSystem.isCustomerService(user.getUsername()) ? "Yes" : "No") +
                        "\n  Product Type           : " + user.getProductType() +
                        "\n  Balance                : " + user.getBalance()
                );

                System.out.println(
                        "\n──────────────────────────────────────────────────────────────────" +
                        "\n                           Profiles:                              " +
                        "\n──────────────────────────────────────────────────────────────────"
                );
                for (final Profile profile : user.getUserProfile())
                {
                    System.out.println(
                            " Email                  : " + profile.getEmail() +
                            "\n  Phone                  : " + profile.getPhoneNumber() +
                            "\n  Two-Factor Enabled     : " + (profile.get2FAStatus() ? "Yes" : "No")
                    );
                }

                System.out.println(
                        "\n──────────────────────────────────────────────────────────────────" +
                        "\n                      Transaction History:                        " +
                        "\n──────────────────────────────────────────────────────────────────"
                );
                for (final Transaction transaction : user.getUserTransaction())
                {
                    System.out.println(
                            "  Transaction ID         : " + transaction.getTransactionID() +
                            "\n  Transaction Type       : " + transaction.getTransactionType() +
                            "\n  Amount                 : " + transaction.getAmount() +
                            "\n  Timestamp              : " + transaction.getTimeStamp()
                    );
                }

                System.out.println(
                        "\n──────────────────────────────────────────────────────────────────" +
                        "\n                          Sessions:" +
                        "\n──────────────────────────────────────────────────────────────────"
                );

                for (final Session session : user.getUserSessions())
                {
                    System.out.println(
                            "  Session ID             : " + session.getSessionID() +
                            "\n  Username               : " + session.getUsername() +
                            "\n  Timestamp              : " + session.getTimeStamp()
                    );
                }

                System.out.println(

                );
                cout << "──────────────────────────────────────────────────────────────────" << endl;
                cout << "                     Product Applications:" << endl;
                cout << "──────────────────────────────────────────────────────────────────" << endl;
                for (final ProductApplication productApp : user.getUserProductApplications())
                {
                    System.out.println(

                    );
                    cout << "  Product Type           : " << productApp.producttype << endl;
                    cout << "  Product ID             : " << productApp.productID << endl;
                }

                System.out.println(

                );
                cout << "──────────────────────────────────────────────────────────────────" << endl;
                cout << "                      Help and Resources:" << endl;
                cout << "──────────────────────────────────────────────────────────────────" << endl;
                for (final HelpAndResources resources : user.getUserHelpAndResources())
                {
                    System.out.println(

                    );
                    cout << "  Help ID                : " << resources.helpID << endl;
                    cout << "  Type                   : " << resources.helpandresourcesType << endl;
                    cout << "  Description            : " << resources.helpandresourcesDescription << endl;
                    cout << "  Feedback               : " << resources.feedback << endl;
                    cout << "──────────────────────────────────────────────────────────────────" << endl;
                }

                return; // Exit the loop once the user is found and displayed
            }
        }
        System.out.printf("User with username '%s' not found.", username);
    }

    public void displayAllUserData() {
        BankSystem.clearConsole();
        cout << "╔════════════════════════════════════════════════════════╗     " << endl;
        cout << "║                   View Users Data                      ║     " << endl;
        cout << "╚════════════════════════════════════════════════════════╝     " << endl;
        cout << " " << endl;
        for (final User user : users)
        {
            cout << "┌────────────────────────────────────────────────────────┐" << endl;
            cout << "│                    Information:                        │" << endl;
            cout << "└────────────────────────────────────────────────────────┘" << endl;
            cout << "      User ID              : " << user.getUserID() << endl;
            cout << "      Name                 : " << user.getName() << endl;
            cout << "      Username             : " << user.getUsername() << endl;
            cout << "      Is Admin             : " << (BankSystem.isAdmin(user.getUsername()) ? "Yes" : "No") << endl;
            cout << "      Is Customer Service  : " << (BankSystem.isCustomerService(user.getUsername()) ? "Yes" : "No") << endl;
            cout << "      Product Type         : " << user.getProductType() << endl;
            cout << "      Balance              : " << user.getBalance() << endl;
            cout << " " << endl;
            cout << "────────────────────────────────────────────────────────" << endl;
            cout << "                      Profiles:                         " << endl;
            cout << "────────────────────────────────────────────────────────" << endl;
            for (final Profile profile : user.userProfile)
            {
                cout << "  Email                    : " << profile.getEmail() << endl;
                cout << "  Phone                    : " << profile.getPhoneNumber() << endl;
                cout << "  Two-Factor Enabled       : " << (profile.get2FAStatus() ? "Yes" : "No") << endl;
                cout << " " << endl;
            }
            cout << "────────────────────────────────────────────────────────" << endl;
            cout << "                 Transaction History:                   " << endl;
            cout << "────────────────────────────────────────────────────────" << endl;
            for (final Transaction transaction : user.transactionhistory)
            {
                cout << "  Transaction ID          : " << transaction.transactionID << endl;
                cout << "  Transaction Type        : " << transaction.transactionType << endl;
                cout << "  Amount                  : " << transaction.amount << endl;
                cout << "  Timestamp               : " << transaction.timestamp << endl;
                cout << " " << endl;
            }
            cout << "────────────────────────────────────────────────────────" << endl;
            cout << "                     Sessions:                          " << endl;
            cout << "────────────────────────────────────────────────────────" << endl;
            for (const Session &session : user.sessions)
            {
                cout << "  Session ID              : " << session.sessionID << endl;
                cout << "  Username                : " << session.username << endl;
                cout << "  Timestamp               : " << session.timestamp << endl;
                cout << " " << endl;
            }
            cout << "────────────────────────────────────────────────────────" << endl;
            cout << "                Product Applications:                   " << endl;
            cout << "────────────────────────────────────────────────────────" << endl;
            for (const ProductApplication &productApp : user.productapplications)
            {
                cout << "  Product Type            : " << productApp.producttype << endl;
                cout << "  Product ID              : " << productApp.productID << endl;
                cout << " " << endl;
            }
            cout << "────────────────────────────────────────────────────────" << endl;
            cout << "                  Help and Resources:                   " << endl;
            cout << "────────────────────────────────────────────────────────" << endl;
            for (const HelpandResources &resources : user.helpandresources)
            {
                cout << "  Help ID                : " << resources.helpID << endl;
                cout << "  Type                   : " << resources.helpandresourcesType << endl;
                cout << "  Description            : " << resources.helpandresourcesDescription << endl;
                cout << "  Feedback               : " << resources.feedback << endl;
                cout << " " << endl;
            }
            cout << "╔═══════════════════════════════════════════════════════════════════════════╗     " << endl;
            cout << "║╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳║     " << endl;
            cout << "╚═══════════════════════════════════════════════════════════════════════════╝     " << endl;
        }
        cout << "\nPress enter to continue...";
    }

    public void makeUserAdmin(String username) {
        for (final User user : BankSystem.getUsers()) {
            if (user.getUsername().equals(username)) {
                user.setAdminStatus(true);
                System.out.printf("User %s is now an admin.", user.getUsername());
                BankSystem.saveDataToFile();
            }
        }
    }

    public void makeUserCustomerService(String username) {
        for (final User user : BankSystem.getUsers()) {
            if (user.getUsername().equals(username)) {
                user.setCSStatus(true);
                System.out.printf("User %s is now a customer service.", user.getUsername());
                BankSystem.saveDataToFile();
            }
        }
    }

    @Override
    public void handleSettings(String username) {
        while (true)
        {
            String newpass, newemail, newphone, newusername;
            char new2FA;
            displayUserData(username);

            cout << "╔═════════════════════════════════════╗    " << endl;
            cout << "║           Manage Account            ║   " << endl;
            cout << "╠═════════════════════════════════════╣    " << endl;
            cout << "║  1. Change Password                 ║" << endl;
            cout << "║  2. Change Email                    ║" << endl;
            cout << "║  3. Change Phone                    ║" << endl;
            cout << "║  4. Change Username                 ║" << endl;
            cout << "║  5. Enable/Disable 2FA              ║" << endl;
            cout << "║  6. Show Activity Log               ║" << endl;
            cout << "║  7. Make User Admin                 ║" << endl;
            cout << "║  8. Make User Customer Service      ║" << endl;
            cout << "║  9. Deposit(Savings Only)           ║" << endl;
            cout << "║  10. Withdraw(Savings Only)         ║" << endl;
            cout << "║  11. Make a Purchase(Credit Only)   ║" << endl;
            cout << "║  12. Bills Payment(Credit Only)     ║" << endl;
            cout << "║  13. Back to Profile                ║" << endl;
            cout << "╚═════════════════════════════════════╝   " << endl;
            cout << " " << endl;

            int mchoice;
            cout << "Enter: ";
            cin >> mchoice;
            cout << " " << endl;
            switch (mchoice)
            {
                case 1:
                    cout << "Enter new password: ";
                    cin >> newpass;
                    ChangePassword(username, newpass);
                    break;

                case 2:
                    cout << "Enter new email: ";
                    cin >> newemail;
                    ChangeEmail(username, newemail);
                    break;

                case 3:
                    cout << "Enter new phone: ";
                    cin >> newphone;
                    ChangePhone(username, newphone);
                    break;

                case 4:
                    cout << "Enter new username: ";
                    cin >> newusername;
                    ChangeUsername(username, newusername);
                    break;

                case 5:
                    cout << "Do you want to enable 2FA?(Y/N): ";
                    cin >> new2FA;
                    DE2FA(username, new2FA);
                    break;

                case 6:
                    displayActivityLog(username);
                    break;

                case 7:
                    makeUserAdmin(username);
                    break;
                case 8:
                    makeUserCustomerService(username);
                    break;
                case 9:
                    admindepositFunds();
                    break;
                case 10:
                    adminwithdrawFunds();
                    break;
                case 11:
                    adminmakePurchase();
                    break;
                case 12:
                    adminpayBills();
                    break;
                case 13:
                    cout << "Press Enter to continue..." << endl;
                    return;

                default:
                    cout << "*Invalid choice. Please select a valid option." << endl;
                    break;
            }
            cout << " " << endl;
        ::system("pause");
        ::system("cls");
        }
    }

    @Override
    public void processDeposit(String username) {
        String username;
        double amount;
        cout << "Enter the username of the user to deposit funds: ";
        cin >> username;
        cin.ignore();
        cout << "Enter the amount to deposit: ";
        cin >> amount;
        cin.ignore();
        if (Transaction.depositFunds(username, amount))
        {
            cout << "Funds deposited successfully." << endl;
        }
        else
        {
            cout << "Error: Unable to deposit funds." << endl;
        }
    }

    @Override
    public void processWithdrawal(String username) {
        string username;
        double amount;
        cout << "Enter the username of the user to withdraw funds: ";
        cin >> username;
        cin.ignore();
        cout << "Enter the amount to withdraw: ";
        cin >> amount;
        cin.ignore();
        if (withdrawFunds(username, amount))
        {
            cout << "Funds withdrawn successfully." << endl;
        }
        else
        {
            cout << "Error: Unable to withdraw funds." << endl;
        }
    }

    @Override
    public void processPurchase(String username) {
        string username;
        double amount;
        string purchaseDescription;
        cout << "Enter the username of the user to make a purchase: ";
        cin >> username;
        cin.ignore();
        cout << "Enter the amount to withdraw: ";
        cin >> amount;
        cin.ignore();
        cout << "Enter the description of the purchase: ";
        getline(cin, purchaseDescription);
        if (makePurchase(username, amount, purchaseDescription))
        {
            cout << "Purchase made successfully." << endl;
        }
        else
        {
            cout << "Error: Unable to make purchase." << endl;
        }
    }

    @Override
    public void processBills(String username) {
        string username;
        double amount;
        string billDescription;
        cout << "Enter the username of the user to pay bills: ";
        cin >> username;
        cin.ignore();
        cout << "Enter the amount to withdraw: ";
        cin >> amount;
        cin.ignore();
        cout << "Enter the description of the bill: ";
        getline(cin, billDescription);
        if (payBills(username, amount, billDescription))
        {
            cout << "Bill paid successfully." << endl;
        }
        else
        {
            cout << "Error: Unable to pay bill." << endl;
        }
    }
}
