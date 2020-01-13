package com.cos.mystarbucks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cos.mystarbucks.model.User;
import com.cos.mystarbucks.service.LoginService;
import com.cos.mystarbucks.util.NavigationFactory;
import com.google.android.material.navigation.NavigationView;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView menuIcon;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private View header;
    private Button btnLogin;

    private TextView loginTextView1, loginTextView2;
    private EditText et1, et2;
    private Button bt;

    private AlertDialog.Builder alertBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        minit();
        NavigationFactory.setNavigation(getApplicationContext(), drawerLayout, navigationView, header, btnLogin);
        toolbarSetting();

        setClickEventListener();
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

    private  void setClickEventListener(){
        loginTextView1 = findViewById(R.id.login_text_view1);
        loginTextView2 = findViewById(R.id.login_text_view2);

        String s1 = "안녕하세요.\n스타벅스입니다.";
        String s2 = "회원서비스 이용을 위해 로그인 해주세요";

        loginTextView1.setText(s1);
        loginTextView2.setText(s2);
        loginTextView2.setTextColor(Color.GRAY);

        et1 = findViewById(R.id.usernameInput);
        et2 = findViewById(R.id.passwordInput);

        alertBuilder = new AlertDialog.Builder(this);

        bt = findViewById(R.id.btn_login_active);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map map = new HashMap();
                map.put("username", et1.getText().toString());
                map.put("password", et2.getText().toString());

                final LoginService loginService = LoginService.retrofit.create(LoginService.class);
                Call<User> call = loginService.getUserInfo(map);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call,
                                           Response<User> response) {

                        User u  = response.body();
                        User user = User.getInstance();

                        user.setId(u.getId());
                        user.setUsername(u.getUsername());
                        user.setName(u.getName());
                        user.setEmail(u.getEmail());
                        user.setLevel(u.getLevel());
                        user.setProvider(u.getProvider());
                        user.setProviderId(u.getProviderId());
                        user.setCreateDate(u.getCreateDate());

                        String setCookie = response.headers().get("Set-Cookie");
                        String[] cookie = setCookie.split(";");
                        user.setCookie(cookie[0]);

                        Intent home = new Intent(getApplicationContext(), MainActivity.class);
                        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(home);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        alertBuilder
                                .setMessage("로그인 정보가 일치하지 않습니다.\n아이디나 비밀번호를 확인 후 다시 입력해 주세요.")
                                .setCancelable(false) // alert 창 바깥을 터치하거나 뒤로가기 하면 alert 창 사라지는 옵션 (default : true)
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                        AlertDialog dialog = alertBuilder.create();
                        dialog.show();
                    }
                });
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
