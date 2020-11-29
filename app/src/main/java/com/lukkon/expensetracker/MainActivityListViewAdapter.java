package com.lukkon.expensetracker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lukkon.expensetracker.dataObjects.Expense;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivityListViewAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<Expense> expenses;

    public MainActivityListViewAdapter(Activity context, ArrayList<Expense> expenses, ArrayList<String> titles){
        super(context, R.layout.custom_listview, titles);
        this.context = context;
        this.expenses = expenses;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_listview, null, true);

        TextView titleText = (TextView)rowView.findViewById(R.id.title);
        TextView amountText = (TextView)rowView.findViewById(R.id.subtitle);

        titleText.setText(expenses.get(position).getTitle());
        amountText.setText(String.valueOf(expenses.get(position).getAmount()));

        return rowView;
    }

}
