package com.lukkon.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lukkon.expensetracker.dataObjects.Category;
import com.lukkon.expensetracker.dataObjects.Expense;
import com.lukkon.expensetracker.dataObjects.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class DBHelper extends SQLiteOpenHelper{

    //Database name
    public static final String DATABASE_NAME = "DBEXPENSETRACKER.db";

    //User table columns
    public static final String USER_COLUMN_USERNAME = "username";
    public static final String USER_COLUMN_PASSWORD = "password";

    //Expense table columns
    public static final String EXPENSE_COLUMN_EXPENSE_ID = "expense_id";
    public static final String EXPENSE_COLUMN_USER_USERNAME = "user_username";
    public static final String EXPENSE_COLUMN_CATEGORY_NAME = "category_name";
    public static final String EXPENSE_COLUMN_AMOUNT = "amount";
    public static final String EXPENSE_COLUMN_TITLE = "title";
    public static final String EXPENSE_COLUMN_DESCRIPTION = "description";

    //Category table columns
    public static final String CATEGORY_COLUMN_NAME = "name";
    public static final String CATEGORY_COLUMN_COLOR = "color";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Users table
        db.execSQL("CREATE TABLE users" + "(username TEXT PRIMARY KEY, password TEXT)");
        //Categories table
        db.execSQL("CREATE TABLE categories" + "(name TEXT PRIMARY KEY, color TEXT)");
        //Expenses table
        db.execSQL("CREATE TABLE expenses" + "(expense_id INTEGER PRIMARY KEY, user_username TEXT, category_name TEXT," +
                " amount INTEGER, title TEXT, description TEXT, FOREIGN KEY(user_username) references users(username), FOREIGN KEY(category_name) REFERENCES categories(name))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS expenses");
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS categories");
        onCreate(db);
    }

    //Inserts
    public boolean insertUser(String username, String password){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", username);
            contentValues.put("password",password);
            long insertedId = db.insert("users", null, contentValues);
            if (insertedId == -1) return false;
            return true;
        }
        catch(Exception e){
            Log.e("DBERROR", e.toString());
            return false;
        }
    }
    public boolean insertCategory(String name, String color){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("color", color);
            long insertedId = db.insert("categories", null, contentValues);
            if (insertedId == -1) return false;
            return true;
        }
        catch(Exception e){
            Log.e("DBERROR", e.toString());
            return false;
        }
    }
    public boolean insertExpense(String user_username, String category_name, int amount, String title, String description){
        Random rand = new Random();
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            //Random id generation
            int expense_id = rand.nextInt();
            while(selectExpense(expense_id)!=null){
                expense_id = rand.nextInt();
            }

            contentValues.put("expense_id", rand.nextInt());
            contentValues.put("user_username", user_username);
            contentValues.put("category_name", category_name);
            contentValues.put("amount", amount);
            contentValues.put("title", title);
            contentValues.put("description", description);
            long insertedId = db.insert("users", null, contentValues);
            if (insertedId == -1) return false;
            return true;
        }
        catch(Exception e){
            Log.e("DBERROR", e.toString());
            return false;
        }
    }

    //Deletes one
    public boolean deleteUser (String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("expenses", "user_username="+username, null);
        int deletedRows = db.delete("users", "username="+username,null);
        return deletedRows > 0;
    }
    public boolean deleteCategory (String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("expenses", "category_name="+name, null);
        int deletedRows = db.delete("categories", "name="+name,null);
        return deletedRows > 0;
    }
    public boolean deleteExpense (int expense_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete("expenses", "expense_id="+expense_id,null);
        return deletedRows > 0;
    }

    //Selects one
    public User selectUser(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from users where username=" + username + "", null);
        String usernameOut = res.getString(res.getColumnIndex(USER_COLUMN_USERNAME));
        String password = res.getString(res.getColumnIndex(USER_COLUMN_PASSWORD));
        return new User(usernameOut,password);
    }
    public Category selectCategory(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from categories where name=" + name + "", null);
        String nameOut = res.getString(res.getColumnIndex(CATEGORY_COLUMN_NAME));
        String color = res.getString(res.getColumnIndex(CATEGORY_COLUMN_COLOR));
        return new Category(nameOut, color);
    }
    public Expense selectExpense(int expense_id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from expenses where expense_id=" + expense_id + "", null);
        int expense_idOut = res.getInt(res.getColumnIndex(EXPENSE_COLUMN_EXPENSE_ID));
        String user_username = res.getString(res.getColumnIndex(EXPENSE_COLUMN_USER_USERNAME));
        String category_name = res.getString(res.getColumnIndex(EXPENSE_COLUMN_CATEGORY_NAME));
        int amount = res.getInt(res.getColumnIndex(EXPENSE_COLUMN_AMOUNT));
        String title = res.getString(res.getColumnIndex(EXPENSE_COLUMN_TITLE));
        String description = res.getString(res.getColumnIndex(EXPENSE_COLUMN_DESCRIPTION));
        return new Expense(expense_idOut, user_username, category_name, amount, title, description);
    }

    //Updates
    public boolean updateUser (String username, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("password",password);
        int updatedRows = db.update("users",contentValues,"username="+username,null);
        return updatedRows > 0;
    }
    public boolean updateCategory (String name, String color)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("color",color);
        int updatedRows = db.update("categories",contentValues,"name="+name,null);
        return updatedRows > 0;
    }
    public boolean updateExpense (int expense_id, String user_username, String category_name, int amount, String title, String description)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("expense_id",expense_id);
        contentValues.put("user_username",user_username);
        contentValues.put("category_name", category_name);
        contentValues.put("amount", amount);
        contentValues.put("title", title);
        contentValues.put("description",description);
        int updatedRows = db.update("expenses",contentValues,"expense_id="+expense_id,null);
        return updatedRows > 0;
    }

    //Selects *
    public ArrayList<User> selectAllUsers(){
        ArrayList<User> arrayList = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from users", null );
        res.moveToFirst();

        while(res.isAfterLast()==false){
            String username = res.getString(res.getColumnIndex(USER_COLUMN_USERNAME));
            String password = res.getString(res.getColumnIndex(USER_COLUMN_PASSWORD));
            arrayList.add(new User(username,password));
            res.moveToNext();
        }
        return arrayList;
    }
    public ArrayList<Category> selectAllCategories(){
        ArrayList<Category> arrayList = new ArrayList<Category>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from categories", null );
        res.moveToFirst();

        while(res.isAfterLast()==false){
            String name = res.getString(res.getColumnIndex(CATEGORY_COLUMN_NAME));
            String color = res.getString(res.getColumnIndex(CATEGORY_COLUMN_COLOR));
            arrayList.add(new Category(name, color));
            res.moveToNext();
        }
        return arrayList;
    }
    public ArrayList<Expense> selectAllExpenses(){
        ArrayList<Expense> arrayList = new ArrayList<Expense>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from expenses", null );
        res.moveToFirst();

        while(res.isAfterLast()==false){
            int expense_id = res.getInt(res.getColumnIndex(EXPENSE_COLUMN_EXPENSE_ID));
            String user_username = res.getString(res.getColumnIndex(EXPENSE_COLUMN_USER_USERNAME));
            String category_name = res.getString(res.getColumnIndex(EXPENSE_COLUMN_CATEGORY_NAME));
            int amount = res.getInt(res.getColumnIndex(EXPENSE_COLUMN_AMOUNT));
            String title = res.getString(res.getColumnIndex(EXPENSE_COLUMN_TITLE));
            String description = res.getString(res.getColumnIndex(EXPENSE_COLUMN_DESCRIPTION));
            arrayList.add(new Expense(expense_id, user_username, category_name, amount, title, description));
            res.moveToNext();
        }
        return arrayList;
    }

    //Deletes *
    public int removeAllUsers()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("expenses", null, null);
        int deletedRows = db.delete("users", null,null);
        return deletedRows;
    }
    public int removeAllCategories()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("expenses", null, null);
        int deletedRows = db.delete("categories", null,null);
        return deletedRows;
    }
    public int removeAllExpenses()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete("expenses", null,null);
        return deletedRows;
    }
}
