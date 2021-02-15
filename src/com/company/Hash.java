package com.company;

import java.security.MessageDigest;

public class Hash {
    public static String getHash(String in){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(in.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
