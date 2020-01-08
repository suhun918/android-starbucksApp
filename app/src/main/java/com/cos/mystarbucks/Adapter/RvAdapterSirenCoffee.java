package com.cos.mystarbucks.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cos.mystarbucks.R;
import com.cos.mystarbucks.model.Siren;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RvAdapterSirenCoffee extends RecyclerView.Adapter<RvAdapterSirenCoffee.ViewHolder> {
    List<Siren.Coffee> coffees = new ArrayList<>();


    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCoffee;
        private ImageView ivCoffee;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            tvCoffee = itemView.findViewById(R.id.tv_siren);
            ivCoffee = itemView.findViewById(R.id.iv_siren);

        }
        public void setItem(Siren.Coffee coffees){
            tvCoffee.setText(coffees.getName());
            Picasso.get()
                   .load("http://192.168.0.50:8080"+coffees.getImage())
                   .into(ivCoffee);
        }
    }

    public void addItem(Siren.Coffee coffee){
        coffees.add(coffee);

    }

    public void addItems(List<Siren.Coffee> coffee){
        this.coffees = coffee;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public RvAdapterSirenCoffee.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View sirenItemView = inflater.inflate(R.layout.recyclerview_sirenitem, parent, false);
        return new ViewHolder(sirenItemView);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RvAdapterSirenCoffee.ViewHolder holder, int position) {
        Siren.Coffee coffee = coffees.get(position);
         holder.setItem(coffee);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {

        return coffees.size();

    }


}
