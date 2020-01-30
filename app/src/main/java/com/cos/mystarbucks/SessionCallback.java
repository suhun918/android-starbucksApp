package com.cos.mystarbucks;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.cos.mystarbucks.model.User;
import com.cos.mystarbucks.service.UserService;
import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionCallback implements ISessionCallback {
    private Activity act;

    public SessionCallback(Activity act) {
        this.act = act;
    }

    //로그인 성공
    @Override
    public void onSessionOpened() {
        requestMe();
    }

    //로그인 실패
    @Override
    public void onSessionOpenFailed(KakaoException exception) {

    }

    /**
     * 사용자 정보 요청 함수_별도의 권한 없이 제공
     * id : 인증 여부를 확인하는 user의 id(long)
     * UUID : 앱과 연동 시에 발급 되는 고유한 id 정보(string)
     * nickname : 사용자 별명(string)
     * thumbnailImagePath : 썸네일 프로필 이미지 경로(string)
     */

    public void requestMe() {

        UserManagement.requestMe(new MeResponseCallback() {
            // 세션 오픈 실패, 세션이 삭제된 경우
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("SessionCallback :: ", "onSessionClosed : " + errorResult.getErrorMessage());
            }

            //회원가입이 안되어 있는 경우
            @Override
            public void onNotSignedUp() {
                Log.d("SessionCallback :: ", "onNotSignedUp");

            }

            //사용자 정보 요청 성공 : 사용자 정보를 리턴
            @Override
            public void onSuccess(final UserProfile userProfile) {

                final long id = userProfile.getId();
                Map map = new HashMap();
                map.put("providerId", id + "");

                final UserService userService = UserService.retrofit.create(UserService.class);
                Call<User> call = userService.kakaoSubscribeCheck(map);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        User u = response.body();

                        if (u.getId() == 0) {
                            Intent intent = new Intent(act, KJoinActivity.class);
                            intent.putExtra("providerId", id); /*송신*/
                            act.startActivity(intent);
                        } else {
                            User user = User.getInstance();

                            user.setId(u.getId());
                            user.setUsername(u.getUsername());
                            user.setName(u.getName());
                            user.setEmail(u.getEmail());
                            user.setLevel(u.getLevel());
                            user.setProvider(u.getProvider());
                            user.setProviderId(u.getProviderId());
                            user.setCreateDate(u.getCreateDate());

                            String setCookie = response.headers().get("Set-Cookie");
                            String[] cookie = setCookie.split(";");
                            user.setCookie(cookie[0]);

                            act.finish();

                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                    }
                });


            }

            //사용자 정보 요청 실패
            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.d("SessionCallback :: ", "onFailure : " + errorResult.getErrorMessage());
            }

        });

    }
}
