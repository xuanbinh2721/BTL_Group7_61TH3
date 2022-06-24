package com.example.clothingshop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.jar.Attributes;

//public class SignupForm extends AppCompatActivity {
//    Button btnBackLogin, btnCreateAccount;
//    EditText edtCreateName, edtCreatePhone, edtCreatePw;
//    DatabaseHelperCustomer myDb;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signup_form);
//        btnBackLogin = (Button) findViewById(R.id.btnBackLogin);
//        btnBackLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                backtoLogin();
//            }
//        });
//        btnCreateAccount = (Button)findViewById(R.id.btnCreateAcc);
//        edtCreateName = (EditText)findViewById(R.id.edtCreateName);
//        edtCreatePhone = (EditText)findViewById(R.id.edtCreatePhone);
//        edtCreatePw = (EditText)findViewById(R.id.edtCreatePw);
//        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CreateAccount();
//            }
//        });
//    }
//
//    private void CreateAccount() {
//        String Name = edtCreateName.getText().toString();
//        String phone = edtCreatePhone.getText().toString();
//        //Toast.makeText(SignupForm.this,"Phone0 = "+phone,Toast.LENGTH_SHORT).show();
//        String password = edtCreatePw.getText().toString();
//
//        if (TextUtils.isEmpty(Name)){
//            Toast.makeText(this,"Please enter your Name...",Toast.LENGTH_SHORT).show();
//        }
//        else if (TextUtils.isEmpty(phone)){
//            Toast.makeText(this,"Please enter your valid Phone...",Toast.LENGTH_SHORT).show();
//        }
//        else if (TextUtils.isEmpty(password)){
//            Toast.makeText(this,"Please enter your valid Password...",Toast.LENGTH_SHORT).show();
//        }
//        else{
//            ValidatePhone(Name,phone,password);
//        }
//    }
//
//    private void ValidatePhone(String name, String phone, String password) {
//        if (!myDb.UserPhoneExists(phone)){
//            if (myDb.insertDataCustomers(name,phone,password)){
//                Toast.makeText(SignupForm.this,"Congratulations! Your account has been created!",Toast.LENGTH_SHORT).show();
//                // Toast.makeText(SignupForm.this,"Welcome "+fName+" !!",Toast.LENGTH_SHORT).show();
//                //loadingBar.dismiss();
//                try{
//                    Intent intent = new Intent(SignupForm.this, CustomerLogin.class);
//                    startActivity(intent);
//                }catch(Exception e ){
//                    e.printStackTrace();
//                }
//            }
//            else{
//                Toast.makeText(SignupForm.this,"Sorry! Some error occurred.. Try again later!",Toast.LENGTH_SHORT).show();
//
//            }
//
//        }
//        else{
//            Toast.makeText(SignupForm.this,"Entered Phone already exits!!",Toast.LENGTH_SHORT).show();
//
//            Toast.makeText(SignupForm.this,"Please try again with another phone!",Toast.LENGTH_SHORT).show();
//
//        }
//
//    }
//    public void backtoLogin() {
//        Intent i = new Intent(this, CustomerLogin.class);
//        startActivity(i);
//    }
//}
public class SignupForm extends AppCompatActivity {
    // Variables for input fields
    private Button createAccountBtn;
    private EditText edtCreateName, edtCreatePhone,edtCreatePassword;
    DatabaseHelperCustomer myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_form);
        /*
        //------------------------FOR BACKGROUND ANIMATION---------------------------------------
        // init constraintLayout
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        // initializing animation drawable by getting background from constraint layout
        animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        // setting enter fade animation duration to 5 seconds
        animationDrawable.setEnterFadeDuration(4000);
        // setting exit fade animation duration to 2 seconds
        animationDrawable.setExitFadeDuration(2000);
        //----------------------------------------------------------------------------------------
        */

        // for input fields editTexts
        createAccountBtn = (Button)findViewById(R.id.btnCreateAcc);
        edtCreateName = (EditText)findViewById(R.id.edtCreateName);
        edtCreatePhone = (EditText)findViewById(R.id.edtCreatePhone);
        edtCreatePassword = (EditText)findViewById(R.id.edtCreatePw);

        // calling constructor of DataBaseHelper class to create the database
        myDb = new DatabaseHelperCustomer(this);

        // Creating user Account
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });



    }
    private void CreateAccount(){
        String Name = edtCreateName.getText().toString();
        String phone = edtCreatePhone.getText().toString();
        //Toast.makeText(SignupForm.this,"Phone0 = "+phone,Toast.LENGTH_SHORT).show();
        String password = edtCreatePassword.getText().toString();

        if (TextUtils.isEmpty(Name)){
            Toast.makeText(this,"Please enter your name...",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Please enter your valid Phone...",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter your valid Password...",Toast.LENGTH_SHORT).show();
        }
        else{
            //Toast.makeText(SignupForm.this,"Phone1 = "+phone,Toast.LENGTH_SHORT).show();
            ValidatePhone(Name,phone,password);
        }
    }
    private void ValidatePhone(String Name,String phone,String password) {

        if ((myDb.UserPhoneExists(phone))==false){
            if (myDb.insertDataCustomers(Name,phone,password)){
                Toast.makeText(SignupForm.this,"Congratulations! Your account has been created!",Toast.LENGTH_SHORT).show();

                // Toast.makeText(SignupForm.this,"Welcome "+fName+" !!",Toast.LENGTH_SHORT).show();
                //loadingBar.dismiss();
                try{
                    Intent intent = new Intent(SignupForm.this, CustomerLogin.class);
                    startActivity(intent);
                }catch(Exception e ){
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(SignupForm.this,"Sorry! Some error occurred.. Try again later!",Toast.LENGTH_SHORT).show();

            }

        }
        else{
            Toast.makeText(SignupForm.this,"Entered Phone already exits!!",Toast.LENGTH_SHORT).show();

            Toast.makeText(SignupForm.this,"Please try again with another phone!",Toast.LENGTH_SHORT).show();

        }

    }
    public void openHomepage(){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
}



