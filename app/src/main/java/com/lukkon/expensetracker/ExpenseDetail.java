package com.lukkon.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lukkon.expensetracker.dataObjects.Expense;

public class ExpenseDetail extends AppCompatActivity {

    TextView titleTextView;
    TextView amountTextView;
    TextView categoryTextView;

    DBHelper db;
    Expense e;
    String color;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);

        e = (Expense)getIntent().getSerializableExtra("expense");
        color = getIntent().getStringExtra("color");
        db = new DBHelper(this);

        titleTextView = findViewById(R.id.titleTextView);
        amountTextView = findViewById(R.id.amountTextView);
        categoryTextView = findViewById(R.id.categoryTextView);

        titleTextView.setText(e.getTitle());
        titleTextView.setBackgroundColor(Color.parseColor(color));
        amountTextView.setText(String.valueOf(e.getAmount()));
        categoryTextView.setText(e.getCategory_name());
    }

    public void onEditButtonClick(View view){
        Toast.makeText(this, "edit", Toast.LENGTH_LONG).show();
    }

    public void onDeleteButtonClick(View view){
        new AlertDialog.Builder(this)
                .setTitle("Delete expense")
                .setMessage("Are you sure you want to delete this record?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onDeleteButtonClickConfirmCall();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void onDeleteButtonClickConfirmCall(){
        db.deleteExpense(this.e.getExpense_id());
        this.finish();
    }
}