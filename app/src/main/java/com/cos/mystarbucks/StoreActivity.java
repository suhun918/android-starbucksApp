package com.cos.mystarbucks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import net.daum.mf.map.api.MapView;


public class StoreActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        toolbarSetting();
        map();
    }

    private void toolbarSetting(){
        toolbar = findViewById(R.id.toolbarBack);
        TextView tv = toolbar.findViewById(R.id.tv_toolbarName);
        tv.setText("매장 위치");

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

    private void map(){
        MapView mapView = new MapView(this);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
    }

}
