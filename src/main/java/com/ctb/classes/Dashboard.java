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
                    System.out.print(
                            """

                                    ╔═════════════════════════════════════╗
                                    ║            Administrator            ║
                                    ╚═════════════════════════════════════╝
                                    ╔═════════════════════════════════════╗
                                    ║         Dashboard Options:          ║
                                    ╠═════════════════════════════════════╣
                                    ║  1. Manage BankSystem.users         ║
                                    ║  2. Help & Resources                ║
                                    ║  3. Logout                          ║
                                    ╚═════════════════════════════════════╝"""
                    );
                    System.out.print("Enter your choice: ");
                }
                else if (user.isCustomerService())
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
                                    ╚═════════════════════════════════════╝"""
                    );

                    System.out.print("Enter your choice: ");
                }
                else
                {
                BankSystem.clearConsole();
                    System.out.print(
                            "\n╭─────────────────────────────────────╮" +
                            "\n│         CENTRAL TRUST BANK          │" +
                            "\n╰─────────────────────────────────────╯" +
                            "\n                                       " +
                            "\n Welcome " + user.getName() + "!" +
                            "\n                                       " +
                            "\n Current Balance: $" + BankSystem.getCurrentBalance(username) +
                            "\n                                       " +
                            "\n╔═════════════════════════════════════╗" +
                            "\n║         Dashboard Options:          ║" +
                            "\n╠═════════════════════════════════════╣" +
                            "\n║  1. Transaction Center              ║" +
                            "\n║  2. User Profile                    ║" +
                            "\n║  3. Data Analytics Dashboard        ║" +
                            "\n║  4. Help & Resources                ║" +
                            "\n║  5. Logout                          ║" +
                            "\n╚═════════════════════════════════════╝"
                    );
                    System.out.print("Enter your choice: ");
                }
            }
        }
    }
}
