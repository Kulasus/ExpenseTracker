package com.lukkon.expensetracker.security;

import android.util.Base64;

public class Encoder {
    public static String encode(String text){
        String ecoded = Base64.encodeToString(text.getBytes(),Base64.DEFAULT);
        return ecoded;
    }
}
