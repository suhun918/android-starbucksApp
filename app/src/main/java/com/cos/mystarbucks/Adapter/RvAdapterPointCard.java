package com.cos.mystarbucks.Adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cos.mystarbucks.LoginActivity;
import com.cos.mystarbucks.R;
import com.cos.mystarbucks.model.PointCardDTO;
import com.cos.mystarbucks.model.User;
import com.cos.mystarbucks.service.CardService;
import com.cos.mystarbucks.util.Localhost;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RvAdapterPointCard extends RecyclerView.Adapter<RvAdapterPointCard.ViewHolder> {
    List<PointCardDTO.Card> cards = new ArrayList<>();
    Activity actCard;
    AlertDialog adNotlogin, adExistCard, adNotExistCard;
    User user = User.getInstance();


    public RvAdapterPointCard(Activity actCard) {
        this.actCard = actCard;
    }


    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCard;
        private ImageView ivCard;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //로그인 한 경우
                    if (user.getId() != 0) {

                        Map cardCheck = new HashMap();
                        cardCheck.put("userId", user.getId() + "");

                        final CardService cardService = CardService.retrofit.create(CardService.class);

                        Call<ResponseBody> existCard = cardService.existCard(cardCheck);

                        existCard.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> existCard, Response<ResponseBody> response) {

                                String res1 = null;
                                try {
                                    res1 = response.body().string();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (res1.equals("0")) {
                                    AlertDialog.Builder abNotExistCard = new AlertDialog.Builder(actCard);
                                    abNotExistCard
                                            .setMessage("선택하신 카드를 등록하시겠습니까?")
                                            .setCancelable(false)
                                            .setPositiveButton("아니요", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            })
                                            .setNegativeButton("예", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //카드를 등록하지 않은 경우
                                                    int pos = getAdapterPosition();
                                                    if (pos != RecyclerView.NO_POSITION) {
                                                        Map saveCard = new HashMap();
                                                        saveCard.put("userId", user.getId() + "");
                                                        saveCard.put("cardId", cards.get(pos).getId() + "");
                                                        saveCard.put("cardName", cards.get(pos).getName());
                                                        saveCard.put("cardImage", cards.get(pos).getImage());


                                                        Call<ResponseBody> enrollment = cardService.enrollment(saveCard);
                                                        enrollment.enqueue(new Callback<ResponseBody>() {
                                                            @Override
                                                            public void onResponse(Call<ResponseBody> enrollment, Response<ResponseBody> response) {
                                                                String res2 = null;
                                                                try {
                                                                    res2 = response.body().string();
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                    res2 = "enrollment fail";
                                                                }

                                                                if (res2.equals("enrollment success")) {
                                                                    Toast myToast1 = Toast.makeText(actCard, "등록성공", Toast.LENGTH_SHORT);
                                                                    myToast1.show();
                                                                } else {
                                                                    Toast myToast2 = Toast.makeText(actCard, "등록실패", Toast.LENGTH_SHORT);
                                                                    myToast2.show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<ResponseBody> enrollment, Throwable t) {
                                                                Toast myToast3 = Toast.makeText(actCard, "서버와 연결할 수 없습니다", Toast.LENGTH_SHORT);
                                                                myToast3.show();
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                    adNotExistCard = abNotExistCard.create();
                                    adNotExistCard.show();

                                } else {
                                    //이미 카드를 등록한 경우
                                    AlertDialog.Builder abExistCard = new AlertDialog.Builder(actCard);
                                    abExistCard
                                            .setMessage("이미 등록한 카드가 있습니다")
                                            .setCancelable(false)
                                            .setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int id) {

                                                }
                                            });
                                    adExistCard = abExistCard.create();
                                    adExistCard.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> existCard, Throwable t) {

                            }
                        });

                        //로그인 하지 않은 경우
                    } else {
                        AlertDialog.Builder abNotlogin = new AlertDialog.Builder(actCard);
                        abNotlogin
                                .setMessage("로그인이 필요한 서비스 입니다.")
                                .setCancelable(false) // alert 창 바깥을 터치하거나 뒤로가기 하면 alert 창 사라지는 옵션 (default : true)
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent login = new Intent(actCard, LoginActivity.class);
                                        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        login.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        actCard.startActivity(login);
                                    }
                                });
                        adNotlogin = abNotlogin.create();
                        adNotlogin.show();
                    }


                }
            });

            // 뷰 객체에 대한 참조. (hold strong reference)
            tvCard = itemView.findViewById(R.id.tv_card);
            ivCard = itemView.findViewById(R.id.iv_card);

        }


        public void setItem(PointCardDTO.Card cards) {
            tvCard.setText(cards.getName());
            Picasso.get()
                    .load(Localhost.URL + cards.getImage())
                    .into(ivCard);
        }
    }

    public void addItem(PointCardDTO.Card card) {

        cards.add(card);
    }

    public void addItems(List<PointCardDTO.Card> card) {

        this.cards = card;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public RvAdapterPointCard.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cardItemView = inflater.inflate(R.layout.recyclerview_carditem, parent, false);
        return new RvAdapterPointCard.ViewHolder(cardItemView);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RvAdapterPointCard.ViewHolder holder, int position) {
        PointCardDTO.Card card = cards.get(position);
        holder.setItem(card);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {

        return cards.size();

    }


}

