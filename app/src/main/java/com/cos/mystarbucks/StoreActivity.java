package com.cos.mystarbucks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cos.mystarbucks.model.User;
import com.cos.mystarbucks.service.LoginService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        Button bt = findViewById(R.id.button3);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LoginService loginService = LoginService.retrofit.create(LoginService.class);

                Call<ResponseBody> call = loginService.test(User.getInstance().getCookie());

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String res = response.body().string();
                            Log.d("================",res);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("==============","실패");
                    }
                });
            }
        });
    }
}
