package com.ctb.classes;

import java.util.Objects;
import java.util.Scanner;

class CustomerService extends User{
    private static final Scanner input = new Scanner(System.in);
    protected static void displayDashboardMenu(final String username) {
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
    
    protected static void displayHelpHistory(final String username) {
        boolean helpFound = false;

        for (final User user : BankSystem.users)
        {
            if (Objects.equals(User.getUsername(), username))
            {
                System.out.print(
                        """

                                ╔═════════════════════════════════════════════════════════════╗
                                ║                        Help History                         ║
                                ╚═════════════════════════════════════════════════════════════╝
                                ───────────────────────────────────────────────────────────────"""
                );
                for (final HelpAndResources resources : user.userHelpAndResources)
                {
                    System.out.print(
                            "\nHelp ID: " + resources.getHelpID() +
                            "\nType: " + resources.getH_rType() +
                            "\nDescription: " + resources.getH_rDescription()
                    );
                    if (!Objects.equals(resources.getFeedback(), "")) {
                        System.out.print(
                                "\nFeedback: " + resources.getFeedback() +
                                "\n───────────────────────────────────────────────────────────────"
                        );

                    } else {
                        System.out.print(
                                """
                                        Feedback: No feedback yet.
                                        ───────────────────────────────────────────────────────────────"""
                        );

                    }
                    helpFound = true;
                }
            }
        }
        if (!helpFound)
        {
            System.out.print("No Help history is available for the user " + username + ".");
        }
    }

    protected static void displayAllHR() {
        System.out.print(
                """

                        ╭───────────────────────────────────────────╮
                        │             Help & Resources              │
                        ╰───────────────────────────────────────────╯
                        ────────────────────────────────────────────"""
        );

        final String desiredType = "Help"; // Change this to the type you want to display
        boolean helpFound = false;            // Initialize a boolean flag to check if any Help is found

        for (final User user : BankSystem.users)
        {
            for (final HelpAndResources resources : user.userHelpAndResources)
            {
                if (Objects.equals(resources.getH_rType(), desiredType))
                {
                    System.out.print(
                            "\nHelp ID: " + resources.getHelpID() +
                            "\nType: " + resources.getH_rType() +
                            "\nDescription: " + resources.getH_rDescription() +
                            "\nFeedback: " + resources.getFeedback() +
                            "────────────────────────────────────────────"
                    );
                    helpFound = true; // Set the flag to true if Help is found
                }
            }
        }

        if (!helpFound)
        {
            System.out.print("\nNo Help is available.");
        }
    }

    protected static void replyToHelp() {
        String helpID;
        System.out.print("\nEnter the help ID of the help and resources to reply to: ");
        helpID = input.nextLine();
        input.nextLine();
        
        System.out.print("Enter your feedback: ");
        String feedback = input.nextLine();
        

        for (final User user : BankSystem.users)
        {
            for (final HelpAndResources resources : user.userHelpAndResources)
            {
                if (Objects.equals(resources.getHelpID(), helpID))
                {
                    resources.setFeedback(feedback);
                    System.out.print("Feedback saved successfully.");
                    BankSystem.saveDataToFile();
                    return;
                }
            }
        }
        System.out.print("Help and resources with ID '" + helpID + "' not found.");
    }
}
