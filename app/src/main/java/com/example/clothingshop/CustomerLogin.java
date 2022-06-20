package com.example.clothingshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CustomerLogin extends AppCompatActivity {
    Button btnLogin;
    Button btntoSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);
        btntoSignUp = (Button) findViewById(R.id.btntoSignupform);
        btntoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignupform();
            }
        });

        btnLogin = (Button) findViewById(R.id.CustomLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHomePage();
            }
        });
    }
    private void openHomePage() {
        Intent i = new Intent(this, HomePage.class);
        startActivity(i);
    }

    private void openSignupform() {
        Intent i = new Intent(this, SignupForm.class);
        startActivity(i);
    }
}
