package com.example.clothingshop;

import static com.example.clothingshop.Prevalent.Prevalent.currentOnlineUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clothingshop.Model.Products;
import com.example.clothingshop.Owner.MaintainProduct;
import com.example.clothingshop.Owner.OwnerCategory;
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
    TextView useNameTextView;

    private String userType = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);



        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            // chỉ thực hiện được khi đến từ
            userType = getIntent().getExtras().get("Owner").toString();
        }
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        // add toolbar
        if (!userType.equals("Owner")) {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
            ActionBarDrawerToggle toggle =
                    new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = findViewById(R.id.navigation_view);
            navigationView.setNavigationItemSelectedListener(this);

            View headerView = navigationView.getHeaderView(0);
            useNameTextView = headerView.findViewById(R.id.textName);
            ImageView profileImageView = headerView.findViewById(R.id.imageView);
            useNameTextView.setText(currentOnlineUser.getName());
            //useNameTextView.setText(currentOnlineUser.getName());
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
        recyclerView = findViewById(R.id.product_content);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
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
                        holder.txtProductPrice.setText( model.getPrice()+ "VND" );
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                if (userType.equals("Owner")){
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
            if (!userType.equals("Owner")) {
                Intent intent = new Intent(HomePage.this, HomePage.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_car){
            if (!userType.equals("Owner")) {
                Intent intent = new Intent(HomePage.this, ContentCart.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_setting) {
            if (!userType.equals("Owner")) {
                Intent intent = new Intent(HomePage.this, Setting.class);
                startActivity(intent);
            }
        }
        else if (id == R.id.nav_logout ) {
            if (!userType.equals("Owner")) {
                Intent intent = new Intent(HomePage.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            else{
                Intent intent = new Intent(HomePage.this, OwnerCategory.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    // xu ly khi bam nut back cua thiet bi
    public void onBackPressed(){
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}