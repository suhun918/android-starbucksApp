package com.cos.mystarbucks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cos.mystarbucks.model.MyPageDTO;
import com.cos.mystarbucks.model.User;
import com.cos.mystarbucks.service.MyPageService;
import com.cos.mystarbucks.util.NavigationFactory;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView menuIcon;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private View header;
    private Button btnLogin;

    private FragmentAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private User user;
    public MyPageDTO myPageDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        navigationSetting();
        toolbarSetting();
    }

    @Override
    protected void onResume() {
        super.onResume();

        user = User.getInstance();

        getData();
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

    private void getData(){

        // ProgressDialog 설정
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(MyPageActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("데이터 받아오는 중...");
        progressDialog.setCancelable(false);
        // ProgressDialog 띄우기
        progressDialog.show();

        final MyPageService myPageService = MyPageService.retrofit.create(MyPageService.class);
        Call<MyPageDTO> call = myPageService.repoContributors(user.getCookie());
        call.enqueue(new Callback<MyPageDTO>() {
            @Override
            public void onResponse(Call<MyPageDTO> call, Response<MyPageDTO> response) {
                btnLogin.setText("로그아웃");
                TextView tv = header.findViewById(R.id.tv_name);
                tv.setText(user.getName());
                tv.setVisibility(View.VISIBLE);

                myPageDTO = response.body();

                progressDialog.dismiss();

                // 데이터 받아오면 viewpager, fragment 세팅
                fragmentViewPager();
            }

            @Override
            public void onFailure(Call<MyPageDTO> call, Throwable t) {
                progressDialog.dismiss();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

                finish();
            }
        });
    }

    private void fragmentViewPager(){
        viewPager = findViewById(R.id.vp_container);
        tabLayout = findViewById(R.id.tabs);

        adapter = new FragmentAdapter(getSupportFragmentManager(),1);

        adapter.addFragment(new FragmentCardRecharge(this));
        adapter.addFragment(new FragmentMyMenu(this));
        adapter.addFragment(new FragmentHistory(this));

        // ViewPager와 Fragment 연결
        viewPager.setAdapter(adapter);

        // ViewPager와 TabLayout 연결
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("내 카드");
        tabLayout.getTabAt(1).setText("My 메뉴");
        tabLayout.getTabAt(2).setText("구매내역");
    }

}
