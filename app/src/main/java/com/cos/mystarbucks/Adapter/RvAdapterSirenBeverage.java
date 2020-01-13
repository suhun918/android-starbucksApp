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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RvAdapterSirenBeverage extends RecyclerView.Adapter<RvAdapterSirenBeverage.ViewHolder> {
    List<Siren.Beverage> beverages = new ArrayList<>();
    Activity actBeverage;

    public RvAdapterSirenBeverage(Activity actBeverage) {
        this.actBeverage = actBeverage;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBeverage;
        private ImageView ivBeverage;

        ViewHolder(View itemView) {
            super(itemView) ;
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        String name = beverages.get(pos).getName();
                        int price = beverages.get(pos).getPrice();
                        String img = Localhost.URL+beverages.get(pos).getImage();
                        Intent what = new Intent(actBeverage, PurchaseActivity.class);
                        what.putExtra("name",name); /*송신*/
                        what.putExtra("price",price);
                        what.putExtra("img",img);
                        actBeverage.startActivity(what);
                    }
                }
            });

            // 뷰 객체에 대한 참조. (hold strong reference)
            tvBeverage = itemView.findViewById(R.id.tv_siren);
            ivBeverage = itemView.findViewById(R.id.iv_siren);

        }
        public void setItem(Siren.Beverage beverages){

            tvBeverage.setText(beverages.getName());
            Picasso.get()
                    .load(Localhost.URL + beverages.getImage())
                    .into(ivBeverage);
        }
    }

    public void addItem(Siren.Beverage beverage){

        beverages.add(beverage);
    }

    public void addItems(List<Siren.Beverage> beverage){

        this.beverages = beverage;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public RvAdapterSirenBeverage.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View sirenItemView = inflater.inflate(R.layout.recyclerview_sirenitem, parent, false);
        return new ViewHolder(sirenItemView);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RvAdapterSirenBeverage.ViewHolder holder, int position) {
        Siren.Beverage beverage = beverages.get(position);
        holder.setItem(beverage);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {

        return beverages.size();

    }


}
