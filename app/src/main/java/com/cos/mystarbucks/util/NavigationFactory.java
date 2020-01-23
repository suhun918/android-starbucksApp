package com.cos.mystarbucks.util;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cos.mystarbucks.CardActivity;
import com.cos.mystarbucks.LoginActivity;
import com.cos.mystarbucks.MainActivity;
import com.cos.mystarbucks.MenuActivity;
import com.cos.mystarbucks.MyPageActivity;
import com.cos.mystarbucks.R;
import com.cos.mystarbucks.SirenOrderActivity;
import com.cos.mystarbucks.StoreActivity;
import com.cos.mystarbucks.WhatsNewActivity;
import com.cos.mystarbucks.model.User;
import com.google.android.material.navigation.NavigationView;

public class NavigationFactory {

    public static void setNavigation(final Context context, final DrawerLayout drawerLayout, NavigationView navigationView, final View header, final Button btnLogin){

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnLogin.getText().equals("로그인")){
                    Intent login = new Intent(context.getApplicationContext(), LoginActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    login.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(login);
                }else{
                    User user = User.getInstance();
                    user.setId(0);
                    user.setCookie(null);

                    TextView tv = header.findViewById(R.id.tv_name);
                    btnLogin.setText("로그인");
                    tv.setVisibility(View.GONE);
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent home = new Intent(context.getApplicationContext(), MainActivity.class);
                        //플래그. 간단히 현재 액티비티에서 어느 액티비티로 이동하는데, 스택 중간에 있었던 액티비티들을 지우는 역할은 한다고 이해하면 된다. 이 플래그가 없으면, 중간에 액티비티는 스택에 그대로 남아있기 때문에 이동 중간에  화면에 표출되어 UI 흐름을 망친다. 또한 시간이 지나면서 수 많은 액티비티가 쌓이게 되어 메모리 낭비를 초래한다.
                        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //"FLAG_ACTIVITY_SINGLE_TOP" 플래그. 띄우려는 액티비티가 스택 맨위에 이미 실행 중이라면 재사용하겠다는 의미로 해석하면 된다.
                        home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        // Activity가 아닌곳에서 startActivity()를 사용할 경우 필요한 플래그
                        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(home);

                        break;
                    case R.id.nav_sirenOrder:

                        Intent siren = new Intent(context.getApplicationContext(), SirenOrderActivity.class);
                        siren.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        siren.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        siren.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(siren);
                        break;
                    case R.id.nav_card:

                        Intent card = new Intent(context.getApplicationContext(), CardActivity.class);
                        card.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        card.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        card.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(card);
                        break;
                    case R.id.nav_store:

                        Intent store = new Intent(context.getApplicationContext(), StoreActivity.class);
                        store.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        store.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        store.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(store);
                        break;
                    case R.id.nav_whatsNew:

                        Intent whatsNew = new Intent(context.getApplicationContext(), WhatsNewActivity.class);
                        whatsNew.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        whatsNew.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        whatsNew.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(whatsNew);
                        break;
                    case R.id.nav_menu:

                        Intent menu = new Intent(context.getApplicationContext(), MenuActivity.class);
                        menu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        menu.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        menu.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(menu);
                        break;
                    case R.id.nav_myPage:

                        Intent myPage = new Intent(context.getApplicationContext(), MyPageActivity.class);
                        myPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myPage.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        myPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(myPage);
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
}
