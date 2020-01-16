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

import java.util.ArrayList;
import java.util.List;

public class RvAdapterMenuBeverage extends RecyclerView.Adapter<RvAdapterMenuBeverage.ViewHolder>{

    List<MenuDTO.Beverage> beverages = new ArrayList<>();
    Activity actMenu;

    public RvAdapterMenuBeverage(Activity actMenu) {
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
                        String name = beverages.get(pos).getName();
                        int price = beverages.get(pos).getPrice();
                        String img = Localhost.URL+beverages.get(pos).getImage();
                        Intent what = new Intent(actMenu, PurchaseActivity.class);
                        what.putExtra("name",name); /*송신*/
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
        public void setItem(MenuDTO.Beverage beverages){
            tvBeverageName.setText(beverages.getName());
            tvBeveragePrice.setText(beverages.getPrice()+" 원");
            Picasso.get()
                    .load(Localhost.URL + beverages.getImage())
                    .into(ivBeverage);
        }
    }

    public void addItem(MenuDTO.Beverage beverage){

        beverages.add(beverage);
    }

    public void addItems(List<MenuDTO.Beverage> beverage){

        this.beverages = beverage;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public RvAdapterMenuBeverage.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View MenuItemView = inflater.inflate(R.layout.recyclerview_menuitem, parent, false);
        return new ViewHolder(MenuItemView);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RvAdapterMenuBeverage.ViewHolder holder, int position) {
        MenuDTO.Beverage beverage = beverages.get(position);
        if(position % 2 == 0){
            holder.loMenuItem.setBackgroundColor(0xFFF4F4F2);
        }else{
            holder.loMenuItem.setBackgroundColor(0xFFFFFFFF);
        }
        holder.setItem(beverage);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {

        return beverages.size();

    }
}
