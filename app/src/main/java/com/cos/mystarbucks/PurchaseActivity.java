package com.cos.mystarbucks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cos.mystarbucks.model.User;
import com.cos.mystarbucks.service.SirenService;
import com.cos.mystarbucks.util.RoundedTransform;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.DecimalFormat;

public class PurchaseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView ivImg;
    private TextView tvName, tvPrice, tvCount;
    private Button btnIncrease, btnDecrease, btnCart, btnOrder;
    private int count = 1, price;
    private String name, img;
    private DecimalFormat formatter;

    private User user = User.getInstance();
    private AlertDialog adNotExistCard, adNotPoint, adSuccess, adNotLogin, adQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        minit();
        toolbarSetting();
        receiveData();
        quantity();
        trade();
    }

    private void minit() {
        toolbar = findViewById(R.id.toolbarBack);
        btnCart = findViewById(R.id.btn_cart);
        btnOrder = findViewById(R.id.btn_order);
        formatter = new DecimalFormat("###,###");
    }

    private void toolbarSetting() {
        TextView tv = toolbar.findViewById(R.id.tv_toolbarName);
        tv.setText("메뉴 상세");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);//검정화살표가 나오길래 내가 집어넣는 하얀 화살표
    }

    //툴바버튼 클릭 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {//여기서 버튼별로 인텐트도 가능
            case android.R.id.home: {//toolbar의 back키를 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void receiveData() {
        tvName = findViewById(R.id.tv_purName);
        tvPrice = findViewById(R.id.tv_purPrice);
        ivImg = findViewById(R.id.iv_purImg);

        Intent intent = getIntent();

        name = intent.getExtras().getString("name");
        tvName.setText(name);

        price = intent.getExtras().getInt("price");
        tvPrice.setText(formatter.format(price)+" 원");

        RoundedTransform transform = new RoundedTransform(100, 0);
        img = intent.getExtras().getString("img");
        Picasso.get()
                .load(img)
                .transform(transform)
                .into(ivImg);
    }

    private void quantity() {
        btnIncrease = findViewById(R.id.btn_plus);
        btnDecrease = findViewById(R.id.btn_minus);
        tvCount = findViewById(R.id.tv_quantity);
        tvCount.setText(count + "");


        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count < 20){
                    count = count + 1;
                    tvCount.setText(count + "");
                    tvPrice.setText(formatter.format(count*price)+" 원");
                }
            }
        });

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count > 1){
                    count = count - 1;
                    tvCount.setText(count + "");
                    tvPrice.setText(formatter.format(count*price)+" 원");
                }
            }
        });
    }

    private void trade() {
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getId() != 0) {
                    Intent intent = getIntent();
                    name = intent.getExtras().getString("name");
                    AlertDialog.Builder abQuestion = new AlertDialog.Builder(PurchaseActivity.this);
                    abQuestion
                            .setMessage(name+"을(를) 구매하시겠습니까?")
                            .setCancelable(false)
                            .setPositiveButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setNegativeButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = getIntent();
                                    name = intent.getExtras().getString("name");
                                    price = intent.getExtras().getInt("price");

                                    Map trade = new HashMap();
                                    trade.put("userId", user.getId() + "");
                                    trade.put("name", name);
                                    trade.put("price", price + "");
                                    trade.put("amount", tvCount.getText() + "");

                                    final SirenService sirenService = SirenService.retrofit.create(SirenService.class);
                                    Call<ResponseBody> call = sirenService.order(trade);

                                    call.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            String res = null;
                                            try {
                                                res = response.body().string();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            if (res.equals("noCard")) {
                                                //카드가 없는 경우
                                                AlertDialog.Builder abNotExistCard = new AlertDialog.Builder(PurchaseActivity.this);
                                                abNotExistCard
                                                        .setMessage("카드를 먼저 등록해주시기 바랍니다.")
                                                        .setCancelable(false)
                                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                            }
                                                        })
                                                        .setNegativeButton("등록", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Intent intent = new Intent(getApplicationContext(), CardActivity.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                                startActivity(intent);
                                                            }
                                                        });
                                                adNotExistCard = abNotExistCard.create();
                                                adNotExistCard.show();
                                            } else if (res.equals("noPoint")) {
                                                //포인트가 모자란 경우
                                                AlertDialog.Builder abNotPoint = new AlertDialog.Builder(PurchaseActivity.this);
                                                abNotPoint
                                                        .setMessage("카드 잔액이 부족합니다.")
                                                        .setCancelable(false)
                                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                            }
                                                        })
                                                        .setNegativeButton("충전", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                                startActivity(intent);
                                                            }
                                                        });
                                                adNotPoint = abNotPoint.create();
                                                adNotPoint.show();
                                            } else {
                                                //구매성공
                                                AlertDialog.Builder abSuccess = new AlertDialog.Builder(PurchaseActivity.this);
                                                abSuccess
                                                        .setMessage("구매완료.\n가까운 매장에서 수령해주시기 바랍니다.")
                                                        .setCancelable(false)
                                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                                startActivity(intent);
                                                            }
                                                        });
                                                adSuccess = abSuccess.create();
                                                adSuccess.show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                                        }
                                    });
                                }
                            });
                    adQuestion = abQuestion.create();
                    adQuestion.show();


                } else {
                    AlertDialog.Builder abNotLogin = new AlertDialog.Builder(PurchaseActivity.this);
                    abNotLogin
                            .setMessage("로그인이 필요한 서비스입니다.")
                            .setCancelable(false)
                            .setPositiveButton("로그인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("회원가입", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                }
                            });
                    adNotLogin = abNotLogin.create();
                    adNotLogin.show();

                }

            }
        });
    }


}
