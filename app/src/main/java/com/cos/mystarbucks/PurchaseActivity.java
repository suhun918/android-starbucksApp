package com.cos.mystarbucks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cos.mystarbucks.model.User;
import com.cos.mystarbucks.util.NavigationFactory;
import com.google.android.material.navigation.NavigationView;

public class PurchaseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView menuIcon;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private View header;
    private Button btnLogin;

    private TextView name, price, img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        minit();
        NavigationFactory.setNavigation(getApplicationContext(), drawerLayout, navigationView, header, btnLogin);
        toolbarSetting();

        receiveData();
    }

    private void minit(){
        toolbar = findViewById(R.id.toolbar);
        menuIcon = findViewById(R.id.menu_icon);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);

        header = navigationView.getHeaderView(0);
        btnLogin = header.findViewById(R.id.btn_login);
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

    private void receiveData(){
        name = findViewById(R.id.tv_name);
        price = findViewById(R.id.tv_price);
        img = findViewById(R.id.tvimg);

        Intent intent = getIntent();

        String cName = intent.getExtras().getString("name");
        name.setText(cName);

        int cPrice = intent.getExtras().getInt("price");
        price.setText(String.valueOf(cPrice));

        String cImg = intent.getExtras().getString("img");
        img.setText(cImg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = User.getInstance();
        TextView tv = header.findViewById(R.id.tv_name);
        if(user.getId() != 0){
            btnLogin.setText("로그아웃");
            tv.setText(user.getName());
            tv.setVisibility(View.VISIBLE);
        }else{
            btnLogin.setText("로그인");
            tv.setVisibility(View.GONE);
        }
    }
}
