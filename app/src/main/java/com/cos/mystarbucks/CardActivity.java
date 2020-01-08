package com.cos.mystarbucks;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CardActivity extends AppCompatActivity implements Runnable{
    // 1. 변수 선언
    Button button1;
    ImageView img1;
    Bitmap bitmap;

    // 메인 스레드와 백그라운드 스레드 간의 통신
    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 서버에서 받아온 이미지를 핸들러를 경유해 이미지뷰에 비트맵 리소스 연결
            img1.setImageBitmap(bitmap);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        // 2. 위젯 연결
        button1 = findViewById(R.id.btn_test);
        img1 = findViewById(R.id.iv_test);


        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Thread th = new Thread(CardActivity.this);
                th.start();
            }
        });
    }

    @Override
    public void run(){
        //http://localhost:8080/resources/static/image/coffee/bb.jpg
        URL url = null;

        try {
            url = new URL("http://192.168.0.50:8080/image/coffee/bb.jpg");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.connect();
            // 스트림 생성
            InputStream is = conn.getInputStream();
            // 스트림에서 받은 데이터를 비트맵 변환
            // 인터넷에서 이미지 가져올 때는 Bitmap을 사용해야함
            bitmap = BitmapFactory.decodeStream(is);

            // 핸들러에게 화면 갱신을 요청한다.
            handler.sendEmptyMessage(0);
            // 연결 종료
            is.close();
            conn.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
