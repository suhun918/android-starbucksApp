package com.cos.mystarbucks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cos.mystarbucks.model.User;
import com.cos.mystarbucks.service.MyPageService;
import com.cos.mystarbucks.util.CustomDialogRecharge;
import com.cos.mystarbucks.util.Localhost;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCardRecharge extends Fragment {

    private MyPageActivity activity;

    private ImageView ivMyCard;
    private TextView tvCardName;
    private TextView tvPoint;
    private Button btnRecharge;
    private Button btnDeleteCard;

    private AlertDialog.Builder alertBuilder;

    private CustomDialogRecharge customDialog;

    public FragmentCardRecharge(Activity activity) {
        this.activity = (MyPageActivity)activity;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_card_recharge, container, false);

        ivMyCard = v.findViewById(R.id.iv_myCard);
        tvCardName = v.findViewById(R.id.tv_cardName);
        tvPoint = v.findViewById(R.id.tv_point);
        btnRecharge = v.findViewById(R.id.btn_recharge);
        btnDeleteCard = v.findViewById(R.id.btn_cardDelete);

        alertBuilder = new AlertDialog.Builder(activity);
        minit();

        return v;
    }

    private void minit(){
        if(activity.myPageDTO.getMyCard() != null) { // 카드가 등록되어 있는 경우
            Picasso.get()
                    .load(Localhost.URL + activity.myPageDTO.getMyCard().getCardImage())
                    .into(ivMyCard);

            tvCardName.setText(activity.myPageDTO.getMyCard().getCardName());

            DecimalFormat formatter = new DecimalFormat("###,###");
            tvPoint.setText( formatter.format(activity.myPageDTO.getMyCard().getPoint()) + " 원" );

            setClickListener();

        }else{ // 카드가 없는 경우
            ivMyCard.setImageResource(R.drawable.regi_card);
            ivMyCard.setScaleType(ImageView.ScaleType.CENTER);
            ivMyCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, CardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            });
            tvCardName.setText("카드를 등록해 주세요.");
        }
    }

    private void setClickListener(){
        // 충전하기 버튼
        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog = new CustomDialogRecharge(activity);
                customDialog.show();
            }
        });

        // 카드삭제 버튼
        btnDeleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertBuilder
                        .setMessage("남아있는 포인트는 모두 소멸됩니다.\n정말 삭제하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                User user = User.getInstance();

                                MyPageService myPageService = MyPageService.retrofit.create(MyPageService.class);
                                Call<ResponseBody> call = myPageService.deleteCard( user.getCookie(), activity.myPageDTO.getMyCard().getId() );
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        activity.finish();
                                        activity.startActivity(new Intent(activity, MyPageActivity.class));
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });

                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
        });
    }

}
