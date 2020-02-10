package com.cos.mystarbucks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cos.mystarbucks.model.Document;
import com.cos.mystarbucks.model.Result;
import com.google.gson.Gson;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class PurchaseDoneActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener, Runnable {
    private Toolbar toolbar;
    private TextView textView;

    private MapView mapView;
    private MapPoint.GeoCoordinate center = new MapPoint.GeoCoordinate(0,0);

    private Gson gson = new Gson();

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_done);

        toolbarSetting();
        kakaoMap();

        Intent intent = getIntent();
        textView = findViewById(R.id.tv_purchase_done);
        DecimalFormat formatter = new DecimalFormat("###,###");
        textView.setText("구매완료. 가까운 매장에서 수령해주시기 바랍니다.\n"+"현재 카드 잔액 : "+ formatter.format(intent.getExtras().getInt("restPoint")) + " 원");
    }

    private void toolbarSetting(){
        toolbar = findViewById(R.id.toolbarBack);
        TextView tv = toolbar.findViewById(R.id.tv_toolbarName);
        tv.setText("구매 완료");

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

    private void kakaoMap(){
        mapView = new MapView(this);
        mapView.setZoomLevel(3,false);

        ViewGroup mapViewContainer = findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        // GPS 현재위치 변화 리스너
        mapView.setCurrentLocationEventListener(this);

        // MapView 이벤트 리스너
        mapView.setMapViewEventListener(this);

        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {

            checkRunTimePermission();
        }
    }

    @Override
    public void run() {
        String resultJson = null;
        try {
            // Open the connection
            URL url = new URL("https://dapi.kakao.com/v2/local/search/keyword.json?y="+center.latitude+"&x="+center.longitude+"&radius=500"+"&query=starbucks");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", getString(R.string.kakao_rest_api_key));
            InputStream is = conn.getInputStream();

            // Get the stream
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            // Set the result
            resultJson = builder.toString();
        }
        catch (Exception e) {
            // Error calling the rest api
            Log.e("REST_API", "GET method failed: " + e.getMessage());
            e.printStackTrace();
        }

//        Log.d("rest 결과", resultJson);

        Result result = gson.fromJson(resultJson, Result.class);

        mapView.removeAllPOIItems();
        mapView.removeAllCircles();
        for(Document d : result.getDocuments()){
            double longitude = Double.parseDouble(d.getX());
            double latitude = Double.parseDouble(d.getY());

            MapPOIItem poiItem = new MapPOIItem();
            poiItem.setItemName(d.getPlace_name());
            poiItem.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
            poiItem.setMarkerType(MapPOIItem.MarkerType.BluePin);
            mapView.addPOIItem(poiItem);
        }
//        MapCircle circle = new MapCircle(MapPoint.mapPointWithGeoCoord(center.latitude, center.longitude), Integer.parseInt(editText2.getText().toString()), Color.RED, Color.argb(60,0,255,0));
//        mapView.addCircle(circle);

    }

    //=================== MapView.CurrentLocationEventListener 인터페이스 구현 ============================
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentPoint, float accuracyInMeters) {
        double latitude = currentPoint.getMapPointGeoCoord().latitude;
        double longitude = currentPoint.getMapPointGeoCoord().longitude;
        center.latitude = latitude;
        center.longitude = longitude;
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }
//=================== MapView.CurrentLocationEventListener 인터페이스 구현 [끝] ============================

    //=========================== MapView.MapViewEventListener 인터페이스 구현 ============================
    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint currentPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        double latitude = mapPoint.getMapPointGeoCoord().latitude;
        double longitude = mapPoint.getMapPointGeoCoord().longitude;
        center.latitude = latitude;
        center.longitude = longitude;

        Thread thread = new Thread(this);
        thread.start();
    }
//=========================== MapView.MapViewEventListener 인터페이스 구현 [끝] ============================

    //======================================= GPS 권한 획득 관련 메서드 =======================================
    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(PurchaseDoneActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED ) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
            mapView.setShowCurrentLocationMarker(true);


        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(PurchaseDoneActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(PurchaseDoneActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(PurchaseDoneActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(PurchaseDoneActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(PurchaseDoneActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
//======================================= GPS 권한 획득 관련 메서드 [끝] =======================================

}
