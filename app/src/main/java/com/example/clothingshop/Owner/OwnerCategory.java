package com.example.clothingshop.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clothingshop.HomePage;
import com.example.clothingshop.MainActivity;
import com.example.clothingshop.R;

public class OwnerCategory extends AppCompatActivity {
    Button btnAddProduct, btnMaintain, btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_category);
        btnLogout = (Button) findViewById(R.id.OwnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutOwner();
            }
        });
        btnAddProduct = findViewById(R.id.btnaddProduct);
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddProduct();
            }
        });
        btnMaintain = findViewById(R.id.btnMaintain);
        btnMaintain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMaintainProduct();
            }
        });
    }

    private void logoutOwner() {
        Intent i = new Intent( this, MainActivity.class);
        startActivity(i);
    }

    private void openMaintainProduct() {
        Intent i = new Intent( this, HomePage.class);
        i.putExtra("Owner","Owner");
        startActivity(i);
    }

    private void openAddProduct() {
        Intent i = new Intent(this,OwnerAddProduct.class);
        startActivity(i);
    }
}
