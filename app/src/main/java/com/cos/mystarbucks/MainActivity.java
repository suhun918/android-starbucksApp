package com.cos.mystarbucks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        menuIcon = findViewById(R.id.menu_icon);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        Toast.makeText(MainActivity.this, "nav_home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_sirenOrder:
                        Toast.makeText(MainActivity.this, "nav_sirenOrder", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_card:
                        Toast.makeText(MainActivity.this, "nav_card", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_store:
                        Toast.makeText(MainActivity.this, "nav_store", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_whatsNew:
                        Toast.makeText(MainActivity.this, "nav_whatsNew", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_menu:
                        Toast.makeText(MainActivity.this, "nav_menu", Toast.LENGTH_SHORT).show();
                        break;

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        toolbarSetting();
    }

    private void toolbarSetting(){
        setSupportActionBar(toolbar);
        menuIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }







}
