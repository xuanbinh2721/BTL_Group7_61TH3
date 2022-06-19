package com.example.clothingshop.Owner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.clothingshop.DatabaseHelperOwner;
import com.example.clothingshop.DatabaseHelperOwner;
import com.example.clothingshop.R;

public class OwnerLogin extends AppCompatActivity {
    // FOR ADMINS DATABASE
    DatabaseHelperOwner ownerDb;

    //  FOR BACKGROUND
    private ConstraintLayout constraintLayout;
    private AnimationDrawable animationDrawable;


    Button btnOwnLogin;

    // FOR EDIT TEXTS
    public EditText edtOwnPhone,edtOwnPassword;

    // FOR PROGRESS DIALOG BAR
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_login);


        ownerDb = new DatabaseHelperOwner(this);

        // TO INITIALISE THE ADMIN DB WITH AN ADMIN
        ownerDb.insertDataOwner("LongCT","123","Long");
        ownerDb.insertDataOwner("BinCB","1","Bih");


        // LOGIN ACTIVITY FROM MAIN PAGE
        btnOwnLogin = (Button)findViewById(R.id.btnOwnLogin);
        edtOwnPhone = (EditText)findViewById(R.id.edtOwnPhone);
        edtOwnPassword = (EditText)findViewById(R.id.edtOwnPw);

        btnOwnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                OwnerLogin();
            }
        });

    }
    private void OwnerLogin(){
        String phone    = edtOwnPhone.getText().toString();
        String password = edtOwnPassword.getText().toString();
        // --------------ALERTS USER IF ANY OF THE FIELDS ARE EMPTY---------------------------------
        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Please enter your Phone...",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter your Password...",Toast.LENGTH_SHORT).show();
        }
        else{

            //TO ALLOW ACCESS TO SIGNED UP USER
            AllowOwnerAccess(phone,password);
        }
        //------------------------------------------------------------------------------------------
    }
    // FOR ADMIN LOGIN
    private void AllowOwnerAccess(String phone,String password) {
        Boolean phoneExists = ownerDb.OwnerPhoneExists(phone);
        if (phoneExists){
            Boolean passCorrect = ownerDb.OwnerPasswordCorrect(phone, password);
            //System.out.println("Password correct = "+ passCorrect);
            if (passCorrect){
                Toast.makeText(OwnerLogin.this,"Welcome "+ ownerDb.getOwnerName(phone).toString()+" !",Toast.LENGTH_SHORT).show();
                try{
                    Intent intent = new Intent(OwnerLogin.this, OwnerCategory.class);
                    startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(OwnerLogin.this,"Sorry! Incorrect Password!",Toast.LENGTH_SHORT).show();

            }
        }
        else{
            //Toast.makeText(MainActivity.this,"Database = "+parentDbName,Toast.LENGTH_SHORT).show();
            Toast.makeText(OwnerLogin.this,"Account with this phone number does not exist!",Toast.LENGTH_SHORT).show();

            Toast.makeText(OwnerLogin.this,"Please create a new account..",Toast.LENGTH_SHORT).show();

        }
    }
}
