package com.cos.mystarbucks;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.cos.mystarbucks.util.InicisWebViewClient;
import com.cos.mystarbucks.util.Localhost;

public class PayActivity extends AppCompatActivity {
    private WebView mainWebView;
    private static final String APP_SCHEME = "iamporttest://";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        mainWebView = (WebView) findViewById(R.id.wv_pay);
        mainWebView.setWebViewClient(new InicisWebViewClient(this));
        WebSettings settings = mainWebView.getSettings();
        settings.setSupportMultipleWindows(true); // 새창 띄우기 허용 여부
        settings.setJavaScriptCanOpenWindowsAutomatically(true); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        settings.setJavaScriptEnabled(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(mainWebView, true);
        }

        Intent intent = getIntent();
        Uri intentData = intent.getData();
        int point = Integer.parseInt(intent.getExtras().getString("point"));
        int userId = intent.getExtras().getInt("userId");

        if ( intentData == null ) {

            mainWebView.loadUrl(Localhost.URL+"/mypage/Apay/"+userId+"/"+point);
        } else {
            //isp 인증 후 복귀했을 때 결제 후속조치
            String url = intentData.toString();
            if ( url.startsWith(APP_SCHEME) ) {
                String redirectURL = url.substring(APP_SCHEME.length()+3);
                mainWebView.loadUrl(redirectURL);
                finish();
            }
        }

        mainWebView.addJavascriptInterface(new AndroidBridge(), "Android");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String url = intent.toString();
        if (url.startsWith(APP_SCHEME)) {
            String redirectURL = url.substring(APP_SCHEME.length() + 3);
            mainWebView.loadUrl(redirectURL);
        }
    }

    public class AndroidBridge{
        @JavascriptInterface
        public void finishActivity(){
            finish();
        }
    }


}
