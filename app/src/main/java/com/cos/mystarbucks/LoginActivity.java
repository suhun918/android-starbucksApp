package com.cos.mystarbucks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cos.mystarbucks.model.User;
import com.cos.mystarbucks.service.UserService;
import com.cos.mystarbucks.util.NavigationFactory;
import com.google.android.material.navigation.NavigationView;
import com.kakao.auth.Session;

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
    private EditText etUsername, etPassword;
    private TextView tvJoin;
    private Button btnLoginAction;

    private AlertDialog.Builder alertBuilder;

//  카카오로그인
    private SessionCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//      카카오로그인
        callback = new SessionCallback(this);
        Session.getCurrentSession().addCallback(callback);
//        ======================
        navigationSetting();
        toolbarSetting();

        minit();
        setClickEventListener();


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

    private void minit(){
        loginTextView1 = findViewById(R.id.login_text_view1);
        loginTextView2 = findViewById(R.id.login_text_view2);

        etUsername = findViewById(R.id.usernameInput);
        etPassword = findViewById(R.id.passwordInput);

        tvJoin = findViewById(R.id.tv_Login_Join);
        btnLoginAction = findViewById(R.id.btn_login_active);

        alertBuilder = new AlertDialog.Builder(this);


        String s1 = "안녕하세요.\n스타벅스입니다.";
        String s2 = "회원서비스 이용을 위해 로그인 해주세요";
        loginTextView1.setText(s1);
        loginTextView2.setText(s2);
        loginTextView2.setTextColor(Color.GRAY);
    }

    private  void setClickEventListener(){
        tvJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        btnLoginAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etUsername.getText().toString().length()==0 || etPassword.getText().toString().length()==0){
                    Toast myToast = Toast.makeText(getApplicationContext(),"아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT);
                    myToast.show();
                }else {

                    Map map = new HashMap();
                    map.put("username", etUsername.getText().toString());
                    map.put("password", etPassword.getText().toString());

                    // ProgressDialog 설정
                    final ProgressDialog progressDialog;
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("로그인 중입니다.");
                    progressDialog.setCancelable(false);
                    // ProgressDialog 띄우기
                    progressDialog.show();

                    final UserService userService = UserService.retrofit.create(UserService.class);
                    Call<User> call = userService.login(map);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call,
                                               Response<User> response) {
                            progressDialog.dismiss();

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

                            finish();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            progressDialog.dismiss();

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


