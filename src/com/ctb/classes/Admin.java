package com.ctb.classes;


class Admin extends User{
    private String adminID;
    private String name;
    private String username;
    private String password;

    public boolean deleteUserByUsername(String username) {
        using namespace std; // Add this line to use the std namespace

        auto userToDelete = std::remove_if(users.begin(), users.end(),
                                       [usernameToDelete](const User &user)
        { return user.username == usernameToDelete; });

        if (userToDelete != users.end())
        {
            users.erase(userToDelete, users.end());

            saveDataToFile(); // Save data after deletion
            return true;
        }
        else
        {

            return false;
        }
    }

    public void handleManageUsers(String username) {
        ::system("cls");
        cout << " " << endl;
        cout << "╔═══════════════════════════════╗    " << endl;
        cout << "║          Manage Users         ║        " << endl;
        cout << "╠═══════════════════════════════╣    " << endl;
        cout << "║  1. View Users Data           ║     " << endl;
        cout << "║  2. Add Users                 ║   " << endl;
        cout << "║  3. Delete Users              ║     " << endl;
        cout << "║  4. Update Users              ║      " << endl;
        cout << "║  5. Exit                      ║      " << endl;
        cout << "╚═══════════════════════════════╝    " << endl;
        cout << " " << endl;
        cout << "Enter your choice: ";
        int choice;
        cin >> choice;
        cin.ignore();
        switch (choice)
        {
            case 1:
                displayAllUserData();
                break;
            case 2:
                applyForProduct();
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
                cout << "Invalid choice. Please try again." << endl;
                break;
        }
    }

    public void updateUser() {
        ::system("cls");
        cout << " " << endl;
        cout << "╭────────────────────────────────────────────────────────────────╮" << endl;
        cout << "│                         Update User                            │ " << endl;
        cout << "╰────────────────────────────────────────────────────────────────╯" << endl;
        cout << " \nEnter the username of the user you want to update: ";
        string pickedusername;
        cin >> pickedusername;
        adminhandleAccountSettings(pickedusername);
    }

    public void deleteUser() {
        ::system("cls");
        string user;
        cout << " " << endl;
        cout << "╭────────────────────────────────────────────────────────────────╮" << endl;
        cout << "│                         Delete User                            │ " << endl;
        cout << "╰────────────────────────────────────────────────────────────────╯" << endl;
        cout << "\nEnter the username of the user to delete: ";
        cin >> user;
        cin.ignore();
        cout << " " << endl;

        if (deleteUserByUsername(user))
        {
            cout << "User with username '" << user << "' has been deleted." << endl;
        }
        else
        {
            cout << "User with username '" << user << "' not found." << endl;
        }
    }

    public void displayUserdata(String username) {
        for (const User &user : users)
        {
            if (user.username == usernameToDisplay)
            {
                cout << endl;
                cout << "──────────────────────────────────────────────────────────────────" << endl;
                cout << "                          Information:" << endl;
                cout << "──────────────────────────────────────────────────────────────────" << endl;
                cout << "  User ID                : " << user.userID << endl;
                cout << "  Name                   : " << user.name << endl;
                cout << "  Username               : " << user.username << endl;
                cout << "  Is Admin               : " << (user.isadmin ? "Yes" : "No") << endl;
                cout << "  Is Customer Service    : " << (user.iscustomerservice ? "Yes" : "No") << endl;
                cout << "  Product Type           : " << user.producttype << endl;
                cout << "  Balance                : " << user.balance << endl;
                cout << "──────────────────────────────────────────────────────────────────" << endl;
                cout << "                           Profiles:" << endl;
                cout << "──────────────────────────────────────────────────────────────────" << endl;
                for (const Profile &profile : user.profiles)
                {
                    cout << "  Email                  : " << profile.email << endl;
                    cout << "  Phone                  : " << profile.phone << endl;
                    cout << "  Two-Factor Enabled     : " << (profile.isTwoFactorEnabled ? "Yes" : "No") << endl;
                }
                cout << "──────────────────────────────────────────────────────────────────" << endl;
                cout << "                      Transaction History:" << endl;
                cout << "──────────────────────────────────────────────────────────────────" << endl;
                for (const Transaction &transaction : user.transactionhistory)
                {
                    cout << "  Transaction ID         : " << transaction.transactionID << endl;
                    cout << "  Transaction Type       : " << transaction.transactionType << endl;
                    cout << "  Amount                 : " << transaction.amount << endl;
                    cout << "  Timestamp              : " << transaction.timestamp << endl;
                }
                cout << "──────────────────────────────────────────────────────────────────" << endl;
                cout << "                          Sessions:" << endl;
                cout << "──────────────────────────────────────────────────────────────────" << endl;
                for (const Session &session : user.sessions)
                {
                    cout << "  Session ID             : " << session.sessionID << endl;
                    cout << "  Username               : " << session.username << endl;
                    cout << "  Timestamp              : " << session.timestamp << endl;
                }
                cout << "──────────────────────────────────────────────────────────────────" << endl;
                cout << "                     Product Applications:" << endl;
                cout << "──────────────────────────────────────────────────────────────────" << endl;
                for (const ProductApplication &productApp : user.productapplications)
                {
                    cout << "  Product Type           : " << productApp.producttype << endl;
                    cout << "  Product ID             : " << productApp.productID << endl;
                }
                cout << "──────────────────────────────────────────────────────────────────" << endl;
                cout << "                      Help and Resources:" << endl;
                cout << "──────────────────────────────────────────────────────────────────" << endl;
                for (const HelpandResources &resources : user.helpandresources)
                {
                    cout << "  Help ID                : " << resources.helpID << endl;
                    cout << "  Type                   : " << resources.helpandresourcesType << endl;
                    cout << "  Description            : " << resources.helpandresourcesDescription << endl;
                    cout << "  Feedback               : " << resources.feedback << endl;
                    cout << "──────────────────────────────────────────────────────────────────" << endl;
                }

                return; // Exit the loop once the user is found and displayed
            }
        }

        cout << "User with username '" << usernameToDisplay << "' not found." << endl;
    }
    }

    public void displayAllUserData() {
        ::system("cls");
        cout << "╔════════════════════════════════════════════════════════╗     " << endl;
        cout << "║                   View Users Data                      ║     " << endl;
        cout << "╚════════════════════════════════════════════════════════╝     " << endl;
        cout << " " << endl;
        for (const User &user : users)
        {
            cout << "┌────────────────────────────────────────────────────────┐" << endl;
            cout << "│                    Information:                        │" << endl;
            cout << "└────────────────────────────────────────────────────────┘" << endl;
            cout << "      User ID              : " << user.userID << endl;
            cout << "      Name                 : " << user.name << endl;
            cout << "      Username             : " << user.username << endl;
            cout << "      Is Admin             : " << (user.isadmin ? "Yes" : "No") << endl;
            cout << "      Is Customer Service  : " << (user.iscustomerservice ? "Yes" : "No") << endl;
            cout << "      Product Type         : " << user.producttype << endl;
            cout << "      Balance              : " << user.balance << endl;
            cout << " " << endl;
            cout << "────────────────────────────────────────────────────────" << endl;
            cout << "                      Profiles:                         " << endl;
            cout << "────────────────────────────────────────────────────────" << endl;
            for (const Profile &profile : user.profiles)
            {
                cout << "  Email                    : " << profile.email << endl;
                cout << "  Phone                    : " << profile.phone << endl;
                cout << "  Two-Factor Enabled       : " << (profile.isTwoFactorEnabled ? "Yes" : "No") << endl;
                cout << " " << endl;
            }
            cout << "────────────────────────────────────────────────────────" << endl;
            cout << "                 Transaction History:                   " << endl;
            cout << "────────────────────────────────────────────────────────" << endl;
            for (const Transaction &transaction : user.transactionhistory)
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
        for (User &user : users)
        {
            if (user.username == username)
            {
                user.isadmin = true;
                cout << "User " << user.username << " is now an admin." << endl;
                saveDataToFile();
            }
        }
    }

    public void makeUserCustomerService(String username) {
        for (User &user : users)
        {
            if (user.username == username)
            {
                user.iscustomerservice = true;
                cout << "User " << user.username << " is now a customer service." << endl;
                saveDataToFile();
            }
        }
    }

    @Override
    public void handleSettings(String username) {
        while (true)
        {
            string newpass, newemail, newphone, newusername;
            char new2FA;
            displayUserDataByUsername(username);

            cout << " " << endl;
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
