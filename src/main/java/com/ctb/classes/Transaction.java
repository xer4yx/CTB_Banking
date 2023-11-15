package com.ctb.classes;

import java.util.Calendar;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Transaction {
    private static final Scanner input = new Scanner(System.in);
    private static final Calendar calendar = Calendar.getInstance();
    private static final Random rand = new Random();
    private String transactionID;
    private String transactionType;
    private String description;
    private double amount;
    private long timeStamp;

    /*----------------------Setter Methods----------------------*/
    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /*----------------------Getter Methods----------------------*/
    public String getTransactionID() {
        return transactionID;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    /*----------------------Class Methods----------------------*/

    protected static String generateTransactionID() {
        {
            long time = calendar.getTimeInMillis();
            int randomNumber = rand.nextInt();
            String timeString = Long.toString(time);
            String randomNumberString = Integer.toString(randomNumber);

            return "TXN" + timeString + randomNumberString;
        }
    }

    protected static boolean depositFunds(String username, double amount) {
        for (User user : BankSystem.users) {
            if (Objects.equals(User.getUsername(), username)) {
                // Check for 2FA within profiles of the user
                if (handleVerification(user))
                    return false;

                Transaction depositTransaction = new Transaction();
                depositTransaction.transactionID = generateTransactionID();
                depositTransaction.transactionType = "Deposit";
                depositTransaction.amount = amount;
                depositTransaction.timeStamp = Calendar.getInstance().getTimeInMillis();

                user.userTransaction.add(depositTransaction);
                user.setBalance(amount);

                BankSystem.saveDataToFile();
                return true;
            }
        }
        return false;
    }

    private static boolean handleVerification(User user) {
        for (final Profile profile : user.userProfile) {
            if (profile.get2FAStatus()) {
                System.out.print("\nSending an OTP for 2 Factor Authentication.");
                SecuritySystem.sendOTP();

                System.out.print("\nEnter your OTP: ");
                String inputOTP = input.nextLine();

                if (!SecuritySystem.verifyOTP(inputOTP)) {
                    System.out.print("\n*Incorrect OTP. Timeout for 30 seconds...");
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        System.err.print(e.getMessage());
                    }
                    return true;
                }
            }
        }
        return false;
    }

    protected static boolean withdrawFunds(String username, double amount) {
        for (User user : BankSystem.users) {
            if (Objects.equals(User.getUsername(), username)) {
                // Check for 2FA within profiles of the user
                if (handleVerification(user))
                    return false;

                if (amount <= 0.0) {
                    System.out.print("*Invalid withdrawal amount. Please enter a positive amount.");
                    return false;
                }

                if (user.getBalance() >= amount) {
                    Transaction withdrawTransaction = new Transaction();
                    withdrawTransaction.transactionID = generateTransactionID(); // Call a function to generate a unique
                                                                                 // transaction ID
                    withdrawTransaction.transactionType = "Withdrawal";
                    withdrawTransaction.amount = amount;
                    withdrawTransaction.timeStamp = Calendar.getInstance().getTimeInMillis();

                    user.userTransaction.add(withdrawTransaction);
                    user.setBalance(user.getBalance() - amount);

                    BankSystem.saveDataToFile();
                    return true;
                } else {
                    System.out.print("\n*Insufficient balance. Withdrawal failed.");
                    return false;
                }
            }
        }
        System.out.print("*User not found. Withdrawal failed.");
        return false;
    }

    protected static boolean makePurchase(String username, double amount, String purchaseDescription) {
        for (User user : BankSystem.users) {
            if (Objects.equals(User.getUsername(), username)) {
                // Check for 2FA within profiles of the user
                if (handleVerification(user))
                    return false;

                if (amount <= 0.0) {
                    System.out.print("*Invalid purchase amount. Please enter a positive amount.");
                    return false;
                }

                // Check if the user's balance will go below -5000 after the purchase
                if (user.getBalance() - amount < -5000.0) {
                    System.out.print("*Insufficient credit limit. Purchase failed.");
                    return false;
                }

                // Update user's transaction history
                Transaction purchaseTransaction = new Transaction();
                purchaseTransaction.transactionID = generateTransactionID();
                purchaseTransaction.transactionType = "Purchase";
                purchaseTransaction.amount = amount;
                purchaseTransaction.timeStamp = Calendar.getInstance().getTimeInMillis();
                purchaseTransaction.description = purchaseDescription;

                user.userTransaction.add(purchaseTransaction);

                // Update user's balance (subtract the purchase amount for a credit card)
                user.setBalance(user.getBalance() - amount);

                // Save the updated user data to the file
                BankSystem.saveDataToFile();
                System.out.print(
                        "Purchase of $" + amount + " successful. " +
                                "Description: " + purchaseDescription);

                return true;
            }
        }
        System.out.print("*User not found. Purchase failed.");
        return false;
    }

    protected static boolean payBills(String username, double amount, String billDescription) {
        for (User user : BankSystem.users) {
            if (Objects.equals(User.getUsername(), username)) {
                if (handleVerification(user))
                    return false;

                if (amount <= 0.0) {
                    System.out.print("*Invalid bill amount. Please enter a positive amount.");
                    return false;
                }
                if (user.getBalance() <= amount) {

                    Transaction billTransaction = new Transaction();
                    billTransaction.transactionID = generateTransactionID();
                    billTransaction.transactionType = "Bill Payment";
                    billTransaction.amount = amount;
                    billTransaction.timeStamp = Calendar.getInstance().getTimeInMillis();
                    billTransaction.description = billDescription;
                    user.userTransaction.add(billTransaction);

                    user.setBalance(amount);
                    BankSystem.saveDataToFile();

                    System.out.print(
                            "Bill payment of $" + amount + " successful. " +
                                    " Description: " + billDescription);

                    return true;
                } else {
                    System.out.print("*Insufficient balance. Bill payment failed.");
                    return false;
                }
            }
        }
        System.out.print("*User not found. Bill payment failed.");
        return false;
    }
}
