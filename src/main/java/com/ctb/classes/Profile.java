package com.ctb.classes;

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

}
