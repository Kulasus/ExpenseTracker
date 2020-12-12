package com.lukkon.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lukkon.expensetracker.dataObjects.User;

public class Login extends AppCompatActivity {

    DBHelper db;
    EditText usernameEditText;
    EditText passwordEditText;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        db = new DBHelper(this);
        prefs = this.getSharedPreferences("com.lukkon.expensetracker", Context.MODE_PRIVATE);
    }

    public void onLoginClick(View view){
        User loggingUser = db.selectUser(usernameEditText.getText().toString());
        if(loggingUser != null){
            if(loggingUser.getPassword().equals(passwordEditText.getText().toString())){
                prefs.edit().putString("loggedUserUsername",loggingUser.getUsername()).apply();
                prefs.edit().putString("loggedUserPassword",loggingUser.getPassword()).apply();
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
            else{
                passwordEditText.setText("");
                Toast.makeText(this, "Invalid password!" , Toast.LENGTH_LONG).show();
            }
        }
        else{
            usernameEditText.setText("");
            passwordEditText.setText("");
            Toast.makeText(this, "User not found!", Toast.LENGTH_LONG).show();
        }
    }
    public void onRegistrationClick(View view){
        usernameEditText.setText("");
        passwordEditText.setText("");
        Intent intent = new Intent(Login.this, Registration.class);
        startActivity(intent);
    }
}