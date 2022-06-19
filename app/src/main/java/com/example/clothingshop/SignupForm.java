package com.example.clothingshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignupForm extends AppCompatActivity {
    Button btnbackLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_form);
        btnbackLogin = (Button) findViewById(R.id.btnBackLogin);
        btnbackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backtoLogin();
            }
        });
    }

    public void backtoLogin() {
        Intent i = new Intent(this, CustomerLogin.class);
        startActivity(i);
    }
}
