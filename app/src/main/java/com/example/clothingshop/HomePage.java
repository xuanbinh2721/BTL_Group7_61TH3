package com.example.clothingshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.transition.Transition;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_CART = 1;
    private static final int FRAGMENT_SETTING = 2;


    private int currnentFrag = FRAGMENT_HOME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerlayout);

        findViewById(R.id.btnMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

//        ActionBar actionBar = getSupportActionBar();
//        String title = actionBar.getTitle().toString(); //Lấy tiêu đề nếu muốn
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setLogo(R.drawable.ic_menu);    //Icon muốn hiện thị
//        actionBar.setDisplayUseLogoEnabled(true);


        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new content_home_fragment());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home){
            if(currnentFrag != FRAGMENT_HOME){
                replaceFragment(new content_home_fragment());
                currnentFrag = FRAGMENT_HOME;
            }
        } else if (id == R.id.nav_car){
            if(currnentFrag != FRAGMENT_CART){
                replaceFragment(new content_cart_fragment());
                currnentFrag = FRAGMENT_CART;
            }
        } else if (id == R.id.nav_setting) {
            if (currnentFrag != FRAGMENT_SETTING) {
                replaceFragment(new setting_fragment());
                currnentFrag = FRAGMENT_SETTING;
            }
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
    private void replaceFragment (Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentframe, fragment);
        transaction.commit();
    }
}