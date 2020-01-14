package com.cos.mystarbucks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cos.mystarbucks.model.User;
import com.cos.mystarbucks.util.Localhost;
import com.cos.mystarbucks.util.NavigationFactory;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class PurchaseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView ivImg;
    private TextView tvName, tvPrice, tvCount;
    private Button btnIncrease, btnDecrease, btnCart, btnOrder;
    private int count = 1, price, ePrice;
    private String name, img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        minit();
        toolbarSetting();
        receiveData();
        quantity();
    }

    private void minit(){
        toolbar = findViewById(R.id.toolbarPurchase);
        btnCart = findViewById(R.id.btn_cart);
        btnOrder = findViewById(R.id.btn_order);


    }

    private void toolbarSetting(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);//검정화살표가 나오길래 내가 집어넣는 하얀 화살표
        }

    //툴바버튼 클릭 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){//여기서 버튼별로 인텐트도 가능
            case android.R.id.home:{//toolbar의 back키를 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void receiveData(){
        tvName = findViewById(R.id.tv_purName);
        tvPrice = findViewById(R.id.tv_purPrice);
        ivImg = findViewById(R.id.iv_purImg);

        Intent intent = getIntent();

        name = intent.getExtras().getString("name");
        tvName.setText(name);

        price = intent.getExtras().getInt("price");
        tvPrice.setText(price+"원");

        img = intent.getExtras().getString("img");
        Picasso.get()
                .load(img)
                .into(ivImg);
    }

    private void quantity(){
        btnIncrease = findViewById(R.id.btn_plus);
        btnDecrease = findViewById(R.id.btn_minus);
        tvCount = findViewById(R.id.tv_quantity);
        tvCount.setText(count+"");


        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count<20){
                    count = count+1;
                    tvCount.setText(count+"");
                    tvPrice.setText(count*price+"원");

                }
            }
        });

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count>1){
                    count = count-1;
                    tvCount.setText(count+"");
                    tvPrice.setText(count*price+"원");
                }

            }
        });
    }

}
