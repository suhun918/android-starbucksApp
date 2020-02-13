package com.cos.mystarbucks.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cos.mystarbucks.PurchaseActivity;
import com.cos.mystarbucks.R;
import com.cos.mystarbucks.model.MenuDTO;
import com.cos.mystarbucks.util.Localhost;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RvAdapterMenuFood extends RecyclerView.Adapter<RvAdapterMenuFood.ViewHolder>{
    private DecimalFormat formatter = new DecimalFormat("###,###");
    List<MenuDTO.Food> foods = new ArrayList<>();
    Activity actMenu;

    public RvAdapterMenuFood(Activity actMenu) {
        this.actMenu = actMenu;
    }


    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBeverageName, tvBeveragePrice;
        private ImageView ivBeverage;
        private LinearLayout loMenuItem;

        ViewHolder(View itemView) {
            super(itemView) ;
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        int id = foods.get(pos).getId();
                        String name = foods.get(pos).getName();
                        int price = foods.get(pos).getPrice();
                        String img = Localhost.URL+foods.get(pos).getImage();
                        Intent what = new Intent(actMenu, PurchaseActivity.class);
                        what.putExtra("food",id); /*송신*/
                        what.putExtra("name",name);
                        what.putExtra("price",price);
                        what.putExtra("img",img);
                        actMenu.startActivity(what);
                    }
                }
            });

            // 뷰 객체에 대한 참조. (hold strong reference)
            tvBeverageName = itemView.findViewById(R.id.tv_menu_name);
            tvBeveragePrice = itemView.findViewById(R.id.tv_menu_price);
            ivBeverage = itemView.findViewById(R.id.iv_menu);
            loMenuItem = itemView.findViewById(R.id.lo_menuItem);

        }
        public void setItem(MenuDTO.Food foods){
            tvBeverageName.setText(foods.getName());
            tvBeveragePrice.setText(formatter.format(foods.getPrice())+" 원");
            Picasso.get()
                    .load(Localhost.URL + foods.getImage())
                    .into(ivBeverage);
        }
    }

    public void addItem(MenuDTO.Food food){

        foods.add(food);
    }

    public void addItems(List<MenuDTO.Food> food){

        this.foods = food;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public RvAdapterMenuFood.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View MenuItemView = inflater.inflate(R.layout.recyclerview_menuitem, parent, false);
        return new ViewHolder(MenuItemView);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RvAdapterMenuFood.ViewHolder holder, int position) {
        MenuDTO.Food food = foods.get(position);
        if(position % 2 == 0){
            holder.loMenuItem.setBackgroundColor(0xFFF4F4F2);
        }else{
            holder.loMenuItem.setBackgroundColor(0xFFFFFFFF);
        }
        holder.setItem(food);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {

        return foods.size();

    }
}
