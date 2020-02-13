package com.cos.mystarbucks.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cos.mystarbucks.MyPageActivity;
import com.cos.mystarbucks.PurchaseActivity;
import com.cos.mystarbucks.R;
import com.cos.mystarbucks.model.MyPageDTO;
import com.cos.mystarbucks.model.User;
import com.cos.mystarbucks.service.MyPageService;
import com.cos.mystarbucks.util.Localhost;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RvAdapterMyMenu extends RecyclerView.Adapter<RvAdapterMyMenu.ViewHolder>{
    private DecimalFormat formatter = new DecimalFormat("###,###");
    private List<MyPageDTO.MyMenu> myMenuList = new ArrayList<>();
    private int myCoffeeEA;
    private User user = User.getInstance();
    private MyPageActivity activity;

    public RvAdapterMyMenu(Activity activity) {
        this.activity = (MyPageActivity)activity;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvMyMenuName, tvMyMenuPrice;
        private ImageView ivMyMenu;
        private LinearLayout loMenuItem;
        private Button btnDeleteMyMenu;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    activity.fragmentId = 1;
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        String name = myMenuList.get(pos).getName();
                        int price = myMenuList.get(pos).getPrice();
                        String img = Localhost.URL+myMenuList.get(pos).getImage();
                        Intent intent = new Intent(activity, PurchaseActivity.class);
                        intent.putExtra("name",name); /*송신*/
                        intent.putExtra("price",price);
                        intent.putExtra("img",img);
                        activity.startActivity(intent);
                    }
                }
            });

            // 뷰 객체에 대한 참조. (hold strong reference)
            tvMyMenuName = itemView.findViewById(R.id.tv_menu_name);
            tvMyMenuPrice = itemView.findViewById(R.id.tv_menu_price);
            ivMyMenu = itemView.findViewById(R.id.iv_menu);
            loMenuItem = itemView.findViewById(R.id.lo_menuItem);
            btnDeleteMyMenu = itemView.findViewById(R.id.btn_delete_mymenu);
        }

        public void setItem(MyPageDTO.MyMenu myMenu){
            btnDeleteMyMenu.setOnClickListener(this);
            tvMyMenuName.setText(myMenu.getName());
            tvMyMenuPrice.setText(formatter.format(myMenu.getPrice())+" 원");
            Picasso.get()
                    .load(Localhost.URL + myMenu.getImage())
                    .into(ivMyMenu);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if(pos != RecyclerView.NO_POSITION){
                // 삭제 버튼 클릭
                if(v.getId() == R.id.btn_delete_mymenu){
                    if(pos < myCoffeeEA){ // 선택한 메뉴가 coffee 메뉴임
                        myCoffeeEA -= 1;

                        final MyPageService myPageService = MyPageService.retrofit.create(MyPageService.class);
                        Call<ResponseBody> call = myPageService.deleteCoffee(user.getCookie(), myMenuList.get(pos).getId());
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });

                    }else{ // 선택한 메뉴가 beverage 메뉴임
                        final MyPageService myPageService = MyPageService.retrofit.create(MyPageService.class);
                        Call<ResponseBody> call = myPageService.deleteBeverage(user.getCookie(), myMenuList.get(pos).getId());
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }

                    myMenuList.remove(pos);
//                    notifyDataSetChanged();
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, myMenuList.size());
                }
            }
        }
    }

    public void addItems(List<MyPageDTO.MyMenu> myCoffeeList, List<MyPageDTO.MyMenu> myBeverageList){
        myMenuList.addAll(myCoffeeList);
        myMenuList.addAll(myBeverageList);

        myCoffeeEA = myCoffeeList.size();
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public RvAdapterMyMenu.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View MenuItemView = inflater.inflate(R.layout.recyclerview_mymenuitem, parent, false);
        return new ViewHolder(MenuItemView);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RvAdapterMyMenu.ViewHolder holder, int position) {
        MyPageDTO.MyMenu myMenu = myMenuList.get(position);
        if(position % 2 == 0){
            holder.loMenuItem.setBackgroundColor(0xFFF4F4F2);
        }else{
            holder.loMenuItem.setBackgroundColor(0xFFFFFFFF);
        }
        holder.setItem(myMenu);
    }

    public int getItemCount() {
        return myMenuList.size();
    }

}
