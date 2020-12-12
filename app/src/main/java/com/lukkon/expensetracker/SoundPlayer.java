package com.lukkon.expensetracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.provider.MediaStore;

public class SoundPlayer {
    private MediaPlayer login;
    private MediaPlayer logout;
    private MediaPlayer button;
    private SharedPreferences prefs;

    public SoundPlayer(Context contex){
        login = MediaPlayer.create(contex,R.raw.login);
        logout = MediaPlayer.create(contex,R.raw.logout);
        button = MediaPlayer.create(contex,R.raw.button);
        prefs = contex.getSharedPreferences("com.lukkon.expensetracker", Context.MODE_PRIVATE);
    }

    public void playLoginSound(){
        if(prefs.getBoolean("sound",true)){
            login.start();
        }

    }

    public void playLogoutSound(){
        if(prefs.getBoolean("sound",true)){
            logout.start();
        }
    }

    public void playButtonSound(){
        if(prefs.getBoolean("sound",true)){
            button.start();
        }
    }
}
