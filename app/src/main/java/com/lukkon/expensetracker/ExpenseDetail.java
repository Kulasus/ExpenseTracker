package com.lukkon.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lukkon.expensetracker.dataObjects.Expense;

import org.w3c.dom.Text;

import java.security.InvalidParameterException;

import static com.lukkon.expensetracker.R.layout.custom_dialog;

public class ExpenseDetail extends AppCompatActivity {

    TextView titleTextView;
    TextView amountTextView;
    TextView categoryTextView;
    TextView descriptionTextView;
    Button editButton;
    Button deleteButton;
    TextView labelCategoryTextView;
    TextView labelAmountTextView;
    ConstraintLayout expenseDetailLayout;

    SoundPlayer soundPlayer;
    DBHelper db;
    SharedPreferences prefs;
    Expense e;
    String color;
    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);

        prefs = getSharedPreferences("com.lukkon.expensetracker", Context.MODE_PRIVATE);
        e = (Expense)getIntent().getSerializableExtra("expense");
        color = getIntent().getStringExtra("color");
        db = new DBHelper(this);
        prefs = getSharedPreferences("com.lukkon.expensetracker", Context.MODE_PRIVATE);
        soundPlayer = new SoundPlayer(this);

        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        titleTextView = findViewById(R.id.titleTextView);
        amountTextView = findViewById(R.id.amountTextView);
        categoryTextView = findViewById(R.id.categoryTextView);
        descriptionTextView = findViewById(R.id.descriptionMultilineText);
        labelAmountTextView = findViewById(R.id.labelAmountTextView);
        labelCategoryTextView = findViewById(R.id.labelCategoryTextView);
        expenseDetailLayout = findViewById(R.id.expenseDetailLayout);

        titleTextView.setText(e.getTitle());
        titleTextView.setBackgroundColor(Color.parseColor(color));
        amountTextView.setText(String.valueOf(e.getAmount()));
        categoryTextView.setText(e.getCategory_name());
        descriptionTextView.setText(e.getDescription());

        if(prefs.getBoolean("lightTheme",true)){
            titleTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            amountTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            categoryTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            descriptionTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            editButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            deleteButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            labelAmountTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            labelCategoryTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            expenseDetailLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
        }
        else{
            titleTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            amountTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            categoryTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            descriptionTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            editButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            deleteButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            labelAmountTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            labelCategoryTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            expenseDetailLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
        }
    }

    public void onEditButtonClick(View view){
        soundPlayer.playButtonSound();

        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(custom_dialog,null);

        final EditText titleEditText = (EditText) dialogView.findViewById(R.id.titleEditText);
        titleEditText.setText(e.getTitle());
        final EditText amountEditText = (EditText) dialogView.findViewById(R.id.amountEditText);
        amountEditText.setText(String.valueOf(e.getAmount()));
        final EditText descriptionEditText = (EditText) dialogView.findViewById(R.id.descriptionEditText);
        descriptionEditText.setText(e.getDescription());

        Button submitButton = (Button) dialogView.findViewById(R.id.submitButton);
        Button cancelButton = (Button) dialogView.findViewById(R.id.cancelButton);

        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int amount = Integer.parseInt(String.valueOf(amountEditText.getText()));
                String title = String.valueOf(titleEditText.getText());
                String description = String.valueOf(descriptionEditText.getText());
                try{
                    onEditButtonClickConfirmCall(amount,title,description);
                    dialogBuilder.dismiss();
                }
                catch (InvalidParameterException e){
                    Log.e("ERR_CUST_DIALOG", e.toString());
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    public void onDeleteButtonClick(View view){
        soundPlayer.playButtonSound();

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

    private void onEditButtonClickConfirmCall(int amount, String title, String description) throws InvalidParameterException {
        if(amount <= 0){
            Toast.makeText(this,"Amount must be bigger than zero.",Toast.LENGTH_LONG).show();
            throw new InvalidParameterException();
        }
        else if(title.length() == 0) {
            Toast.makeText(this, "Title cannot be empty.",Toast.LENGTH_LONG).show();
            throw new InvalidParameterException();
        }
        else{
            db.updateExpense(e.getExpense_id(),e.getUser_username(),e.getCategory_name(),amount, title, description);
            this.titleTextView.setText(title);
            this.amountTextView.setText(String.valueOf(amount));
            this.descriptionTextView.setText(description);
        }
    }
}