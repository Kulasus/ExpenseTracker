package com.lukkon.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class AddExpense extends AppCompatActivity {

    ConstraintLayout addExpenseLayout;
    Button confirmButton;
    TextView mainTitle;
    TextView labelTitle;
    TextView labelAmount;
    TextView labelCategory;
    EditText title;
    EditText amount;
    EditText description;
    RadioGroup radios;
    RadioButton selectedRadio;
    RadioButton radio1;
    RadioButton radio2;
    RadioButton radio3;
    RadioButton radio4;
    DBHelper db;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        addExpenseLayout = findViewById(R.id.addExpenseLayout);
        confirmButton = findViewById(R.id.confirmButton);
        mainTitle = findViewById(R.id.mainLabelTextViewAdding);
        labelTitle = findViewById(R.id.labelTitleTextViewAdding);
        labelAmount = findViewById(R.id.labelAmountTextViewAdding);
        labelCategory = findViewById(R.id.labelCategoryListViewAdding);
        radio1 = findViewById(R.id.carRadioButton);
        radio2 = findViewById(R.id.foodRadioButton);
        radio3 = findViewById(R.id.cultureRadioButton);
        radio4 = findViewById(R.id.toursimRadioButton);
        title = findViewById(R.id.titleEditTextAdding);
        amount = findViewById(R.id.amountEditTextAdding);
        description = findViewById(R.id.descriptionMultilineTextAdding);
        radios = findViewById(R.id.radioGroup);

        db = new DBHelper(this);
        prefs = this.getSharedPreferences("com.lukkon.expensetracker", Context.MODE_PRIVATE);

        if(prefs.getBoolean("lightTheme", true)){
            mainTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            title.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            title.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            labelTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            amount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            amount.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            labelAmount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            labelCategory.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            description.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            description.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            confirmButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            radio1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            radio2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            radio3.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            radio4.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            addExpenseLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
        }
        else{
            mainTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            title.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            title.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            labelTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            amount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            amount.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            labelAmount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            labelCategory.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            description.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            description.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            confirmButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            radio1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            radio2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            radio3.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            radio4.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            addExpenseLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
        }

    }
    public void onConfirmButtonClick(View view){
        selectedRadio = findViewById(radios.getCheckedRadioButtonId());
        String userUsernameString = prefs.getString("loggedUserUsername", "ERR");
        String categoryNameString = selectedRadio.getText().toString();
        String titleString = title.getText().toString();
        String descriptionString = description.getText().toString();
        int amountInt = Integer.parseInt(amount.getText().toString());
        db.insertExpense(userUsernameString,categoryNameString,amountInt,titleString,descriptionString);
        this.finish();
    }
}