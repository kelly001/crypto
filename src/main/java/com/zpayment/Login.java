package com.zpayment;

import database.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Login {
    protected static User user;

    public static boolean authenticate(String username, String password) {
        try {
            user = User.loadByEmail(username);
            if (user != null &&  user.getPassword().equals(Login.getSecurePassword(password, user.getSalt()))) {
                return true;
            }
        } catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String getSecurePassword(String passwordToHash, String salt)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(salt.getBytes());

            byte[] bytes = md.digest(passwordToHash.getBytes());

            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public static String getRandomSalt()
    {
        byte[] salt = new byte[16];
        try {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");

        sr.nextBytes(salt);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return salt.toString();
    }
}
