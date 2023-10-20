package com.ctb.classes;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class SecuritySystem {
    public String encrypt(String password) {
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

    protected static boolean authenticateUser(final String username, final String password) {
        auto user_it = find_if(users.begin(), users.end(),
                [&](const User &user)
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

}
