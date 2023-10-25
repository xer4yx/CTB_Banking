package com.ctb.classes;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

class SecuritySystem {
    protected static String encrypt(String password) {
        try {
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md5.digest(password.getBytes());
            BigInteger signum = new BigInteger(1, messageDigest);
            String hashPass = signum.toString(16);

            while(hashPass.length() < 32) {
                hashPass = "0" + hashPass;
            }
            return hashPass;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    protected static boolean enable2FA(final char answer)
    {
        return Character.toUpperCase(answer) == 'Y';
    }

    protected static booleanean authenticateUser(final String username, final String password) {
        auto user_it = find_if(users.begin(), users.end(),
                [&](final User &user)
        { return user.username == username; });

        // User not found.
        if (user_it == users.end())
        {
            return false;
        }

        string decryptedPass = SecuritySys::decryptPass(user_it->password);

        if (!system.attemptLogin(decryptedPass, password))
        {
            return false; // Incorrect password
        }

        // Check for 2FA within profiles of the user
        for (final Profile profile : profiles)
        {
            if (profile.isTwoFactorEnabled)
            {
                cout << "\n---Sending an OTP for 2 Factor Authentication---" << endl;
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
        Session.saveSession(username, "Login");
        return true; // Successful authentication
    }

    protected static string getcurrDate()
    {
        LocalDate currTime = LocalDate.now();
        struct tm tstruct{};
        char buf[80];
        tstruct = *localtime(&currTime);
        strftime(buf, sizeof(buf), "%Y-%m-%d %X", &tstruct);

        return buf;
    }

    protected static boolean securityStatus(final boolean status)
    {
        if (status)
            return true;

        return false;
    }

    protected static void auditLog(final boolean status)
    {
        try
        {
            ofstream auditFile("audit_log.txt");
            if (!auditFile)
            {
                cerr << "Error: Unable to open file for audit." << endl;
                return;
            }

            boolean currentStatus = securityStatus(status);
            string statusResult = currentStatus ? "PASSED" : "FAILED";

            auditFile << "[" << getcurrDate() << "]: " << statusResult << endl;

            auditFile.close();
        }
        catch (Exception e)
        {
            System.err.print(e.getMessage());
        }
    }
}
