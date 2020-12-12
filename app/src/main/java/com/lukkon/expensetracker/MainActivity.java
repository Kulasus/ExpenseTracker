package com.lukkon.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lukkon.expensetracker.dataObjects.Expense;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;
    ListView latestExpenseListView;
    TextView latestExpensesTextView;
    ConstraintLayout mainViewLayout;
    DBHelper db;
    SoundPlayer soundPlayer;

    SensorManager sensorManager;
    float acelVal, acelLast, shake;
    static int shakeCounter = 0;
    static boolean lightTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBHelper(this);
        prefs = this.getSharedPreferences("com.lukkon.expensetracker", Context.MODE_PRIVATE);
        soundPlayer = new SoundPlayer(this);

        latestExpensesTextView = findViewById(R.id.latestExpensesTextView);
        mainViewLayout = findViewById(R.id.mainViewLayout);

        latestExpenseListView = findViewById(R.id.latestExpensesListView);
        loadExpenseListView();

        lightTheme = prefs.getBoolean("lightTheme", true);
        changeTheme();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 1.00f;

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    private void loadExpenseListView(){
        final ArrayList<Expense> latestExpenses = db.selectAllExpensesByUsername(prefs.getString("loggedUserUsername",null));
        ArrayList<String> titles = new ArrayList<String>();
        for(Expense e : latestExpenses){
            titles.add(e.getTitle());
        }
        MainActivityListViewAdapter arrayAdapter = new MainActivityListViewAdapter(this, latestExpenses, titles, db, lightTheme);
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
        this.finish();
        startActivity(getIntent());
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.addMenuItem){
            Intent intent = new Intent(MainActivity.this, AddExpense.class);
            startActivity(intent);
        }
        if(id == R.id.aboutMenuItem){
            Intent intent = new Intent(MainActivity.this, About.class);
            startActivity(intent);
        }
        if(id == R.id.settingsMenuItem){
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
        }
        if(id == R.id.logoutMenuItem){

            soundPlayer.playLogoutSound();
            prefs.edit().remove("loggedUserUsername").remove("loggedUserPassword").remove("sound").apply();
            prefs.edit().remove("loggedUserUsername").remove("loggedUserPassword").apply();
            prefs.edit().remove("lightTheme").apply();
          
            Intent intent = new Intent(MainActivity.this, Login.class);
            finish();
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            acelLast = acelVal;
            acelVal = (float) Math.sqrt((double) (x*x)+(y*y)+(z*z));

            float delta = acelVal-acelLast;
            shake = shake*0.9f+delta;

            if(shake>30){
                shakeCounter++;
            }

            if(shakeCounter >= 3){
                shakeCounter = 0;
                lightTheme = !lightTheme;
                changeTheme();
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    public void changeTheme(){
        prefs.edit().remove("lightTheme").putBoolean("lightTheme",lightTheme).apply();


        if(lightTheme){
            latestExpensesTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            mainViewLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhite));
            loadExpenseListView();
        }
        else{
            latestExpensesTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            mainViewLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorDark));
            loadExpenseListView();
        }
    }
}