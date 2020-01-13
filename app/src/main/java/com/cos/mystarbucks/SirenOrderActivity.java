package com.cos.mystarbucks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cos.mystarbucks.Adapter.RvAdapterSirenBeverage;
import com.cos.mystarbucks.Adapter.RvAdapterSirenCoffee;
import com.cos.mystarbucks.model.Siren;
import com.cos.mystarbucks.model.User;
import com.cos.mystarbucks.service.SirenService;
import com.cos.mystarbucks.util.NavigationFactory;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SirenOrderActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView menuIcon;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private View header;
    private Button btnLogin;

    private TextView AllMenuIcon;
    private RecyclerView cRecyclerView, bRecyclerView;
    private RecyclerView.LayoutManager clayoutManager, blayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siren_order);

        minit();
        NavigationFactory.setNavigation(getApplicationContext(), drawerLayout, navigationView, header, btnLogin);
        toolbarSetting();

        IconIntent();
        recyclerView();
        rvDataSetting();
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

    private void IconIntent(){
        AllMenuIcon = findViewById(R.id.tv_SirenAllMenu);
        AllMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(getApplicationContext(), MenuActivity.class);
                menu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                menu.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(menu);
            }
        });
    }


    private void recyclerView(){
        clayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        cRecyclerView = findViewById(R.id.siren_menu1);
        cRecyclerView.setHasFixedSize(true);
        cRecyclerView.setLayoutManager(clayoutManager);

        blayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        bRecyclerView = findViewById(R.id.siren_menu2);
        bRecyclerView.setHasFixedSize(true);
        bRecyclerView.setLayoutManager(blayoutManager);
    }

    private void rvDataSetting(){
        final RvAdapterSirenCoffee cAdapter = new RvAdapterSirenCoffee(this);
        final RvAdapterSirenBeverage bAdapter = new RvAdapterSirenBeverage(this);

        final SirenService sirenService = SirenService.retrofit.create(SirenService.class);
        Call<Siren> call = sirenService.repoContributors();
        call.enqueue(new Callback<Siren>() {
            @Override
            public void onResponse(Call<Siren> call,
                                   Response<Siren> response) {

                final Siren siren = response.body();
                cAdapter.addItems(siren.getCoffees());
                bAdapter.addItems(siren.getBeverages());
                cRecyclerView.setAdapter(cAdapter);
                bRecyclerView.setAdapter(bAdapter);

            }
            @Override
            public void onFailure(Call<Siren> call, Throwable t) {
            }
        });

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
