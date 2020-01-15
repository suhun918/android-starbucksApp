package com.cos.mystarbucks.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cos.mystarbucks.PurchaseActivity;
import com.cos.mystarbucks.R;
import com.cos.mystarbucks.model.Siren;
import com.cos.mystarbucks.util.Localhost;
import com.cos.mystarbucks.util.RoundedTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RvAdapterSirenFood extends RecyclerView.Adapter<RvAdapterSirenFood.ViewHolder> {
    List<Siren.Food> foods = new ArrayList<>();
    Activity actSiren;

    public RvAdapterSirenFood(Activity actSiren) {
        this.actSiren = actSiren;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCoffee;
        private ImageView ivCoffee;

        ViewHolder(View itemView) {
            super(itemView) ;
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        String name = foods.get(pos).getName();
                        int price = foods.get(pos).getPrice();
                        String img = Localhost.URL+foods.get(pos).getImage();
                        Intent what = new Intent(actSiren, PurchaseActivity.class);
                        what.putExtra("name",name); /*송신*/
                        what.putExtra("price",price);
                        what.putExtra("img",img);
                        actSiren.startActivity(what);
                    }
                }
            });

            // 뷰 객체에 대한 참조. (hold strong reference)
            tvCoffee = itemView.findViewById(R.id.tv_siren);
            ivCoffee = itemView.findViewById(R.id.iv_siren);

        }
        public void setItem(Siren.Food foods){
            RoundedTransform transform = new RoundedTransform(100,0);
            tvCoffee.setText(foods.getName());
            Picasso.get()
                   .load(Localhost.URL + foods.getImage())
                   .transform(transform)
                   .into(ivCoffee);
        }
    }

    public void addItem(Siren.Food food){
        foods.add(food);

    }

    public void addItems(List<Siren.Food> food){
        this.foods = food;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public RvAdapterSirenFood.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View sirenItemView = inflater.inflate(R.layout.recyclerview_sirenitem, parent, false);
        return new ViewHolder(sirenItemView);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RvAdapterSirenFood.ViewHolder holder, int position) {
        Siren.Food food = foods.get(position);
         holder.setItem(food);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {

        return foods.size();

    }


}
