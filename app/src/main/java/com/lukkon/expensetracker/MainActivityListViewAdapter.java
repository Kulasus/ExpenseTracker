package com.lukkon.expensetracker;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.lukkon.expensetracker.dataObjects.Category;
import com.lukkon.expensetracker.dataObjects.Expense;

import java.util.ArrayList;

public class MainActivityListViewAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<Expense> expenses;
    private final DBHelper db;
    private boolean lightTheme;


    public MainActivityListViewAdapter(Activity context, ArrayList<Expense> expenses, ArrayList<String> titles, DBHelper db, boolean lightTheme){
        super(context, R.layout.custom_listview, titles);
        this.context = context;
        this.expenses = expenses;
        this.db = db;
        this.lightTheme = lightTheme;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_listview, null, true);
        setRowColor(rowView, position);

        TextView titleText = (TextView)rowView.findViewById(R.id.titleCustomListView);
        TextView amountText = (TextView)rowView.findViewById(R.id.subtitleCustomListView);

        if(lightTheme){
            titleText.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            amountText.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }
        else{
            titleText.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            amountText.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        titleText.setText(expenses.get(position).getTitle());
        amountText.setText(String.valueOf(expenses.get(position).getAmount()));

        return rowView;
    }

    private void setRowColor(View rowView, int position){
        String rowColor = this.getRowColor(rowView, position);
        rowView.setBackgroundColor(Color.parseColor(rowColor));
    }

    private String getRowColor(View rowView, int position){
        String categoryName = this.expenses.get(position).getCategory_name();
        Category category = db.selectCategory(categoryName);
        return category.getColor();
    }

}
