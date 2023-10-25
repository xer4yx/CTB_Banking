package com.ctb.classes;

import java.util.Objects;

public class Profile{
    private String email;
    private String phoneNumber;
    private boolean is2FAEnabled;

    /*----------------------Setter Methods----------------------*/
    public void setEmail(String email) {this.email = email;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public void set2FAStatus(boolean is2FAEnabled) {this.is2FAEnabled = is2FAEnabled;}

    /*----------------------Getter Methods----------------------*/
    public String getEmail() {return this.email;}
    public String getPhoneNumber() {return this.phoneNumber;}
    public boolean get2FAStatus() {return this.is2FAEnabled;}

    /*----------------------Class Methods----------------------*/
    public void displayProfile(String username) {
        for (final User user : BankSystem.users)
        {
            if (Objects.equals(User.getUsername(), username))
            {
                String show2FAStatus = get2FAStatus() ? "Enabled" : "Disabled";
                System.out.println(
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
