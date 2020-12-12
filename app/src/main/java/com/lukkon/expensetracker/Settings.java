package com.lukkon.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    ConstraintLayout settingsLayout;
    TextView settingsSoundTextView;
    RadioGroup radios;
    RadioButton soundOn;
    RadioButton soundOff;
    SharedPreferences prefs;
    boolean sound;
    boolean lightTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsLayout = findViewById(R.id.settingsLayout);
        settingsSoundTextView = findViewById(R.id.settingsSoundTextView);
        radios = findViewById(R.id.settingsRadioGroup);
        soundOn = findViewById(R.id.soundOnRadioButtonSettings);
        soundOff = findViewById(R.id.soundOffRadioButtonSettings);

        prefs = this.getSharedPreferences("com.lukkon.expensetracker", Context.MODE_PRIVATE);
        sound = prefs.getBoolean("sound",true);
        lightTheme = prefs.getBoolean("lightTheme",true);

        if(sound){
            soundOn.setChecked(true);
        }
        else{
            soundOff.setChecked(true);
        }

        if(lightTheme){
            settingsSoundTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            soundOff.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            soundOn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            settingsLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhite));
        }
        else{
            settingsSoundTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            soundOff.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            soundOn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            settingsLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorBlack));
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