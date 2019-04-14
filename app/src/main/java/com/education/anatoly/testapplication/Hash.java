package com.education.anatoly.testapplication;

import java.security.MessageDigest;

/**
 * Created by Anatoly on 12.04.2019.
 */

public class Hash {
    public static String SHA1(String str)
    {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            sha1.update(str.getBytes());

            byte[] res = sha1.digest();
            StringBuilder hash = new StringBuilder(res.length*2);
            String s;
            for (int i = 0; i < res.length; i++) {
                s = Integer.toHexString(0xff & res[i]);
                hash.append(s.length() == 1 ? "0"+s : s);
            }

            return hash.toString();
        }
        catch (Exception e){}
        return null;
    }
}
