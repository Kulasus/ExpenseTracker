package com.lukkon.expensetracker.security;

import android.util.Base64;

public class Decoder {
    public static String decode(String text){
        String decoded = new String(Base64.decode(text.getBytes(),Base64.DEFAULT));
        return decoded.toString();
    }
}
