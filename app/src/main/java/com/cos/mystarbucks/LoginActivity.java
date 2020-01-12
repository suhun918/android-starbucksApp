package com.cos.mystarbucks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cos.mystarbucks.model.User;
import com.cos.mystarbucks.service.LoginService;
import com.cos.mystarbucks.util.Localhost;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menuIcon;

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

        setClickEventListener();
        drawerLayout();
        toolbarSetting();
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


    private void drawerLayout() {
        toolbar = findViewById(R.id.toolbar);
        menuIcon = findViewById(R.id.menu_icon);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        header = navigationView.getHeaderView(0);
        btnLogin = header.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                login.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(login);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        Intent home = new Intent(getApplicationContext(), MainActivity.class);
                        //플래그. 간단히 현재 액티비티에서 어느 액티비티로 이동하는데, 스택 중간에 있었던 액티비티들을 지우는 역할은 한다고 이해하면 된다. 이 플래그가 없으면, 중간에 액티비티는 스택에 그대로 남아있기 때문에 이동 중간에  화면에 표출되어 UI 흐름을 망친다. 또한 시간이 지나면서 수 많은 액티비티가 쌓이게 되어 메모리 낭비를 초래한다.
                        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //"FLAG_ACTIVITY_SINGLE_TOP" 플래그. 띄우려는 액티비티가 스택 맨위에 이미 실행 중이라면 재사용하겠다는 의미로 해석하면 된다.
                        home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(home);

                        break;
                    case R.id.nav_sirenOrder:

                        Intent siren = new Intent(getApplicationContext(), SirenOrderActivity.class);
                        siren.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        siren.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(siren);
                        break;
                    case R.id.nav_card:

                        Intent card = new Intent(getApplicationContext(), CardActivity.class);
                        card.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        card.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(card);
                        break;
                    case R.id.nav_store:

                        Intent store = new Intent(getApplicationContext(), StoreActivity.class);
                        store.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        store.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(store);
                        break;
                    case R.id.nav_whatsNew:

                        Intent whatsNew = new Intent(getApplicationContext(), WhatsNewActivity.class);
                        whatsNew.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        whatsNew.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(whatsNew);
                        break;
                    case R.id.nav_menu:

                        Intent menu = new Intent(getApplicationContext(), MenuActivity.class);
                        menu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        menu.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(menu);
                        break;

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
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
