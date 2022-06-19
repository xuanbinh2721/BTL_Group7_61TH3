package com.example.clothingshop;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelperOwner extends SQLiteOpenHelper {
    public static final String DATABASE_ADMINS = "Owners.db";
    private ProgressDialog loadingBar;

    // THE TABLE FOR ADMINS OF THE FASHIONISTA APP
    public static final String TABLE_ADMINS = "Owner";
    public static final String O_name = "name";
    public static final String O_phone = "phone";
    public static final String O_password = "password";

    public DatabaseHelperOwner(@Nullable Context context) {
        super(context, DATABASE_ADMINS, null, 1);
    }

    public DatabaseHelperOwner(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table "+ TABLE_ADMINS +"('name' TEXT,'phone' TEXT PRIMARY KEY,'password' TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ADMINS);
        onCreate(db);
    }

    //*******OWNER DATABASE METHODS********
    public boolean insertDataOwner(String name,String phone,String password){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(O_name,name);
        contentValues.put(O_phone,phone);
        contentValues.put(O_password,password);
        long result = db.insert(TABLE_ADMINS,null ,contentValues);
        // data not inserted successfully
        if(result == -1) {
            System.out.println("Sorry...Some error occured, Try again later!!!");
            return false;
        }
        else {
            System.out.println("Congratulations! You are now an Admin!!!");
            return true;
        }
    }
    public String  getOwnerPassword(String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select name from "+TABLE_ADMINS+" where phone = ?",new String[]{phone});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("password"));
            return name;
        }
        cursor.close();
        return null;
    }
    public String getOwnerName(String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select name from "+TABLE_ADMINS+" where phone = ?",new String[]{phone});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            return name;
        }
        cursor.close();
        return null;
    }
    public Boolean OwnerPhoneExists(String phone){
//        System.out.println("\n\n\n\n\nPHONE = "+phone+"\n\n\n\n\n");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select phone from "+TABLE_ADMINS+" where phone = ? ",new String[]{phone});
        if (res.getCount() > 0) {
            // phone number exists
            res.close();
            return true;
        }
        else {
            res.close();
            // phone number does not exist
            return false;
        }

    }
    public Boolean OwnerPasswordCorrect(String Phone,String Password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select password from "+TABLE_ADMINS+" where phone = ? and password = ?",new String[]{Phone,Password});
        if (res.getCount() > 0)
            return true;
        else
            return false;
    }
    public boolean updateDataOwners(String name, String phone,String password) {
        // creating an SqliteDatabase instance
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(O_name,name);
        contentValues.put(O_phone,phone);
        contentValues.put(O_password,password);
        // updation on basis of unique phone no (Primary key)
        db.update(TABLE_ADMINS, contentValues, "PHONE = ?",new String[] { phone });
        return true;
    }
    public Integer deleteDataOwners(String phone) {
        // taking a database instance to delete the data
        SQLiteDatabase db = this.getWritableDatabase();
        // deleting data on basis of a key
        // new String[] will be the value of ?
        return db.delete(TABLE_ADMINS, "PHONE = ?",new String[] {phone});
    }// returns the number of row deleted
    //*************************************************************************************************************************************
}

