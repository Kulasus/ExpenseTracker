package com.lukkon.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lukkon.expensetracker.dataObjects.User;

public class Registration extends AppCompatActivity {

    Thread thread = new Thread(){
        @Override
        public void start(){
            try{
                Thread.sleep(Toast.LENGTH_SHORT);
                Registration.this.finish();
            } catch(Exception e){
                Log.e("REGISTRATIONERROR", e.toString());
            }
        }
    };
    EditText usernameEditText;
    EditText passwordEditText;
    EditText passwordRepeatEditText;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordRepeatEditText = findViewById(R.id.passwordRepeatEditText);
        db = new DBHelper(this);
    }

    public boolean onRegistrationClick(View view){
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordRepeat = passwordRepeatEditText.getText().toString();

        //Data verification
        if(username.length() < 5 ){
            Toast.makeText(Registration.this, "Username must contain at least 5 characters.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.length() < 8){
            Toast.makeText(Registration.this, "Password is too short.", Toast.LENGTH_SHORT).show();
            return false;
        }

        //User verification
        if(db.selectUser(username)==null){
            if(password.equals(passwordRepeat)){
                db.insertUser(username,password);
                //Intent intent = new Intent(Registration.this, Login.class);
                Toast.makeText(Registration.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                thread.start();
                return true;
            }
            else{
                passwordEditText.setText("");
                passwordRepeatEditText.setText("");
                Toast.makeText(Registration.this, "Passwords doesn't match.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else{
            usernameEditText.setText("");
            passwordEditText.setText("");
            passwordRepeatEditText.setText("");
            Toast.makeText(Registration.this, "User already exist.", Toast.LENGTH_SHORT).show();
            return false;
        }

    }
}