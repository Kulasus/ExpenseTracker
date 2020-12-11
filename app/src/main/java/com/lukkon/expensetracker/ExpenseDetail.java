package com.lukkon.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lukkon.expensetracker.dataObjects.Expense;

public class ExpenseDetail extends AppCompatActivity {

    TextView titleTextView;
    TextView amountTextView;
    TextView categoryTextView;

    Expense e;
    //DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);

        e = (Expense)getIntent().getSerializableExtra("expense");

        titleTextView = findViewById(R.id.titleTextView);
        amountTextView = findViewById(R.id.amountTextView);
        categoryTextView = findViewById(R.id.categoryTextView);

        titleTextView.setText(e.getTitle());
        amountTextView.setText(String.valueOf(e.getAmount()));
        categoryTextView.setText(e.getCategory_name());
    }

    public void onEditButtonClick(View view){
        Toast.makeText(this, "edit", Toast.LENGTH_LONG).show();
    }

    public void onDeleteButtonClick(View view){
        Toast.makeText(this, "delete", Toast.LENGTH_LONG).show();
    }
}