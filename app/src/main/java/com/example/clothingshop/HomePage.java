package com.example.clothingshop;

import static com.example.clothingshop.Prevalent.Prevalent.currentOnlineUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.clothingshop.Model.Products;
import com.example.clothingshop.Owner.MaintainProduct;
import com.example.clothingshop.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView textNameLogin;

    private String userType = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        recyclerView = findViewById(R.id.product_content);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            // this will work only if we come from the admin activity
            userType = getIntent().getExtras().get("Admin").toString();
        }
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        // add toolbar
        if (!userType.equals("Admin")) {
            DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
            ActionBarDrawerToggle toggle =
                    new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_drawer_open, R.string.nav_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = findViewById(R.id.navigation_view);
            navigationView.setNavigationItemSelectedListener(this);

            findViewById(R.id.btnMenu).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
            View headerView = navigationView.getHeaderView(0);
            TextView useNameTextView = headerView.findViewById(R.id.textName);
            ImageView profileImageView = headerView.findViewById(R.id.imageView);


            useNameTextView.setText(currentOnlineUser.getName());
            //Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);
            DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentOnlineUser.getPhone());
            UsersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.child("image").exists()) {
                            String image = dataSnapshot.child("image").getValue().toString();
                            Picasso.get().load(image).placeholder(R.drawable.avatar).into(profileImageView);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    protected void onStart(){
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef, Products.class)
                        .build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@androidx.annotation.NonNull ProductViewHolder holder, int position, @android.support.annotation.NonNull final Products model){
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = Rs. " + model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                if (userType.equals("Admin")){
                                    Intent intent = new Intent(HomePage.this, MaintainProduct.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(HomePage.this,ProductDetails.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    @Override
                    public ProductViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType){
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home){
//            if(currnentFrag != FRAGMENT_HOME){
//                replaceFragment(new content_home_fragment());
//                currnentFrag = FRAGMENT_HOME;
//

        } else if (id == R.id.nav_car){
            Intent intent = new Intent(HomePage.this, ContentCart.class);
            startActivity(intent);
//            if(currnentFrag != FRAGMENT_CART){
//                replaceFragment(new content_cart_fragment());
//                currnentFrag = FRAGMENT_CART;
//            }
        } else if (id == R.id.nav_setting) {
//            if (currnentFrag != FRAGMENT_SETTING) {
//                replaceFragment(new setting_fragment());
//                currnentFrag = FRAGMENT_SETTING;
//            }
            Intent intent = new Intent(HomePage.this, setting.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_logout ) {
           finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    // xu ly khi bam nut back cua thiet bi
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}