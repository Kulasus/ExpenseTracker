package com.lukkon.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lukkon.expensetracker.dataObjects.Category;
import com.lukkon.expensetracker.dataObjects.Expense;
import com.lukkon.expensetracker.dataObjects.User;
import com.lukkon.expensetracker.security.Decoder;
import com.lukkon.expensetracker.security.Encoder;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

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
        insertInitData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS expenses");
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS categories");
        onCreate(db);
    }

    private void insertInitData(SQLiteDatabase db){
        db.execSQL("INSERT INTO users" + "(username, password)" + "VALUES('test','" + Encoder.encode("test") + "')");
        db.execSQL("INSERT INTO categories" + "(name, color)" + "VALUES('car','#ff00ff')");
        db.execSQL("INSERT INTO categories" + "(name, color)" + "VALUES('food','#0000ff')");
        db.execSQL("INSERT INTO categories" + "(name, color)" + "VALUES('culture','#ff0000')");
        db.execSQL("INSERT INTO categories" + "(name, color)" + "VALUES('tourism','#00ff00')");
        db.execSQL("INSERT INTO expenses" + "(expense_id, user_username, category_name, amount, title, description)" + "VALUES(1,'test', 'car', 500, 'Fuel', 'Car refueling at Shell gas station near my work')");
        db.execSQL("INSERT INTO expenses" + "(expense_id, user_username, category_name, amount, title, description)" + "VALUES(2,'test', 'car', 200, 'Fuel', 'Car refueling at MOL gas station near my home')");
        db.execSQL("INSERT INTO expenses" + "(expense_id, user_username, category_name, amount, title, description)" + "VALUES(3,'test', 'food', 1500, 'Shopping', 'Regular purchase of food and other stuff for home')");
        db.execSQL("INSERT INTO expenses" + "(expense_id, user_username, category_name, amount, title, description)" + "VALUES(4,'test', 'culture', 300, 'Cinema', 'Visiting cinema at Futurum Shopping Mal, I watched film Shrek 2')");
    }

    //Inserts
    public boolean insertUser(String username, String password){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", username);
            contentValues.put("password", Encoder.encode(password));
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
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("users", "user_username="+username, null);
            int deletedRows = db.delete("users", "username="+username,null);
            return deletedRows > 0;
        }
        catch(Exception e){
            Log.e("DBERROR", e.toString());
            return false;
        }
    }
    public boolean deleteCategory (String name)
    {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("category", "category_name="+name, null);
            int deletedRows = db.delete("categories", "name="+name,null);
            return deletedRows > 0;
        }
        catch(Exception e){
            Log.e("DBERROR", e.toString());
            return false;
        }
    }
    public boolean deleteExpense (int expense_id)
    {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            int deletedRows = db.delete("expenses", "expense_id="+expense_id,null);
            return deletedRows > 0;
        }
        catch(Exception e){
            Log.e("DBERROR", e.toString());
            return false;
        }
    }

    //Selects one
    public User selectUser(String username){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res =  db.rawQuery("select * from users where username='" + username + "'", null);
            if(res.moveToFirst()){
                String usernameOut = res.getString(res.getColumnIndex(USER_COLUMN_USERNAME));
                String password = Decoder.decode(res.getString(res.getColumnIndex(USER_COLUMN_PASSWORD)));
                return new User(usernameOut,password);
            }
            else{
                throw new SQLiteException("User not found.");
            }
        }
        catch(Exception e){
            Log.e("DBERROR", e.toString());
            return null;
        }
    }
    public Category selectCategory(String name){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res =  db.rawQuery("select * from categories where name='" + name + "''", null);
            if(res.moveToFirst()){
                String nameOut = res.getString(res.getColumnIndex(CATEGORY_COLUMN_NAME));
                String color = res.getString(res.getColumnIndex(CATEGORY_COLUMN_COLOR));
                return new Category(nameOut, color);
            }
            else{
                throw new SQLiteException("Category not found.");
            }
        }
        catch(Exception e){
            Log.e("DBERROR", e.toString());
            return null;
        }
    }
    public Expense selectExpense(int expense_id){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res =  db.rawQuery("select * from expenses where expense_id=" + expense_id + "", null);
            if(res.moveToFirst()){
                int expense_idOut = res.getInt(res.getColumnIndex(EXPENSE_COLUMN_EXPENSE_ID));
                String user_username = res.getString(res.getColumnIndex(EXPENSE_COLUMN_USER_USERNAME));
                String category_name = res.getString(res.getColumnIndex(EXPENSE_COLUMN_CATEGORY_NAME));
                int amount = res.getInt(res.getColumnIndex(EXPENSE_COLUMN_AMOUNT));
                String title = res.getString(res.getColumnIndex(EXPENSE_COLUMN_TITLE));
                String description = res.getString(res.getColumnIndex(EXPENSE_COLUMN_DESCRIPTION));
                return new Expense(expense_idOut, user_username, category_name, amount, title, description);
            }
            else{
                throw new SQLiteException("Expense not found.");
            }
        }
        catch(Exception e){
            Log.e("DBERROR", e.toString());
            return null;
        }
    }

    //Updates
    public boolean updateUser (String username, String password)
    {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("username",username);
            contentValues.put("password",password);
            int updatedRows = db.update("users",contentValues,"username="+username,null);
            return updatedRows > 0;
        }
        catch(Exception e){
            Log.e("DBERROR", e.toString());
            return false;
        }

    }
    public boolean updateCategory (String name, String color)
    {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name",name);
            contentValues.put("color",color);
            int updatedRows = db.update("categories",contentValues,"name="+name,null);
            return updatedRows > 0;
        }
        catch(Exception e){
            Log.e("DBERROR", e.toString());
            return false;
        }
    }
    public boolean updateExpense (int expense_id, String user_username, String category_name, int amount, String title, String description)
    {
        try{
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
        catch(Exception e){
            Log.e("DBERROR", e.toString());
            return false;
        }
    }

    //Selects *
    public ArrayList<User> selectAllUsers(){
        ArrayList<User> arrayList = new ArrayList<User>();
        try{
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
        catch(Exception e){
            Log.e("DBERROR", e.toString());
            return null;
        }
    }
    public ArrayList<Category> selectAllCategories(){
        ArrayList<Category> arrayList = new ArrayList<Category>();
        try{
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
        catch(Exception e){
            Log.e("DBERROR", e.toString());
            return null;
        }
    }
    public ArrayList<Expense> selectAllExpenses(){
        ArrayList<Expense> arrayList = new ArrayList<Expense>();
        try{
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
        catch(Exception e){
            Log.e("DBERROR", e.toString());
            return null;
        }
    }
    public ArrayList<Expense> selectAllExpensesByUsername(String user_username){
        ArrayList<Expense> arrayList = new ArrayList<Expense>();
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res =  db.rawQuery( "select * from expenses where user_username LIKE '"+user_username+"'", null );
            res.moveToFirst();

            while(res.isAfterLast()==false){
                int expense_id = res.getInt(res.getColumnIndex(EXPENSE_COLUMN_EXPENSE_ID));
                String user_usernameOut = res.getString(res.getColumnIndex(EXPENSE_COLUMN_USER_USERNAME));
                String category_name = res.getString(res.getColumnIndex(EXPENSE_COLUMN_CATEGORY_NAME));
                int amount = res.getInt(res.getColumnIndex(EXPENSE_COLUMN_AMOUNT));
                String title = res.getString(res.getColumnIndex(EXPENSE_COLUMN_TITLE));
                String description = res.getString(res.getColumnIndex(EXPENSE_COLUMN_DESCRIPTION));
                arrayList.add(new Expense(expense_id, user_usernameOut, category_name, amount, title, description));
                res.moveToNext();
            }
            return arrayList;
        }
        catch(Exception e){
            Log.e("DBERROR", e.toString());
            return null;
        }
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
