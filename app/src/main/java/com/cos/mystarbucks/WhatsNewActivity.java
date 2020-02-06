package com.cos.mystarbucks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cos.mystarbucks.Adapter.RVAdapterWhatsNew;
import com.cos.mystarbucks.model.BoardDTO;
import com.cos.mystarbucks.model.User;
import com.cos.mystarbucks.service.WhatsNewService;
import com.cos.mystarbucks.util.NavigationFactory;
import com.google.android.material.navigation.NavigationView;

import java.sql.Timestamp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WhatsNewActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView menuIcon;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private View header;
    private Button btnLogin;



    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RVAdapterWhatsNew adapter = new RVAdapterWhatsNew(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_new);

        navigationSetting();
        toolbarSetting();

        recyclerView();
        rvDataSetting();

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


    private void recyclerView(){
        layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.rv_what);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                // newState = 스크롤이 움직이기 시작할 때 : 1 / 화면에서 손 땔 때 : 0, / 스크롤 움직이다가 끝 부분(위, 아래)에 걸려서 멈출 때 : 2
//                super.onScrollStateChanged(recyclerView, newState);
//
//                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
//                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
//                    int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
//
//                    if (lastVisibleItemPosition == itemTotalCount) {
//                        BoardDTO.Board asdf = new BoardDTO().new Board();
//                        asdf.setTitle((lastVisibleItemPosition + 1) + "");
//                        asdf.setCreateDate(new Timestamp(10000));
//                        adapter.addItem(asdf);
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//            }
//
//        });
    }

    private void rvDataSetting(){
//        final RVAdapterWhatsNew Adapter = new RVAdapterWhatsNew(this);

        final WhatsNewService whatsNewService = WhatsNewService.retrofit.create(WhatsNewService.class);
        Call<BoardDTO> call = whatsNewService.repoContributors();
        call.enqueue(new Callback<BoardDTO>() {
            @Override
            public void onResponse(Call<BoardDTO> call,
                                   Response<BoardDTO> response) {

                final BoardDTO boardDTO = response.body();
                adapter.addItems(boardDTO.getBoards());
                recyclerView.setAdapter(adapter);

            }
            @Override
            public void onFailure(Call<BoardDTO> call, Throwable t) {
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
