package com.ctb.classes;

import java.util.Objects;

public class Profile{
    private static String email;
    private static String phoneNumber;
    private static boolean is2FAEnabled;

    /*----------------------Setter Methods----------------------*/
    public void setEmail(String email) {this.email = email;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public void set2FAStatus(boolean is2FAEnabled) {this.is2FAEnabled = is2FAEnabled;}

    /*----------------------Getter Methods----------------------*/
    public static String getEmail() {return email;}
    public static String getPhoneNumber() {return phoneNumber;}
    public static boolean get2FAStatus() {return is2FAEnabled;}

    /*----------------------Class Methods----------------------*/
    public static void displayProfile(String username) {
        //TODO: transfer to User
        //CONVERT: List -> Database
        for (final User user : BankSystem.users)
        {
            if (Objects.equals(User.getUsername(), username))
            {
                String show2FAStatus = get2FAStatus() ? "Enabled" : "Disabled";
                System.out.print(
                        "\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" +
                        "\n                      User Profile                            " +
                        "\n══════════════════════════════════════════════════════════════" +
                        "\n  Name: " + user.getName() +
                        "\n  Username: " + User.getUsername() +
                        "\n  Email: " + getEmail() +
                        "\n  Phone: " + getPhoneNumber() +
                        "\n  Account: " + user.getProductType() +
                        "\n  Balance: " + user.getBalance() +
                        "\n  Two Factor Authentication: " + show2FAStatus +
                        "\n══════════════════════════════════════════════════════════════");
                user.displayUserSettings(User.getUsername());
            }
        }
    }
}
