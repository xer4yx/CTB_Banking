package com.ctb.classes;

import com.ctb.interfaces.TransactionInterface;

import java.time.Instant;

class Transaction {
    private String transactionID;
    private String transactionType;
    private String description;
    private double amount;
    private final long timeStamp = Instant.now().getEpochSecond();

    /*----------------------Setter Methods----------------------*/
    public void setTransactionID(String transactionID) {this.transactionID = transactionID;}
    public void setTransactionType(String transactionType) {this.transactionType = transactionType;}
    public void setDescription(String description) {this.description = description;}
    public void setAmount(double amount) {this.amount = amount;}

    /*----------------------Getter Methods----------------------*/
    public String getTransactionID() {return transactionID;}
    public String getTransactionType() {return transactionType;}
    public String getDescription() {return description;}
    public double getAmount() {return amount;}
    protected long getTimeStamp() {return timeStamp;}

    /*----------------------Class Methods----------------------*/

    protected void generateTransactionID() {
        {
            // Implement your logic to generate a unique transaction ID
            // Example: You can use a combination of timestamp and a random number
            return "TXN" + to_string(time(nullptr)) + to_string(rand());
        }
    }

    protected static boolean depositFunds(String username, double amount) {
        for (User &user : users)
        {
            if (user.username == username)
            {
                // Check for 2FA within profiles of the user
                for (const Profile &profile : user.profiles)
                {
                    if (profile.isTwoFactorEnabled)
                    {
                        cout << "\nSending an OTP for 2 Factor Authentication." << endl;
                        system.sendOTP();

                        string inputOTP;
                        cout << "\nEnter your OTP: ";
                        cin >> inputOTP;
                        cin.ignore();

                        if (!system.verifyOTP(inputOTP))
                        {
                            cout << "\n*Incorrect OTP. Timeout for 30 seconds..." << endl;
                            sleep_for(seconds(30));
                            return false;
                        }
                    }
                }

                // Update user's transaction history
                Transaction depositTransaction;
                depositTransaction.transactionID = generateTransactionID(); // Call a function to generate a unique transaction ID
                depositTransaction.transactionType = "Deposit";
                depositTransaction.amount = amount;
                depositTransaction.timestamp = time(nullptr);

                user.transactionhistory.push_back(depositTransaction);

                // Update user's balance
                user.balance += amount;

                // Save the updated user data to the file
                saveDataToFile();

                return true;
            }
        }
        return false;
    }

    protected static boolean withdrawFunds(String username, double amount) {
        for (User &user : users)
        {
            if (user.username == username)
            {
                // Check for 2FA within profiles of the user
                for (const Profile &profile : user.profiles)
                {
                    if (profile.isTwoFactorEnabled)
                    {
                        cout << "\nSending an OTP for 2 Factor Authentication." << endl;
                        system.sendOTP();

                        string inputOTP;
                        cout << "\nEnter your OTP: ";
                        cin >> inputOTP;

                        if (!system.verifyOTP(inputOTP))
                        {
                            cout << "\n*Incorrect OTP. Timeout for 30 seconds..." << endl;
                            sleep_for(seconds(30));
                            return false;
                        }
                    }
                }

                if (amount <= 0.0)
                {
                    cout << "*Invalid withdrawal amount. Please enter a positive amount." << endl;
                    return false;
                }

                if (user.balance >= amount)
                {
                    // Update user's transaction history
                    Transaction withdrawTransaction;
                    withdrawTransaction.transactionID = generateTransactionID(); // Call a function to generate a unique transaction ID
                    withdrawTransaction.transactionType = "Withdrawal";
                    withdrawTransaction.amount = amount;
                    withdrawTransaction.timestamp = time(nullptr);

                    user.transactionhistory.push_back(withdrawTransaction);

                    // Update user's balance
                    user.balance -= amount;

                    // Save the updated user data to the file
                    saveDataToFile();

                    return true;
                }
                else
                {
                    cout << "\n*Insufficient balance. Withdrawal failed." << endl;
                    return false;
                }
            }
        }

        cout << "*User not found. Withdrawal failed." << endl;
        return false;
    }

    protected static boolean makePurchase(String username, double amount, String purchaseDescription) {
        for (User &user : users)
        {
            if (user.username == username)
            {
                // Check for 2FA within profiles of the user
                for (const Profile &profile : user.profiles)
                {
                    if (profile.isTwoFactorEnabled)
                    {
                        cout << "\nSending an OTP for 2 Factor Authentication." << endl;
                        system.sendOTP();

                        string inputOTP;
                        cout << "\nEnter your OTP: ";
                        cin >> inputOTP;

                        if (!system.verifyOTP(inputOTP))
                        {
                            cout << "\n*Incorrect OTP. Timeout for 30 seconds..." << endl;
                            sleep_for(seconds(30));
                            return false;
                        }
                    }
                }

                if (amount <= 0.0)
                {
                    cout << "*Invalid purchase amount. Please enter a positive amount." << endl;
                    return false;
                }

                // Check if the user's balance will go below -5000 after the purchase
                if (user.balance - amount < -5000.0)
                {
                    cout << "*Insufficient credit limit. Purchase failed." << endl;
                    return false;
                }

                // Update user's transaction history
                Transaction purchaseTransaction;
                purchaseTransaction.transactionID = generateTransactionID();
                purchaseTransaction.transactionType = "Purchase";
                purchaseTransaction.amount = amount;
                purchaseTransaction.timestamp = time(nullptr);
                purchaseTransaction.description = purchaseDescription;

                user.transactionhistory.push_back(purchaseTransaction);

                // Update user's balance (subtract the purchase amount for a credit card)
                user.balance -= amount;

                // Save the updated user data to the file
                saveDataToFile();
                cout << endl;
                cout << "Purchase of $" << amount << " successful. " << endl;
                cout << "Description: " << purchaseDescription << endl;

                return true;
            }
        }
        cout << endl;
        cout << "*User not found. Purchase failed." << endl;
        return false;
    }

    protected static boolean payBills(String username, double amount, String billDescription) {
        for (User &user : users)
        {
            if (user.username == username)
            {
                for (const Profile &profile : user.profiles)
                {
                    if (profile.isTwoFactorEnabled)
                    {
                        cout << "\nSending an OTP for 2 Factor Authentication." << endl;
                        system.sendOTP();

                        string inputOTP;
                        cout << "\nEnter your OTP: ";
                        cin >> inputOTP;

                        if (!system.verifyOTP(inputOTP))
                        {
                            cout << "\n*Incorrect OTP. Timeout for 30 seconds..." << endl;
                            sleep_for(seconds(30));
                            return false;
                        }
                    }
                }

                if (amount <= 0.0)
                {
                    cout << "*Invalid bill amount. Please enter a positive amount." << endl;
                    return false;
                }
                if (user.balance <= amount)
                {
                    // Update user's transaction history
                    Transaction billTransaction;
                    billTransaction.transactionID = generateTransactionID();
                    billTransaction.transactionType = "Bill Payment";
                    billTransaction.amount = amount;
                    billTransaction.timestamp = time(nullptr);
                    billTransaction.description = billDescription;
                    user.transactionhistory.push_back(billTransaction);
                    // Update user's balance
                    user.balance += amount;
                    // Save the updated user data to the file
                    saveDataToFile();

                    cout << endl;
                    cout << "Bill payment of $" << amount << " successful. " << endl;
                    cout << " Description: " << billDescription << endl;
                    return true;
                }
                else
                {
                    cout << endl;
                    cout << "*Insufficient balance. Bill payment failed." << endl;
                    return false;
                }
            }
        }
        cout << "*User not found. Bill payment failed." << endl;
        return false;
    }
}
