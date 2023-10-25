package com.ctb.classes;

import java.util.Objects;

public class Dashboard {
    private String dashboardID;
    private String dashboardContent;

    /*----------------------Setter Methods----------------------*/
    public void setDashboardID(String dashboardID) {this.dashboardID = dashboardID;}
    public void setDashboardContent(String dashboardContent) {this.dashboardContent = dashboardContent;}

    /*----------------------Getter Methods----------------------*/
    public String getDashboardID() {return dashboardID;}
    public String getDashboardContent() {return dashboardContent;}

    /*----------------------Class Methods----------------------*/
    public void displayDashboard(String username) {
        for (final User user : BankSystem.users)
        {
            if (Objects.equals(User.getUsername(), username))
            {
                if (user.isAdmin())
                {
                BankSystem.clearConsole();
                    System.out.println(
                            """

                                    ╔═════════════════════════════════════╗
                                    ║            Administrator            ║
                                    ╚═════════════════════════════════════╝
                                                                          \s
                                    ╔═════════════════════════════════════╗
                                    ║         Dashboard Options:          ║
                                    ╠═════════════════════════════════════╣
                                    ║  1. Manage BankSystem.users         ║
                                    ║  2. Help & Resources                ║
                                    ║  3. Logout                          ║
                                    ╚═════════════════════════════════════╝"""
                    );
                    System.out.println("Enter your choice: ");
                }
                else if (user.isCustomerService())
                {
                BankSystem.clearConsole();
                    System.out.println(
                            """

                                    ╔═════════════════════════════════════╗
                                    ║          Customer Service           ║
                                    ╚═════════════════════════════════════╝
                                                                          \s
                                    ╔═════════════════════════════════════╗
                                    ║         Dashboard Options:          ║
                                    ╠═════════════════════════════════════╣
                                    ║  1. Messages                        ║
                                    ║  2. Logout                          ║
                                    ╚═════════════════════════════════════╝"""
                    );

                    System.out.println("Enter your choice: ");
                }
                else
                {
                BankSystem.clearConsole();
                    SetConsoleOutputCP(CP_UTF8);
                    " "
                    "╭─────────────────────────────────────╮"
                    "│         CENTRAL TRUST BANK          │"
                    "╰─────────────────────────────────────╯"
                    " "
                    " Welcome " << user.name << "!"
                    "  "
                    " Current Balance: $" << getCurrentBalance(username)
                    "                                           "
                    "╔═════════════════════════════════════╗    "
                    "║         Dashboard Options:          ║     "
                    "╠═════════════════════════════════════╣    "
                    "║  1. Transaction Center              ║     "
                    "║  2. User Profile                    ║     "
                    "║  3. Data Analytics Dashboard        ║     "
                    "║  4. Help & Resources                ║     "
                    "║  5. Logout                          ║     "
                    "╚═════════════════════════════════════╝"
                    " "
                    "Enter your choice: ";
                }
            }
        }
    }
}
