package com.cos.mystarbucks.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cos.mystarbucks.MyPageActivity;
import com.cos.mystarbucks.R;
import com.cos.mystarbucks.model.User;
import com.cos.mystarbucks.service.MyPageService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CustomDialogRecharge extends Dialog {

    private MyPageActivity activity;

    private Button leftButton;
    private Button rightButton;
    private EditText etPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_recharge);

        leftButton = findViewById(R.id.btn_recharge);
        rightButton = findViewById(R.id.btn_cancel);
        etPoint = findViewById(R.id.et_point);

        // 충전 버튼
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 충전금액 입력해야 함
                if(etPoint.getText().toString().length() > 0){
                    User user = User.getInstance();

                    MyPageService myPageService = MyPageService.retrofit.create(MyPageService.class);
                    Call<ResponseBody> call = myPageService.recharge( user.getCookie(), Integer.parseInt(etPoint.getText().toString()) );
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dismiss();

                            String res = null;
                            try {
                                res = response.body().string();
                            }catch (Exception e){
                                e.printStackTrace();
                                res = "notlogin";
                            }

                            if(res.equals("1")){ // 충전완료
                                activity.finish();
                                activity.startActivity(new Intent(activity, MyPageActivity.class));
                            }else if(res.equals("notlogin")){ // 세션없음
                                Toast myToast = Toast.makeText(activity,"다시 로그인 해주세요", Toast.LENGTH_SHORT);
                                myToast.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }

            }
        });

        // 취소 버튼
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public CustomDialogRecharge(@NonNull Context context) {
        super(context);
        activity = (MyPageActivity)context;
    }
}
