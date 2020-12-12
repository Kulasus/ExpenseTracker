package com.lukkon.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddExpense extends AppCompatActivity {

    EditText title;
    EditText amount;
    EditText description;
    RadioGroup radios;
    RadioButton selectedRadio;
    DBHelper db;
    SharedPreferences prefs;
    SoundPlayer soundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        title = findViewById(R.id.titleEditTextAdding);
        amount = findViewById(R.id.amountEditTextAdding);
        description = findViewById(R.id.descriptionMultilineTextAdding);
        radios = findViewById(R.id.radioGroup);
        db = new DBHelper(this);
        prefs = this.getSharedPreferences("com.lukkon.expensetracker", Context.MODE_PRIVATE);
        soundPlayer = new SoundPlayer(this);
    }
    public void onConfirmButtonClick(View view){
        soundPlayer.playButtonSound();
        try{
            selectedRadio = findViewById(radios.getCheckedRadioButtonId());
            String userUsernameString = prefs.getString("loggedUserUsername", "ERR");
            String categoryNameString = selectedRadio.getText().toString();
            String titleString = title.getText().toString();
            String descriptionString = description.getText().toString();
            int amountInt = Integer.parseInt(amount.getText().toString());
            if(titleString.length() <= 0 || amountInt <= 0){
                Toast.makeText(this, "Invalid parameters.", Toast.LENGTH_LONG).show();
            }
            else{
                db.insertExpense(userUsernameString,categoryNameString,amountInt,titleString,descriptionString);
                this.finish();
            }
        }
        catch(Exception e){
            Toast.makeText(this, "Invalid parameters.", Toast.LENGTH_SHORT).show();
        }
    }
}