package com.cos.mystarbucks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.cos.mystarbucks.Adapter.RvAdapterCart;
import com.cos.mystarbucks.model.CartDTO;
import com.cos.mystarbucks.model.User;
import com.cos.mystarbucks.service.SirenService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView tvAllCount, tvAllPrice, tvNoCart, tvNotLoginCart;
    private Button btnCartPurchase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        minit();
        toolbarSetting();
        setRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        rvDataSetting();
    }

    private void minit() {
        toolbar = findViewById(R.id.toolbarBack);
        tvAllCount = findViewById(R.id.tv_cart_allcount);
        tvAllPrice = findViewById(R.id.tv_cart_allprice);
        tvNoCart = findViewById(R.id.tv_no_cart);
        tvNotLoginCart = findViewById(R.id.tv_not_login_cart);
        btnCartPurchase = findViewById(R.id.btn_cart_purchase);
    }

    private void toolbarSetting() {
        TextView tv = toolbar.findViewById(R.id.tv_toolbarName);
        tv.setText("담기");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);//검정화살표가 나오길래 내가 집어넣는 하얀 화살표


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_action_add_cart, menu) ;
        return true ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //toolbar의 back키를 눌렀을 때 동작
                finish();
                return true;
            case R.id.action_add :
                Intent menu = new Intent(this.getApplicationContext(), MenuActivity.class);
                menu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                menu.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(menu);
                return true ;
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }
    private void setRecyclerView(){
        layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.rv_cart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void rvDataSetting(){
        User user = User.getInstance();

        Map map = new HashMap();
        map.put("userId", user.getId()+"");
        final RvAdapterCart Adapter = new RvAdapterCart(this);

        final SirenService sirenService = SirenService.retrofit.create(SirenService.class);
        Call<CartDTO> call = sirenService.cartrepoContributors(map);
        call.enqueue(new Callback<CartDTO>() {
            @Override
            public void onResponse(Call<CartDTO> call, Response<CartDTO> response) {
                final CartDTO cartDTO = response.body();
                Adapter.addItems(cartDTO.getCarts(), tvAllCount, tvAllPrice, btnCartPurchase, tvNoCart, tvNotLoginCart);
                recyclerView.setAdapter(Adapter);
            }

            @Override
            public void onFailure(Call<CartDTO> call, Throwable t) {

            }
        });
    }


}
