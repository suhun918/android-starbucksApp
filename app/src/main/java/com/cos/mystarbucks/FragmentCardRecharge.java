package com.cos.mystarbucks;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cos.mystarbucks.util.Localhost;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class FragmentCardRecharge extends Fragment {

    private MyPageActivity activity;

    // 내가 실행하는게 아님!!
    // fragment_first.xml 을 메모리에 로딩하고 Activity에 붙여서 return 하면 됨.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_card_recharge, container, false);

        ImageView ivMyCard = v.findViewById(R.id.iv_myCard);
        Picasso.get()
                .load(Localhost.URL + activity.myPageDTO.getCardImage())
                .into(ivMyCard);

        TextView tvCardName = v.findViewById(R.id.tv_cardName);
        tvCardName.setText(activity.myPageDTO.getCardName());

        DecimalFormat formatter = new DecimalFormat("###,###");
        TextView tvPoint = v.findViewById(R.id.tv_point);
        tvPoint.setText( formatter.format(activity.myPageDTO.getPoint()) + " 원" );

        return v;
    }

    public FragmentCardRecharge(Activity activity) {
        this.activity = (MyPageActivity)activity;
    }

}
