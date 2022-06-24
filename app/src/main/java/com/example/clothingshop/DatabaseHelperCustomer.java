package com.example.clothingshop;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.example.clothingshop.Model.Customers;

public class DatabaseHelperCustomer extends SQLiteOpenHelper {
    // THE DATABASE FOR THE WHOLE APP
    public static final String DATABASE_CUSTOMER = "Customer.db";
    private ProgressDialog loadingBar;
    // THE TABLE FOR USERS OF THE FASHIONISTA APP
    public static final String TABLE_CUSTOMERS = "customers";
    public static final String U_name = "name";
    public static final String U_phone = "phone";
    public static final String U_password = "password";
    public static final String U_address = "address";
    public static final String U_profilePhoto = "profilePhoto";

    public DatabaseHelperCustomer(@Nullable Context context) {
        super(context, DATABASE_CUSTOMER, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        // executes whatever string query we enter as string
        db.execSQL("create table "+ TABLE_CUSTOMERS +"('name' TEXT,'phone' TEXT PRIMARY KEY,'password' TEXT,'address' TEXT,'profilePhoto' BLOB)");


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CUSTOMERS);
        onCreate(db);
    }
    //*****************************************USER DATABASE METHODS**********************************************************************

    public boolean insertDataCustomers(String name,String phone,String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(U_name,name);
        contentValues.put(U_phone,phone);
        contentValues.put(U_password,password);
        contentValues.put(U_address,"null");
        contentValues.put(U_profilePhoto, "null");


        long result = db.insert(TABLE_CUSTOMERS,null ,contentValues);
        db.close();
        // data not inserted successfully
        if(result == -1) {
            System.out.println("Sorry.. Some error occurred, Try again later!!!");
            return false;
        }
        else {
            System.out.println("Congratulations! Your account has been created successfully!!!!");
            return true;
        }

    }

    public byte[] getImage(String phone) {
        byte[] data = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT profilePhoto FROM "+ TABLE_CUSTOMERS+" WHERE phone = ?", new String[]{phone});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            data = cursor.getBlob(0);
            break;  // Assumption: phone number is unique
        }
        cursor.close();
        return data;
        /*SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from Users", null);
        if(c.moveToNext()){
            byte[] image = c.getBlob(5);
            return image;
        }
        return null;*/
    }

    public String getUserPassword(String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select name from "+TABLE_CUSTOMERS+" where phone = ?",new String[]{phone});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("password"));
            return name;
        }
        cursor.close();
        return null;
    }
    public boolean changePassword(String phone,String newPassword){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(U_password,newPassword);
        long result =  db.update(TABLE_CUSTOMERS, contentValues, "phone = ?",new String[] { phone });
        db.close();
        if(result == -1) {
            System.out.println("Sorry.. Some error occurred, Try again later!!!");
            return false;
        }
        else {
            System.out.println("Congratulations! Your account has been created successfully!!!!");
            return true;
        }

    }
    public String getUserName(String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select name from "+TABLE_CUSTOMERS+" where phone = ?",new String[]{phone});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            return name;
        }
        cursor.close();
        return null;
    }

    public String getUserAddress(String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select address from "+TABLE_CUSTOMERS+" where phone = ?",new String[]{phone});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("address"));
            return address;
        }
        cursor.close();
        return null;
    }
    public Boolean UserPhoneExists(String phone){
        //System.out.println("\n\n\n\n\nPHONE = "+Phone+"\n\n\n\n\n");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select phone from "+TABLE_CUSTOMERS+" where phone = ? ",new String[]{phone});
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
    public Boolean UserPasswordCorrect(String Phone,String Password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select password from "+TABLE_CUSTOMERS+" where phone = ? and password = ?",new String[]{Phone,Password});
        if (res.getCount() > 0)
            return true;
        else
            return false;

    }
    public Cursor getAllDataUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_CUSTOMERS,null);
        return res;
    }
    public boolean updateDataUsers(String oldPhone,String phone,String name,String address) {
        // creating an SQLiteDatabase instance
        if (phone == null || oldPhone == null)
            return true;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(U_name,name);
        contentValues.put(U_phone,phone);
        contentValues.put(U_address,address);
        db.update(TABLE_CUSTOMERS, contentValues, "phone = ?",new String[] { oldPhone });
        db.close();
        return true;
    }
    public Integer deleteDataUsers(String phone) {
        // taking a database instance to delete the data
        SQLiteDatabase db = this.getWritableDatabase();
        // deleting data on basis of a key
        // new String[] will be the value of ?
        return db.delete(TABLE_CUSTOMERS, "PHONE = ?",new String[] {phone});
    }// returns the number of row deleted
    public void insertProfilePic(String phone,byte[] img) {
        SQLiteDatabase db = this.getWritableDatabase();
        /*String sql = "UPDATE Users SET profilePhoto = ? WHERE phone = ?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindBlob(1, img);
        statement.bindString(2,phone);
        statement.execute();
        db.close();*/
        ContentValues values = new ContentValues();
        values.put("profilePhoto",img);
        db.insert("Users", null,values);
        db.close();
    }

}
