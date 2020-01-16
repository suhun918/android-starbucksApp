package com.cos.mystarbucks.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cos.mystarbucks.R;
import com.cos.mystarbucks.model.PointCardDTO;
import com.cos.mystarbucks.util.Localhost;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RvAdapterPointCard extends RecyclerView.Adapter<RvAdapterPointCard.ViewHolder>{
    List<PointCardDTO.Card> cards = new ArrayList<>();


    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCard;
        private ImageView ivCard;

        ViewHolder(View itemView) {
            super(itemView) ;
            // 뷰 객체에 대한 참조. (hold strong reference)
            tvCard = itemView.findViewById(R.id.tv_card);
            ivCard = itemView.findViewById(R.id.iv_card);

        }
        public void setItem(PointCardDTO.Card cards){
            tvCard.setText(cards.getName());
            Picasso.get()
                    .load(Localhost.URL + cards.getImage())
                    .into(ivCard);
        }
    }

    public void addItem(PointCardDTO.Card card){

        cards.add(card);
    }

    public void addItems(List<PointCardDTO.Card> card){

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

