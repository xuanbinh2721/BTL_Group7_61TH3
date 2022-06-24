package com.example.clothingshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.clothingshop.Model.Customers;
import com.example.clothingshop.Prevalent.Prevalent;

import java.util.jar.Attributes;

public class CustomerLogin extends AppCompatActivity {
    Button btnLogin, btntoSignUp;
    EditText edtPhone, edtPasswd;
    CheckBox cbRememberPasswd;
    DatabaseHelperCustomer MyDb;
    // DatabaseHelperCustomer dbCustomer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        MyDb = new DatabaseHelperCustomer(this);
        edtPhone = (EditText)findViewById(R.id.edtPhone);
        edtPasswd = (EditText)findViewById(R.id.edtPasswd);
        btnLogin = (Button)findViewById(R.id.CustomLogin);
        btntoSignUp =  (Button)findViewById(R.id.btntoSignUp);
        btntoSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openSignupForm();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                CustomerLogin();
            }
        });


    }

    private void openSignupForm() {
        Intent intent = new Intent(this, SignupForm.class);
        startActivity(intent);
    }

    private void CustomerLogin() {
        String phone = edtPhone.getText().toString();
        String password = edtPasswd.getText().toString();
        // --------------ALERTS USER IF ANY OF THE FIELDS ARE EMPTY---------------------------------
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please enter your Phone...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your Password...", Toast.LENGTH_SHORT).show();
        } else {
            //TO ALLOW ACCESS TO SIGNED UP USER
            AllowAccessToUserAccount(phone, password);
        }
    }
    private void openSignupform() {
        Intent i = new Intent(this, SignupForm.class);
        startActivity(i);
    }


    private void AllowAccessToUserAccount(String phone, String password) {
        Boolean phoneExists = MyDb.UserPhoneExists(phone);

        if (phoneExists){
            Boolean passcorrect = MyDb.UserPasswordCorrect(phone, password);

            if (passcorrect){
                if (MyDb.getUserName(phone)!=null)
                    Toast.makeText(CustomerLogin.this,"Welcome "+MyDb.getUserName(phone).toString()+" !",Toast.LENGTH_SHORT).show();
                String n = MyDb.getUserName(phone);
                System.out.println(n+" ");
                Customers c = new Customers(n,phone,password,null,null);
                try{
                    Intent intent = new Intent(CustomerLogin.this, HomePage.class);
                    Prevalent.currentOnlineUser = c;
                    startActivity(intent);
                }catch(Exception e ){
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(CustomerLogin.this,"Sorry! Incorrect Password!",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            //Toast.makeText(MainActivity.this,"Database = "+parentDbName,Toast.LENGTH_SHORT).show();
            Toast.makeText(CustomerLogin.this,"Account with this phone number does not exist!",Toast.LENGTH_SHORT).show();

            Toast.makeText(CustomerLogin.this,"Please create a new account..",Toast.LENGTH_SHORT).show();

        }

    }
    public void openHomepage(){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
}

