package com.example.clothingshop.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clothingshop.R;

public class OwnerCategory extends AppCompatActivity {
    Button btnAddProduct;
    Button btnMaintain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_category);

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

    private void openMaintainProduct() {
        Intent i = new Intent( this, MaintainProduct.class);
        startActivity(i);
    }

    private void openAddProduct() {
        Intent i = new Intent(this,OwnerAddProduct.class);
        startActivity(i);
    }
}
