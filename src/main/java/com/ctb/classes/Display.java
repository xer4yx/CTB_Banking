package com.ctb.classes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

import static com.ctb.classes.BankSystem.*;

public class Display {
    private static final Scanner input = new Scanner(System.in);
    public static void displayMainMenu()
    {
        System.out.print(
                """
                        
                        ╔══════════════════════════════════════╗
                        ║      ╔═══════════════════════╗       ║
                        ║      ║   CENTRAL TRUST BANK  ║       ║
                        ║      ╚═══════════════════════╝       ║
                        ╚══════════════════════════════════════╝
                        ┌──────────────────────────────────────┐
                        │ ┌──────────────────────────────────┐ │
                        │ │  1. Login                        │ │
                        │ │  2. Product Application          │ │
                        │ │  3. Forgot Password              │ │
                        │ │  4. Exit                         │ │
                        │ └──────────────────────────────────┘ │
                        └──────────────────────────────────────┘
                        Enter your choice:\s"""
        );
    }

     public static boolean loginUser()
     {
        BankSystem.clearConsole();
         System.out.print(
                 """
      
                        ╔══════════════════════════════════════╗
                        ║      ╔═══════════════════════╗       ║
                        ║      ║   CENTRAL TRUST BANK  ║       ║
                        ║      ╚═══════════════════════╝       ║
                        ╚══════════════════════════════════════╝
                                                              
                        ╔══════════════════════════════════════╗
                        ║                Login                 ║
                        ╚══════════════════════════════════════╝"""
         );
        String username, password;
        System.out.print("\nEnter username:");
        username = input.nextLine();
        System.out.print("Enter password:");
        password = input.nextLine();

        if (SecuritySystem.authenticateUser(username, password))
        {

            BankSystem.setCurrentLoggedInUser(username);
            BankSystem.setCurrentProductType(username);
            System.out.print(
                    """
                            
                            ---Login successful!---
                            
                            """
            );
            System.out.print("Press Enter to continue...");
            input.nextLine();
            BankSystem.clearConsole();
            return true;
        }
        else
        {
            System.out.print(
                    """
                            
                            *Invalid username or password. Please try again.
                            
                            """
            );
            System.out.print("Press Enter to continue...");
            input.nextLine();
            BankSystem.clearConsole();
            return false;
        }
    }

    protected static void logout(String username)
    {
        for (User user : BankSystem.users)
        {
            if (User.getUsername().equals(username))
            {
                Session.saveSession(username, "Logout");
                System.out.print("Logged out successfully.");
                break;
            }
        }
    }

     void forgotPassword()
     {
         BankSystem.clearConsole();
         System.out.print(
                 """
                         ╭────────────────────────────────────────────────────────────╮
                         │                     Forgot Password                        │
                         ╰────────────────────────────────────────────────────────────╯"""
         );
         char choice;
         System.out.print("\nEnter your email: ");
         String email = input.nextLine();
         boolean emailFound = false; // To track whether the email was found or not

         for (User user : BankSystem.users)
         {
            for (Profile profile : user.userProfile)
            {
                if (Objects.equals(profile.getEmail(), email))
                {
                    System.out.print("---Email found!---");
                    System.out.print("\nSending an OTP for " + profile.getEmail() + " 2 Factor Authentication.");
                    SecuritySystem.sendOTP();
                    System.out.print("\nEnter your OTP: ");
                    String inputOTP = input.nextLine();
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
                    System.out.print(
                            """
                                    ──────────────────────────────────────────────────────────────
                                    Enter new password:\s
                                    """
                    );
                    String newpass = input.nextLine();
                    User.changePassword(User.getUsername(), newpass);
                    System.out.print("---Password changed successfully!---");
                    emailFound = true;
                }
            }
         }

        if (!emailFound)
        {
            System.out.print("\n*Email not found. Please try again.");
        }
    }

     static void displayDashboardMenu(final String username)
    {
        for (final User user : BankSystem.users)
        {
            if (Objects.equals(User.getUsername(), username))
            {
                if (User.isAdmin())
                {
                    BankSystem.clearConsole();
                    System.out.print(
                            """
                                    ╔═════════════════════════════════════╗
                                    ║            Administrator            ║
                                    ╚═════════════════════════════════════╝
                                                                           
                                    ╔═════════════════════════════════════╗
                                    ║         Dashboard Options:          ║
                                    ╠═════════════════════════════════════╣
                                    ║  1. Manage Users                    ║
                                    ║  2. Help  Resources                 ║
                                    ║  3. Logout                          ║
                                    ╚═════════════════════════════════════╝
                                    Enter your choice:\s"""
                    );
                }
                else if (User.isCustomerService())
                {
                    BankSystem.clearConsole();
                    System.out.print(
                            """
                                    ╔═════════════════════════════════════╗
                                    ║          Customer Service           ║
                                    ╚═════════════════════════════════════╝
                                                                           
                                    ╔═════════════════════════════════════╗
                                    ║         Dashboard Options:          ║
                                    ╠═════════════════════════════════════╣
                                    ║  1. Messages                        ║
                                    ║  2. Logout                          ║
                                    ╚═════════════════════════════════════╝
                                    Enter your choice:\s"""
                    );
                }
                else
                {
                    BankSystem.clearConsole();
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
    }

    public static void handleDashboardOptions()
    {
        while (true)
        {
            displayDashboardMenu(getCurrentLoggedInUser());
            String productType = BankSystem.getCurrentProductType(BankSystem.getCurrentLoggedInUser());
            int choice = input.nextInt();
            input.nextLine();
            if (User.isAdmin())
            {
                switch (choice)
                {
                    case 1:
                        Admin.handleManageUsers(getCurrentLoggedInUser());
                        input.nextLine();
                        break;
                    case 2:
                        BankSystem.clearConsole();
                        CustomerService.displayAllHR();
                        CustomerService.replyToHelp();
                        break;
                    case 3:
                        System.out.print("Logging out...");
                        logout(getCurrentLoggedInUser());
                        setCurrentLoggedInUser("");
                        System.out.print("Press Enter to continue...");
                        input.nextLine();
                        BankSystem.clearConsole();
                        return;
                    default:
                        System.out.print("*Invalid choice. Please select a valid option.");
                }
            }
            else if (User.isCustomerService())
            {
                switch (choice)
                {
                    case 1:
                        BankSystem.clearConsole();
                        CustomerService.displayAllHR();
                        CustomerService.replyToHelp();
                        break;
                    case 2:
                        System.out.print("Logging out...");
                        logout(getCurrentLoggedInUser());
                        setCurrentLoggedInUser("");
                        System.out.print("Press Enter to continue...");
                        input.nextLine();
                        BankSystem.clearConsole();
                        return;
                    default:
                        System.out.print("*Invalid choice. Please select a valid option.");
                }
            }
            else
            {
                switch (choice)
                {
                    case 1:
                        handleProductOptions(getCurrentProductType(getCurrentLoggedInUser()), getCurrentLoggedInUser());
                        break;
                    case 2:
                        Profile.displayProfile(getCurrentLoggedInUser());
                        break;
                    case 3:
                        viewAnalyticsDashBoard(getCurrentLoggedInUser());
                        break;
                    case 4:
                        handleHelpAndResources(getCurrentLoggedInUser());
                        break;
                    case 5:
                        System.out.print("\nLogging out...");
                        logout(getCurrentLoggedInUser());
                        setCurrentLoggedInUser("");
                        System.out.print(
                                """
                                        ──────────────────────────────────
                                        Press Enter to continue..."""
                        );
                        input.nextLine();
                        BankSystem.clearConsole();
                        return;
                    default:
                        System.out.print("*Invalid choice. Please select a valid option.");
                }
            }
        }
    }

    public static void handleProductOptions(final String producttype, final String username)
    {
        if (Objects.equals(producttype, "Savings Account"))
        {
            displaySavingsMenu(username);
        }
        else if (Objects.equals(producttype, "Credit Account"))
        {
            displayCreditMenu(username);
        }
    }

    protected static void displaySavingsMenu(final String username)
    {
        handleTransactionCenter(username);
    }

    protected static void displayCreditMenu(final String username)
    {
        handleCreditCenter(username);
    }

    protected static void displayTransactionMenu(final String username)
    {
        BankSystem.clearConsole();
        System.out.print(
                """
                        
                        ╔═════════════════════════════════════╗\s
                        ║         Transaction Center:         ║\s
                        ╠═════════════════════════════════════╣\s
                        ║  1. Deposit Funds                   ║ \s
                        ║  2. Withdraw Funds                  ║
                        ║  3. View Transaction History        ║ \s
                        ║  4. Back to Dashboard               ║\s
                        ╚═════════════════════════════════════╝
                        Enter your choice:\s"""
        );
        setCurrentLoggedInUser(username);
    }

    protected static void displayTransactionCredit(final String username)
    {
    BankSystem.clearConsole();
        System.out.print(
                """
                        ╔═════════════════════════════════════╗
                        ║         Transaction Center:         ║
                        ╠═════════════════════════════════════╣
                        ║  1. Make a Purchase                 ║
                        ║  2. Pay Bills                       ║
                        ║  3. View Transaction History        ║
                        ║  4. Back to Dashboard               ║
                        ╚═════════════════════════════════════╝
                        Enter your choice:\s"""
        );
        setCurrentLoggedInUser(username);
    }

    protected static void displayTransactionHistory(final String username)
    {
        for (final User user : BankSystem.users)
        {
            if (Objects.equals(User.getUsername(), username))
            {
                System.out.print(
                        """
                                ╔═════════════════════════════════════╗
                                ║        Transaction History          ║
                                ╚═════════════════════════════════════╝"""
                );
                System.out.print(
                        "User: " + User.getUsername() +
                        "\n───────────────────────────────────────");
                for (final Transaction transaction : user.userTransaction)
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    Date date = new Date(transaction.getTimeStamp());
                    String formattedDate = sdf.format(date);
                    System.out.print(
                            "\nTransaction ID: " + transaction.getTransactionID() +
                            "\nTransaction Type: " + transaction.getTransactionType() +
                            "\nAmount: $" + transaction.getAmount() +
                            "\nTimestamp: " + formattedDate +
                            "\nDescription: " + transaction.getDescription() +
                            "\n───────────────────────────────────────"
                    );
                }
            }
        }
    }

    protected static void handleTransactionCenter(final String username)
    {
        while (true)
        {
            displayTransactionMenu(username);
            int transactionChoice = input.nextInt();
            input.nextLine();

            switch (transactionChoice)
            {
                case 1:
                    User.processDeposit(username);
                    break;
                case 2:
                    User.processWithdrawal(username);
                    break;
                case 3:;
            BankSystem.clearConsole();
                    displayTransactionHistory(username);
                    System.out.print("Press Enter to continue...");
                    input.nextLine();
                    break;
                case 4:
                    return;
                default:
                    System.out.print("*Invalid choice. Please select a valid option.");
            }
        }
    }

    protected static void handleCreditCenter(final String username)
    {
        while (true)
        {
            displayTransactionCredit(username);

            int transactionChoice = input.nextInt();
            input.nextLine();

            switch (transactionChoice)
            {
                case 1:
                    User.processPurchase(username);
            BankSystem.clearConsole();
                    break;
                case 2:
                    User.processBills(username);
            BankSystem.clearConsole();
                    break;
                case 3:
                    BankSystem.clearConsole();
                    displayTransactionHistory(username);
                    System.out.print("Press Enter to continue...");
                    input.nextLine();
                    break;
                case 4:
                    return;
                default:
                    System.out.print("*Invalid choice. Please select a valid option.");
            }
        }
    }

     public static void handleHelpAndResources(final String username)
     {
        BankSystem.clearConsole();
        System.out.print(
                """
                        ╔═════════════════════════════════════╗
                        ║          Help  Resources            ║
                        ╠═════════════════════════════════════╣
                        ║  1. Chat with AI Assistant          ║
                        ║  2. Contact US                      ║
                        ║  3. Back to Dashboard               ║
                        ╚═════════════════════════════════════╝
                        Enter your choice:\s"""
        );

        int jhchoice = input.nextInt();
        input.nextLine();
        switch (jhchoice)
        {
            case 1:
                System.out.print(
                        """
                                Hi! I'm your AI Assistant. How may I help you?
                                Enter inquiry:\\s"""
                );
                String message = input.nextLine();
                HelpAndResources.chatBot(message, BankSystem.getCurrentLoggedInUser());
                System.out.print("Press Enter to continue...");
                input.nextLine();
                break;
            case 2:
                BankSystem.clearConsole();
                System.out.print(
                        """
                                ╭────────────────────────────────────────────────╮
                                │                  Contact Us                    │
                                ├────────────────────────────────────────────────┤
                                │  Email: Uniportal@proton.me                    │
                                │  Phone: 1-800-123-4567                         │
                                │  Address: 123 Main St, New York, NY 10001      │
                                ╰────────────────────────────────────────────────╯
                                ╭────────────────────────────────────────────────╮
                                │  1. Send a message                             │
                                │  2. Back to Dashboard                          │
                                ╰────────────────────────────────────────────────╯
                                Enter your choice:\s"""
                );

                int schoice = input.nextInt();
                if (schoice == 1)
                {
                    User.askHelp(getCurrentLoggedInUser());
                    System.out.print(
                            """
                                    Message sent successfully!
                                    Press Enter to continue...
                                    """
                    );
                    input.nextLine();
                }
                else if (schoice == 2)
                {
                    break;
                }
                else
                {
                    System.out.print("*Invalid choice. Please select a valid option.");
                    return;
                }
                break;
            case 3:
                return;
            default:
                System.out.print("Press Enter to continue...");
                input.nextLine();
        }
     }

    public static void viewAnalyticsDashBoard(final String username)
    {
        for (final User user : BankSystem.users)
        {
            if (Objects.equals(BankSystem.getCurrentLoggedInUser(), username))
            {
                BankSystem.clearConsole();
                System.out.print(
                        """
                                ╔═════════════════════════════════════╗
                                ║           Data Analytics            ║
                                ╚═════════════════════════════════════╝"""
                );
                System.out.print(
                        "\nName: " + user.getName() +
                        "\n───────────────────────────────────────"
                );
                if (Objects.equals(user.getProductType(), "Savings Account"))
                {
                    System.out.print(
                            "\nTotal Net worth: " + BankSystem.calculateTotalNet(BankSystem.getCurrentLoggedInUser()) +
                            "\nTotal Interest Earned: " + BankSystem.showInterestEarned(BankSystem.getCurrentLoggedInUser())
                    );
                }
                else if (Objects.equals(getCurrentProductType(getCurrentLoggedInUser()), "Credit Account"))
                {
                    System.out.print(
                            "\nTotal Spent: " + calculateTotalSpent(getCurrentLoggedInUser()) +
                            "\nTotal Paid: " + calculateTotalPaid(username) +
                            "───────────────────────────────────────"
                    );
                }
            }
        }
        System.out.print("Press Enter to continue...");
        input.nextLine();
    }

    public static void handleProductApplication()
    {
        User.applyProduct();
    }

}
