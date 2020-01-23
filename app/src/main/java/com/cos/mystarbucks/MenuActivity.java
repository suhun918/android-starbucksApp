package com.cos.mystarbucks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cos.mystarbucks.model.User;
import com.cos.mystarbucks.util.NavigationFactory;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class MenuActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView menuIcon;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private View header;
    private Button btnLogin;

    private FragmentAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        navigationSetting();
        toolbarSetting();

        fragmentViewPager();
    }

    private void navigationSetting(){
        toolbar = findViewById(R.id.toolbar);
        menuIcon = findViewById(R.id.menu_icon);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);

        header = navigationView.getHeaderView(0);
        btnLogin = header.findViewById(R.id.btn_login);

        NavigationFactory.setNavigation(getApplicationContext(), drawerLayout, navigationView, header, btnLogin);
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

    private void fragmentViewPager(){
        viewPager = findViewById(R.id.vp_container);
        tabLayout = findViewById(R.id.tabs);

        adapter = new FragmentAdapter(getSupportFragmentManager(),1);

        adapter.addFragment(new FragmentCoffee(this));
        adapter.addFragment(new FragmentBeverage(this));
        adapter.addFragment(new FragmentFood(this));

        // ViewPager와 Fragment 연결
        viewPager.setAdapter(adapter);

        // ViewPager와 TabLayout 연결
        tabLayout.setupWithViewPager(viewPager); // 메뉴 3개 만들어짐

        tabLayout.getTabAt(0).setText("원두");
        tabLayout.getTabAt(1).setText("음료");
        tabLayout.getTabAt(2).setText("음식");

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
