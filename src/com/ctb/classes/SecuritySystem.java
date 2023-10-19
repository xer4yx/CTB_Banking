package com.ctb.classes;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecuritySystem {
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

    private boolean authenticateUser(final String username, final String password) {return true;}

}
