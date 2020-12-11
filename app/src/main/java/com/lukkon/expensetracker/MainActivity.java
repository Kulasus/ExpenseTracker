package com.lukkon.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.lukkon.expensetracker.dataObjects.Expense;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;
    ListView latestExpenseListView;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBHelper(this);
        prefs = this.getSharedPreferences("com.lukkon.expensetracker", Context.MODE_PRIVATE);
        latestExpenseListView = findViewById(R.id.latestExpensesListView);
        loadExpenseListView();
    }

    private void loadExpenseListView(){
        final ArrayList<Expense> latestExpenses = db.selectAllExpensesByUsername(prefs.getString("loggedUserUsername",null));
        ArrayList<String> titles = new ArrayList<String>();
        for(Expense e : latestExpenses){
            titles.add(e.getTitle());
        }
        MainActivityListViewAdapter arrayAdapter = new MainActivityListViewAdapter(this, latestExpenses, titles, db);
        latestExpenseListView.setAdapter(arrayAdapter);
        latestExpenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Expense expense = latestExpenses.get(position);
                String colorString = db.selectCategory(expense.getCategory_name()).getColor();
                Intent intent = new Intent(MainActivity.this, ExpenseDetail.class);
                intent.putExtra("expense",expense);
                intent.putExtra("color", colorString);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startActivity(getIntent());
    }
}