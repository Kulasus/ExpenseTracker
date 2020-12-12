package com.lukkon.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Settings extends AppCompatActivity {

    RadioGroup radios;
    RadioButton soundOn;
    RadioButton soundOff;
    SharedPreferences prefs;
    boolean sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        radios = findViewById(R.id.settingsRadioGroup);
        soundOn = findViewById(R.id.soundOnRadioButtonSettings);
        soundOff = findViewById(R.id.soundOffRadioButtonSettings);

        prefs = this.getSharedPreferences("com.lukkon.expensetracker", Context.MODE_PRIVATE);
        sound = prefs.getBoolean("sound",true);

        if(sound){
            soundOn.setChecked(true);
        }
        else{
            soundOff.setChecked(true);
        }

        radios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedButton = group.findViewById(checkedId);
                if(checkedButton.getText().toString().equals("On")){
                    prefs.edit().putBoolean("sound",true).apply();
                }
                else if(checkedButton.getText().toString().equals("Off")){
                    prefs.edit().putBoolean("sound",false).apply();
                }
            }
        });
    }


}