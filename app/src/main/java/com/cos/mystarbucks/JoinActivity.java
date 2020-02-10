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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cos.mystarbucks.service.UserService;
import com.cos.mystarbucks.util.CustomDialogLoading;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private Button btnJoin;
    private EditText etUsername, etPassword, etPasswordCheck, etEmail, etName;
    private AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        toolbarSetting();

        minit();
        setClickEventListener();
    }

    private void toolbarSetting(){
        toolbar = findViewById(R.id.toolbarBack);
        TextView tv = toolbar.findViewById(R.id.tv_toolbarName);
        tv.setText("회원가입");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);//검정화살표가 나오길래 내가 집어넣는 하얀 화살표
    }

    //툴바버튼 클릭 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void minit(){
        etUsername = findViewById(R.id.join_username);
        etPassword = findViewById(R.id.join_password);
        etPasswordCheck = findViewById(R.id.join_password_check);
        etEmail = findViewById(R.id.join_email);
        etName = findViewById(R.id.join_name);

        alertBuilder = new AlertDialog.Builder(this);

        btnJoin = findViewById(R.id.btn_join);
    }

    private void setClickEventListener(){
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String validation = validationCheck();

                if(validation.equals("ok")){
                    Map map = new HashMap();
                    map.put("username", etUsername.getText().toString());
                    map.put("password", etPassword.getText().toString());
                    map.put("email", etEmail.getText().toString());
                    map.put("name", etName.getText().toString());

                    final CustomDialogLoading dialog = new CustomDialogLoading(JoinActivity.this);
                    dialog.show();

                    UserService userService = UserService.retrofit.create(UserService.class);
                    Call<ResponseBody> call = userService.join(map);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();

                            String res = null;
                            try {
                                res = response.body().string();
                            }catch (Exception e){
                                e.printStackTrace();
                                res = "join fail";
                            }

                            if(res.equals("join success")){
                                alertBuilder
                                        .setMessage(etName.getText().toString() + "님,\n회원가입이 완료되었습니다.")
                                        .setCancelable(false) // alert 창 바깥을 터치하거나 뒤로가기 하면 alert 창 사라지는 옵션 (default : true)
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent home = new Intent(getApplicationContext(), LoginActivity.class);
                                                home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                startActivity(home);
                                                finish();
                                            }
                                        });
                                AlertDialog dialog = alertBuilder.create();
                                dialog.show();
                            }else {
                                Toast myToast = Toast.makeText(getApplicationContext(),"이미 사용중인 아이디 입니다", Toast.LENGTH_SHORT);
                                myToast.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            dialog.dismiss();

                            Toast myToast = Toast.makeText(getApplicationContext(),"서버와 연결할 수 없습니다", Toast.LENGTH_SHORT);
                            myToast.show();
                        }
                    });

                }else{
                    Toast myToast = Toast.makeText(getApplicationContext(), validation, Toast.LENGTH_SHORT);
                    myToast.show();
                }

            }
        });
    }

    private String validationCheck(){

        if(etUsername.getText().toString().length()==0 || etPassword.getText().toString().length()==0 || etEmail.getText().toString().length()==0 || etName.getText().toString().length()==0){
            return "빈 칸을 모두 입력해주세요";
        }

        if(etPassword.getText().toString().equals(etPasswordCheck.getText().toString())){

        }else{
            return "비밀번호를 정확하게 입력해주세요";
        }

        String email = etEmail.getText().toString();
        String[] temp = email.split("@");
        if(temp.length<2){
            return "이메일을 정확하게 입력해주세요";
        }

        return "ok";
    }

}
