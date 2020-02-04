package com.cos.mystarbucks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cos.mystarbucks.Adapter.RvAdapterSirenBeverage;
import com.cos.mystarbucks.Adapter.RvAdapterSirenFood;
import com.cos.mystarbucks.model.SirenDTO;
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

    private TextView allMenu, myMenu, history, toolbarText;
    private RecyclerView fRecyclerView, bRecyclerView;
    private RecyclerView.LayoutManager flayoutManager, blayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siren_order);

        navigationSetting();
        toolbarSetting();

        IconIntent();
        recyclerView();
        rvDataSetting();
    }

    private void navigationSetting(){
        toolbar = findViewById(R.id.toolbar);
        menuIcon = findViewById(R.id.menu_icon);
        toolbarText = findViewById(R.id.toolbar_text);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);

        header = navigationView.getHeaderView(0);
        btnLogin = header.findViewById(R.id.btn_login);

        NavigationFactory.setNavigation(getApplicationContext(), drawerLayout, navigationView, header, btnLogin);
    }

    private void toolbarSetting(){
        toolbarText.setText("사이렌 오더");
        toolbarText.setTextSize(20);
        setSupportActionBar(toolbar);
        menuIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    private void IconIntent(){
        allMenu = findViewById(R.id.tv_SirenAllMenu);
        allMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(getApplicationContext(), MenuActivity.class);
                menu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                menu.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(menu);
            }
        });

        myMenu = findViewById(R.id.tv_SirenMyMenu);
        myMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("fragmentId", 1);
                startActivity(intent);
            }
        });

        history = findViewById(R.id.tv_SirenHistory);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("fragmentId", 2);
                startActivity(intent);
            }
        });
    }


    private void recyclerView(){
        blayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        bRecyclerView = findViewById(R.id.siren_menu1);
        bRecyclerView.setHasFixedSize(true);
        bRecyclerView.setLayoutManager(blayoutManager);

        flayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        fRecyclerView = findViewById(R.id.siren_menu2);
        fRecyclerView.setHasFixedSize(true);
        fRecyclerView.setLayoutManager(flayoutManager);
    }

    private void rvDataSetting(){
        final RvAdapterSirenFood fAdapter = new RvAdapterSirenFood(this);
        final RvAdapterSirenBeverage bAdapter = new RvAdapterSirenBeverage(this);

        final SirenService sirenService = SirenService.retrofit.create(SirenService.class);
        Call<SirenDTO> call = sirenService.repoContributors();
        call.enqueue(new Callback<SirenDTO>() {
            @Override
            public void onResponse(Call<SirenDTO> call,
                                   Response<SirenDTO> response) {

                final SirenDTO sirenDTO = response.body();
                fAdapter.addItems(sirenDTO.getFoods());
                bAdapter.addItems(sirenDTO.getBeverages());
                fRecyclerView.setAdapter(fAdapter);
                bRecyclerView.setAdapter(bAdapter);

            }
            @Override
            public void onFailure(Call<SirenDTO> call, Throwable t) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_action_cart, menu) ;
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart :
                Intent cart = new Intent(this.getApplicationContext(), CartActivity.class);
                cart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                cart.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(cart);
                return true ;
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }

}
