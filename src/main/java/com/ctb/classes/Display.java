package com.ctb.classes;

import java.util.Objects;
import java.util.Scanner;

import static com.ctb.classes.BankSystem.setCurrentLoggedInUser;

public class Display {
    private final Scanner input = new Scanner(System.in);
    public void displayMainMenu()
    {
        System.out.println(
                """
                        ╔══════════════════════════════════════╗
                        ║      ╔═══════════════════════╗       ║
                        ║      ║   CENTRAL TRUST BANK  ║       ║
                        ║      ╚═══════════════════════╝       ║
                        ╚══════════════════════════════════════╝
                                                              \s
                        ┌──────────────────────────────────────┐
                        │ ┌──────────────────────────────────┐ │
                        │ │  1. Login                        │ │
                        │ │  2. Product Application          │ │
                        │ │  3. Forgot Password              │ │
                        │ │  4. Exit                         │ │
                        │ └──────────────────────────────────┘ │
                        └──────────────────────────────────────┘"""
        );

        System.out.println("Enter your choice: ");
    }

     protected boolean loginUser(String loggedInUsername)
     {
        BankSystem.clearConsole();
         System.out.println(
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
        System.out.println("Enter username:");
        username = input.nextLine();
        System.out.println("Enter password:");
        password = input.nextLine();

        if (SecuritySystem.authenticateUser(username, password))
        {
            loggedInUsername = username;

            setCurrentLoggedInUser(loggedInUsername);
            System.out.println(
                    """
                            
                            ---Login successful!---
                            
                            """
            );
            System.out.println("Press Enter to continue...");
            input.nextLine();
            BankSystem.clearConsole();
            return true;
        }
        else
        {
            System.out.println(
                    """
                            
                            *Invalid username or password. Please try again.
                            
                            """
            );
            System.out.println("Press Enter to continue...");
            input.nextLine();
            BankSystem.clearConsole();
            return false;
        }
    }

    protected void logout(String username)
    {
        for (User user : BankSystem.users)
        {
            if (User.getUsername().equals(username))
            {
                Session.saveSession(username, "Logout");
                System.out.println("Logged out successfully.");
                break;
            }
        }
    }

     void forgotPassword() 
     {
         BankSystem.clearConsole();
         System.out.println(
                 """
                         ╭────────────────────────────────────────────────────────────╮
                         │                     Forgot Password                        │
                         ╰────────────────────────────────────────────────────────────╯"""
         );
         char choice;
         System.out.println("\nEnter your email: ");
         String email = input.nextLine();
         boolean emailFound = false; // To track whether the email was found or not
         
         for (User user : BankSystem.users) 
         {
            for (Profile profile : user.userProfile)
            {
                if (Objects.equals(profile.getEmail(), email))
                {
                    System.out.println("---Email found!---");
                    System.out.println("\nSending an OTP for " + profile.getEmail() + " 2 Factor Authentication.");
                    SecuritySystem.sendOTP();
                    System.out.println("\nEnter your OTP: ");
                    String inputOTP = input.nextLine();
                    if (!SecuritySystem.verifyOTP(inputOTP))
                    {
                        System.out.println("\n*Incorrect OTP. Timeout for 30 seconds...");
                        try {
                            Thread.sleep(30000);
                        } catch (InterruptedException e) {
                            System.err.print(e.getMessage());
                        }
                        return;
                    }
                    System.out.println(
                            """
                                    ──────────────────────────────────────────────────────────────
                                    Enter new password:\s
                                    """
                    );
                    String newpass = input.nextLine();
                    User.changePassword(User.getUsername(), newpass);
                    System.out.println("---Password changed successfully!---");
                    emailFound = true;
                }
            }
         }

        if (!emailFound)
        {
            System.out.println("\n*Email not found. Please try again.");
        }
    }

     void displayDashboardMenu(final String username)
    {
        for (final User user : BankSystem.users)
        {
            if (Objects.equals(User.getUsername(), username))
            {
                if (User.isAdmin())
                {
                    BankSystem.clearConsole();
                    System.out.println(
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
                    System.out.println(
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
                    System.out.println(
                            """
                                    
                                    ╭─────────────────────────────────────╮
                                    │         CENTRAL TRUST BANK          │
                                    ╰─────────────────────────────────────╯
                                    """
                    );
                    System.out.println();
                    System.out.println("Welcome " + BankSystem.getCurrentLoggedInUser() + "!");
                    System.out.println("Current Balance: $" + BankSystem.getCurrentBalance(BankSystem.getCurrentLoggedInUser()));
                    System.out.println(
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

     void handleDashboardOptions(final String username)
    {
        while (true)
        {
            displayDashboardMenu(username);
            String productType = BankSystem.getCurrentProductType(BankSystem.getCurrentLoggedInUser());
            int choice = input.nextInt();
            input.nextLine();
            if (User.isAdmin())
            {
                switch (choice)
                {
                    case 1:
                        Admin.handleManageUsers(username);
                        input.nextLine();
                        break;
                    case 2:
                        BankSystem.clearConsole();
                        CustomerService.displayAllHR();
                        CustomerService.replyToHelp();
                        break;
                    case 3:
                        System.out.println("Logging out...");
                        logout(username);
                        setCurrentLoggedInUser("");
                       "
                      Press Enter to continue...";
                        input.nextLine();
                BankSystem.clearConsole();
                        return;
                    default:
                      *Invalid choice. Please select a valid option."
                }
            }
            else if (iscustomerservice(username))
            {
                switch (choice)
                {
                    case 1:
                BankSystem.clearConsole();
                        displayAllHelpAndResources();
                        replyHelpAndResources();
                        break;
                    case 2:
                        // Logout the user
                      Logging out..."
                        logout(username);
                        setCurrentLoggedInUser("");
                       "
                      Press Enter to continue...";
                        input.nextLine();
                BankSystem.clearConsole();
                        return;
                    default:
                      *Invalid choice. Please select a valid option."
                }
            }
            else
            {
                switch (choice)
                {
                    case 1:
                        handleProductOptions(productType, username);
                        break;
                    case 2:
                        displayProfile(username);
                        break;
                    case 3:
                        viewAnalyticsDashBoard(username);
                        break;
                    case 4:
                        handleHelpAndResources(username);
                        break;
                    case 5:
                        // Logout the user
                      \nLogging out..."
                        logout(username);
                        setCurrentLoggedInUser("");
                      ──────────────────────────────────"
                      Press Enter to continue...";
                        input.nextLine();
                BankSystem.clearConsole();
                        return;
                    default:
                      *Invalid choice. Please select a valid option."
                }
            }
        }
    }

     void handleProductOptions(final String producttype, final String username)
    {
        if (producttype == "Savings Account")
        {
            displaySavingsMenu(username);
        }
        else if (producttype == "Credit Account")
        {
            displayCreditMenu(username);
        }
    }

     void displaySavingsMenu(final String username)
    {
        handleTransactionCenter(username);
    }

     void displayCreditMenu(final String username)
    {
        handleCreditCenter(username);
    }

     void displayTransactionMenu(final String username)
    {
    BankSystem.clearConsole();
       "
      ╔═════════════════════════════════════╗ 
      ║         Transaction Center:         ║ 
      ╠═════════════════════════════════════╣ 
      ║  1. Deposit Funds                   ║  
      ║  2. Withdraw Funds                  ║  
      ║  3. View Transaction History        ║  
      ║  4. Back to Dashboard               ║ 
      ╚═════════════════════════════════════╝ 
       "
      Enter your choice: ";
        setCurrentLoggedInUser(username);
    }

     void displayTransactionCredit(final String username)
    {
    BankSystem.clearConsole();
       "
      ╔═════════════════════════════════════╗ 
      ║         Transaction Center:         ║ 
      ╠═════════════════════════════════════╣ 
      ║  1. Make a Purchase                 ║  
      ║  2. Pay Bills                       ║  
      ║  3. View Transaction History        ║  
      ║  4. Back to Dashboard               ║ 
      ╚═════════════════════════════════════╝  
       "
      Enter your choice: ";
        setCurrentLoggedInUser(username);
    }

     static void displayTransactionHistory(final String username)
    {
        for (final User user : BankSystem.users)
        {
            if (user.username == username)
            {
              ╔═════════════════════════════════════╗ 
              ║        Transaction History          ║ 
              ╚═════════════════════════════════════╝ 
               User: " + user.username
              ───────────────────────────────────────"
                for (final Transaction transaction : user.transactionhistory)
                {
                  Transaction ID: " + transaction.transactionID
                  Transaction Type: " + transaction.transactionType
                  Amount: $" + transaction.amount
                  Timestamp: " + ctime(transaction.timestamp);
                  Description: " + transaction.description
                  ───────────────────────────────────────"
                }
            }
        }
    }

     void handleTransactionCenter(final String username)
    {
        while (true)
        {
            displayTransactionMenu(username);

            int transactionChoice;
            cin >> transactionChoice;
            input.nextLine();

            switch (transactionChoice)
            {
                case 1:
                    processDeposit(username);
                    break;
                case 2:
                    processWithdrawal(username);
                    break;
                case 3:;
            BankSystem.clearConsole();
                    displayTransactionHistory(username);
                   "
                  Press Enter to continue...";
                    input.nextLine();
                    break;
                case 4:
                    return; // Return to the dashboard
                default:
                  *Invalid choice. Please select a valid option."
            }
        }
    }

     void handleCreditCenter(final String username)
    {
        while (true)
        {
            displayTransactionCredit(username);

            int transactionChoice;
            cin >> transactionChoice;
            input.nextLine();

            switch (transactionChoice)
            {
                case 1:
                    processPurchase(username);
            BankSystem.clearConsole();
                    break;
                case 2:
                    processPayBills(username);
            BankSystem.clearConsole();
                    break;
                case 3:
            BankSystem.clearConsole();
                    displayTransactionHistory(username);
                   "
                  Press Enter to continue...";
                    input.nextLine();
                    break;
                case 4:
                    return;
                default:
                  *Invalid choice. Please select a valid option."
            }
        }
    }

     void handleHelpAndResources(final String username)
    {
    BankSystem.clearConsole();
       "
      ╔═════════════════════════════════════╗ 
      ║          Help  Resources           ║ 
      ╠═════════════════════════════════════╣ 
      ║  1. Chat with AI Assistant          ║  
      ║  2. Contact US                      ║  
      ║  3. Back to Dashboard               ║  
      ╚═════════════════════════════════════╝  
       "
      Enter your choice: ";

        int jhchoice;
        String message;
        cin >> jhchoice;
        cin.ignore(numeric_limits<streamsize>::max(), '\n');
         endl;
        switch (jhchoice)
        {
            case 1:
              \nHi! I'm your AI Assistant. How may I help you?\n"
                       
              Enter inquiry: ";
                getline(cin, message);
                chatBot(message, username);
               "
              Press Enter to continue...";
                input.nextLine();
                break;
            case 2:
        BankSystem.clearConsole();
               "
              ╭────────────────────────────────────────────────╮"
              │                  Contact Us                    │"
              ├────────────────────────────────────────────────┤"
              │  Email: Uniportal@proton.me                    │"
              │  Phone: 1-800-123-4567                         │"
              │  Address: 123 Main St, New York, NY 10001      │"
              ╰────────────────────────────────────────────────╯"
                                                             
              ╭────────────────────────────────────────────────╮      
              │  1. Send a message                             │      
              │  2. Back to Dashboard                          │      
              ╰────────────────────────────────────────────────╯      
               "
                int schoice;
              Enter your choice: ";
                cin >> schoice;
               "
                if (schoice == 1)
                {
                    askHelp(username);
                  \nMessage sent successfully!"
                   "
                  Press Enter to continue...";
                    input.nextLine();
                }
                else if (schoice == 2)
                {
                    break;
                }
                else
                {
                  *Invalid choice. Please select a valid option."
                    return;
                }
                break;
            case 3:
                return;
            default:
              Press Enter to continue...";
                input.nextLine();
                return;
        }
    }

     void viewAnalyticsDashBoard(final String username)
    {
        for (final User user : BankSystem.users)
        {
            if (user.username == username)
            {
            BankSystem.clearConsole();
               "
              ╔═════════════════════════════════════╗ 
              ║           Data Analytics            ║
              ╚═════════════════════════════════════╝
               Name: " + user.name
              ───────────────────────────────────────"
                if (user.producttype == "Savings Account")
                {
                  Total Networth: " + showTotalNetworth(username)
                  Total Interest Earned: " + showInterestEarned(username)
                }
                else if (user.producttype == "Credit Account")
                {
                  Total Spent: " + showtotalSpent(username)
                  Total Paid: " + showtotalPaid(username)
                }

              ───────────────────────────────────────"
            }
        }
       "
      Press Enter to continue...";
        input.nextLine();
    }
}
