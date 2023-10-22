package com.ctb.classes;

import java.util.Objects;
import java.util.Scanner;

class Admin extends User{
    private String adminID;
    private final Scanner input = new Scanner(System.in);

    /*----------------------Setter Methods----------------------*/
    protected void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    /*----------------------Getter Methods----------------------*/
    protected String getAdminID() {return adminID;}

    /*----------------------Class Methods----------------------*/
    protected boolean deleteUserByUsername(String userToDelete) {
        var userIterator = BankSystem.getUsers().iterator();
        while(userIterator.hasNext()) {
            User user = userIterator.next();
            if (getUsername().equals(userToDelete)) {
                userIterator.remove();
                BankSystem.saveDataToFile();
                return true;
            }
        }
        return false;
    }

    protected void handleManageUsers(String username) {
        BankSystem.clearConsole();
        System.out.print(
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
                System.out.print("Invalid choice. Please try again.");
                break;
        }
    }

    protected void updateUser() {
        BankSystem.clearConsole();
        System.out.print(
                "\n╭────────────────────────────────────────────────────────────────╮" +
                "\n│                         Update User                            │" +
                "\n╰────────────────────────────────────────────────────────────────╯"
        );
        System.out.print("\nEnter the username of the user you want to update: ");
        String pickedUsername = input.nextLine();
        input.nextLine();
        handleSettings(pickedUsername);
    }

    protected void deleteUser() {
        BankSystem.clearConsole();
        System.out.print(
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
    protected void displayUserData(String username) {
        for (final User user : BankSystem.getUsers())
        {
            if (Objects.equals(getUsername(), username))
            {
                handleUserData(user);
                return; // Exit the loop once the user is found and displayed
            }
        }
        System.out.printf("User with username '%s' not found.", username);
    }

    protected void handleUserData(User user) {
        System.out.print(
                "\n──────────────────────────────────────────────────────────────────" +
                "\n                          Information:                            " +
                "\n──────────────────────────────────────────────────────────────────" +
                "\n  User ID                : " + user.getUserID() +
                "\n  Name                   : " + user.getName() +
                "\n  Username               : " + getUsername() +
                "\n  Is Admin               : " + (BankSystem.isAdmin(getUsername()) ? "Yes" : "No") +
                "\n  Is Customer Service    : " + (BankSystem.isCustomerService(getUsername()) ? "Yes" : "No") +
                "\n  Product Type           : " + user.getProductType() +
                "\n  Balance                : " + user.getBalance()
        );

        System.out.print(
                "\n──────────────────────────────────────────────────────────────────" +
                "\n                           Profiles:                              " +
                "\n──────────────────────────────────────────────────────────────────"
        );
        for (final Profile profile : user.getUserProfile())
        {
            System.out.print(
                    "\n Email                  : " + profile.getEmail() +
                    "\n  Phone                  : " + profile.getPhoneNumber() +
                    "\n  Two-Factor Enabled     : " + (profile.get2FAStatus() ? "Yes" : "No")
            );
        }

        System.out.print(
                "\n──────────────────────────────────────────────────────────────────" +
                "\n                      Transaction History:                        " +
                "\n──────────────────────────────────────────────────────────────────"
        );
        for (final Transaction transaction : user.getUserTransaction())
        {
            System.out.print(
                    "\n  Transaction ID         : " + transaction.getTransactionID() +
                    "\n  Transaction Type       : " + transaction.getTransactionType() +
                    "\n  Amount                 : " + transaction.getAmount() +
                    "\n  Timestamp              : " + transaction.getTimeStamp()
            );
        }

        System.out.print(
                "\n──────────────────────────────────────────────────────────────────" +
                "\n                          Sessions:" +
                "\n──────────────────────────────────────────────────────────────────"
        );

        for (final Session session : user.getUserSessions())
        {
            System.out.print(
                    "\n  Session ID             : " + session.getSessionID() +
                    "\n  Username               : " + getUsername() +
                    "\n  Timestamp              : " + session.getTimeStamp()
            );
        }

        System.out.print(
            "\n──────────────────────────────────────────────────────────────────" +
            "\n                     Product Applications:" +
            "\n──────────────────────────────────────────────────────────────────"
        );
        for (final ProductApplication productApp : user.getUserProductApplications())
        {
            System.out.print(
                "\n  Product ID             : " + productApp.getProductID() +
                "\n  Product Type           : " + productApp.getProductType()
            );

        }

        System.out.print(
            "\n──────────────────────────────────────────────────────────────────" +
            "\n                      Help and Resources:" +
            "\n──────────────────────────────────────────────────────────────────"
        );
        for (final HelpAndResources resources : user.getUserHelpAndResources())
        {
            System.out.print(
                "\n  Help ID                : " + resources.getHelpID() +
                "\n  Type                   : " + resources.getH_rType() +
                "\n  Description            : " + resources.getH_rDescription() +
                "\n  Feedback               : " + resources.getFeedback() +
                "\n──────────────────────────────────────────────────────────────────"
            );
        }
        System.out.print(
                "\n╔═══════════════════════════════════════════════════════════════════════════╗     " +
                "\n║╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳║     " +
                "\n╚═══════════════════════════════════════════════════════════════════════════╝     "
        );
    }

    protected void displayAllUserData() {
        BankSystem.clearConsole();
        System.out.print(
                "\n╔════════════════════════════════════════════════════════╗" +
                "\n║                   View Users Data                      ║" +
                "\n╚════════════════════════════════════════════════════════╝"
        );
        for (final User user : BankSystem.getUsers())
        {
            handleUserData(user);
        }
        System.out.print("\nPress enter to continue...");
    }

    protected void makeUserAdmin(String username) {
        for (final User user : BankSystem.getUsers()) {
            if (getUsername().equals(username)) {
                user.setAdminStatus(true);
                System.out.printf("User %s is now an admin.", getUsername());
                BankSystem.saveDataToFile();
            }
        }
    }

    protected void makeUserCustomerService(String username) {
        for (final User user : BankSystem.getUsers()) {
            if (getUsername().equals(username)) {
                user.setCSStatus(true);
                System.out.printf("User %s is now a customer service.", getUsername());
                BankSystem.saveDataToFile();
            }
        }
    }

    @Override
    public void handleSettings(String username) {
        while (true)
        {
            String newPassword, newEmail, newPhoneNumber, newUsername;
            char new2FA;
            displayUserData(username);
            System.out.print(
                "╔═════════════════════════════════════╗    " +
                "║           Manage Account            ║   " +
                "╠═════════════════════════════════════╣    " +
                "║  1. Change Password                 ║" +
                "║  2. Change Email                    ║" +
                "║  3. Change Phone                    ║" +
                "║  4. Change Username                 ║" +
                "║  5. Enable/Disable 2FA              ║" +
                "║  6. Show Activity Log               ║" +
                "║  7. Make User Admin                 ║" +
                "║  8. Make User Customer Service      ║" +
                "║  9. Deposit(Savings Only)           ║" +
                "║  10. Withdraw(Savings Only)         ║" +
                "║  11. Make a Purchase(Credit Only)   ║" +
                "║  12. Bills Payment(Credit Only)     ║" +
                "║  13. Back to Profile                ║" +
                "╚═════════════════════════════════════╝"
            );
            System.out.print("Enter: ");
            int choice = input.nextInt();
            input.nextLine();
            switch (choice)
            {
                case 1:
                    System.out.print("Enter new password: ");
                    newPassword = input.nextLine();
                    Admin.changePassword(username, newPassword);
                    break;

                case 2:
                    System.out.print("Enter new email: ");
                    newEmail = input.nextLine();
                    Admin.changeEmail(username, newEmail);
                    break;

                case 3:
                    System.out.print("Enter new phone: ");
                    newPhoneNumber = input.nextLine();
                    Admin.changePhoneNum(username, newPhoneNumber);
                    break;

                case 4:
                    System.out.print("Enter new username: ");
                    newUsername = input.nextLine();
                    Admin.changeUsername(username, newUsername);
                    break;

                case 5:
                    System.out.print("Do you want to enable 2FA?(Y/N): ");
                    new2FA = input.next().charAt(0);
                    Admin.change2FAStatus(username, new2FA);
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
                    processDeposit();
                    break;
                case 10:
                    processWithdrawal();
                    break;
                case 11:
                    processPurchase();
                    break;
                case 12:
                    processBills();
                    break;
                case 13:
                    System.out.print("Press Enter to continue...");
                    return;

                default:
                    System.out.print("*Invalid choice. Please select a valid option.");
                    break;
            }
            BankSystem.clearConsole();
        }
    }

    protected void processDeposit() {
        String username;
        double amount;
        System.out.print("Enter the username of the user to deposit funds: ");
        username = input.nextLine();
        input.nextLine();
        System.out.print("Enter the amount to deposit: ");
        amount = input.nextInt();
        input.nextLine();
        if (Transaction.depositFunds(username, amount))
        {
            System.out.print("Funds deposited successfully.");
        }
        else
        {
            System.out.print("Error: Unable to deposit funds.");
        }
    }

    protected void processWithdrawal() {
        String username;
        double amount;
        System.out.print("Enter the username of the user to withdraw funds: ");
        username = input.nextLine();
        input.nextLine();
        System.out.print("Enter the amount to withdraw: ");
        amount = input.nextInt();
        input.nextLine();
        if (Transaction.withdrawFunds(username, amount))
        {
            System.out.print("Funds withdrawn successfully.");
        }
        else
        {
            System.out.print("Error: Unable to withdraw funds.");
        }
    }

    protected void processPurchase() {
        String username, purchaseDescription;
        double amount;
        System.out.print("Enter the username of the user to make a purchase: ");
        username = input.nextLine();
        input.nextLine();
        System.out.print("Enter the amount to withdraw: ");
        amount = input.nextInt();
        input.nextLine();
        System.out.print("Enter the description of the purchase: ");
        purchaseDescription =  input.nextLine();
        input.nextLine();
        if (Transaction.makePurchase(username, amount, purchaseDescription))
        {
            System.out.print("Purchase made successfully.");
        }
        else
        {
            System.out.print("Error: Unable to make purchase.");
        }
    }

    protected void processBills() {
        String username, billDescription;
        double amount;
        System.out.print("Enter the username of the user to pay bills: ");
        username = input.nextLine();
        input.nextLine();
        System.out.print("Enter the amount to withdraw: ");
        amount = input.nextInt();
        input.nextLine();
        System.out.print("Enter the description of the bill: ");
        billDescription = input.nextLine();
        input.nextLine();
        if (Transaction.payBills(username, amount, billDescription))
        {
            System.out.print("Bill paid successfully.");
        }
        else
        {
            System.out.print("Error: Unable to pay bill.");
        }
    }
}
