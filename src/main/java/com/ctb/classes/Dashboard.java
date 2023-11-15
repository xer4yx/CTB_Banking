package com.ctb.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

//TODO: Obsolete file
public class Dashboard {
    private String dashboardID;
    private String dashboardContent;

    /*----------------------Setter Methods----------------------*/
    public void setDashboardID(String dashboardID) {
        this.dashboardID = dashboardID;
    }

    public void setDashboardContent(String dashboardContent) {
        this.dashboardContent = dashboardContent;
    }

    /*----------------------Getter Methods----------------------*/
    public String getDashboardID() {
        return dashboardID;
    }

    public String getDashboardContent() {
        return dashboardContent;
    }

    /*----------------------Class Methods----------------------*/
    public void displayDashboard(String username) {
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getBoolean("is_admin")) {
                    System.out.print("\033[H\033[2J");
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
                                    ╚═════════════════════════════════════╝""");
                    System.out.print("Enter your choice: ");
                } else if (rs.getBoolean("is_customerservice")) {
                    System.out.print("\033[H\033[2J");
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
                                    ╚═════════════════════════════════════╝""");

                    System.out.print("Enter your choice: ");
                } else {
                    System.out.print("\033[H\033[2J");
                    System.out.print(
                            "\n╭─────────────────────────────────────╮" +
                                    "\n│         CENTRAL TRUST BANK          │" +
                                    "\n╰─────────────────────────────────────╯" +
                                    "\n                                       " +
                                    "\n Welcome " + username + "!" +
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
                                    "\n╚═════════════════════════════════════╝");
                    System.out.print("Enter your choice: ");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
