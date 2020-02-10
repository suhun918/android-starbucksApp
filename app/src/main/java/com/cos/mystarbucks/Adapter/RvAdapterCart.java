package com.cos.mystarbucks.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cos.mystarbucks.CardActivity;
import com.cos.mystarbucks.MainActivity;
import com.cos.mystarbucks.MyPageActivity;
import com.cos.mystarbucks.PurchaseDoneActivity;
import com.cos.mystarbucks.R;
import com.cos.mystarbucks.model.CartDTO;
import com.cos.mystarbucks.model.User;
import com.cos.mystarbucks.service.SirenService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RvAdapterCart extends RecyclerView.Adapter<RvAdapterCart.ViewHolder> {
    private DecimalFormat formatter = new DecimalFormat("###,###");
    private TextView tvAllCount, tvAllPrice, tvNoCart, tvNotLoginCart;
    private Button btnCartPurchase;
    private Activity act;
    private int allCount =0, allPrice=0;
    User user = User.getInstance();

    private AlertDialog adNotExistCard, adNotPoint, adSuccess, adQuestion;

    private List<CartDTO.Cart> carts = new ArrayList<>();

    public RvAdapterCart(Activity act) {
        this.act = act;

    }



    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvCartName, tvCartPrice;
        private Button btnIncrease, btnDecrease, btnDelete;
        private TextView tvCount;
        private int count, price;

        ViewHolder(final View itemView) {
            super(itemView) ;


            // 뷰 객체에 대한 참조. (hold strong reference)
            btnIncrease = itemView.findViewById(R.id.btn_cart_plus);
            btnDecrease = itemView.findViewById(R.id.btn_cart_minus);
            btnDelete = itemView.findViewById(R.id.btn_cart_delete);
            tvCount = itemView.findViewById(R.id.tv_cart_count);

            tvCartName = itemView.findViewById(R.id.tv_cart_name);
            tvCartPrice = itemView.findViewById(R.id.tv_cart_price);

        }
        public void setItem(CartDTO.Cart carts){
            tvCartName.setText(carts.getName());

            tvCartPrice.setText(formatter.format(carts.getPrice()*(carts.getTempCount()+1))+" 원");
            tvCount.setText(carts.getTempCount()+1+"");

            itemView.setOnClickListener(this);
            btnIncrease.setOnClickListener(this);
            btnDecrease.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            int pos = getAdapterPosition();
            if(pos != RecyclerView.NO_POSITION){
                price = carts.get(pos).getPrice();
                switch (v.getId()) {
                    case R.id.btn_cart_plus:
                        if(carts.get(pos).getTempCount() < 19){
                            count = carts.get(pos).getTempCount();
                            count = count + 1;
                            tvCount.setText(count+1+"");

                            tvCartPrice.setText(formatter.format((count+1)*price)+" 원");
                            carts.get(pos).setTempCount(count);
                            //총 개수
                            String a = tvAllCount.getText().toString();
                            String[] aa = a.split(" ");
                            String aaa = aa[1];
                            allCount = Integer.parseInt(aaa);
                            tvAllCount.setText("총 "+(allCount+1)+" 개");
                            //총 가격
                            String b = tvAllPrice.getText().toString();
                            String[] bb = b.split(" ");
                            String bbb = bb[0];
                            String[] cc = bbb.split(",");
                            String ccc = cc[0];
                            String cccc = cc[1];
                            String d = ccc+cccc;
                            allPrice = Integer.parseInt(d);
                            tvAllPrice.setText(formatter.format(allPrice+carts.get(pos).getPrice())+" 원");
                        }
                        break;
                    case R.id.btn_cart_minus:
                        if(carts.get(pos).getTempCount() > 0){
                            count = carts.get(pos).getTempCount();
                            count = count - 1;
                            tvCount.setText(count +1+ "");

                            tvCartPrice.setText(formatter.format((count+1)*price)+" 원");
                            carts.get(pos).setTempCount(count);
                            //총 개수
                            String a = tvAllCount.getText().toString();
                            String[] aa = a.split(" ");
                            String aaa = aa[1];
                            allCount = Integer.parseInt(aaa);
                            tvAllCount.setText("총 "+(allCount-1)+" 개");
                            //총 가격
                            String b = tvAllPrice.getText().toString();
                            String[] bb = b.split(" ");
                            String bbb = bb[0];
                            String[] cc = bbb.split(",");
                            String ccc = cc[0];
                            String cccc = cc[1];
                            String d = ccc+cccc;

                            allPrice = Integer.parseInt(d);
                            tvAllPrice.setText(formatter.format(allPrice-carts.get(pos).getPrice())+" 원");
                        }
                        break;
                    case R.id.btn_cart_delete:
                        Map map = new HashMap();
                        map.put("id", carts.get(pos).getId()+"");

                        final SirenService sirenService = SirenService.retrofit.create(SirenService.class);
                        Call<ResponseBody> call = sirenService.deleteCart(map);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                String res = null;
                                try {
                                    res = response.body().string();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (res.equals("deleteSuccess")) {
                                    int pos = getAdapterPosition();
                                    //총 개수
                                    String a = tvAllCount.getText().toString();
                                    String[] aa = a.split(" ");
                                    String aaa = aa[1];
                                    allCount = Integer.parseInt(aaa);
                                    tvAllCount.setText("총 "+(allCount-(carts.get(pos).getTempCount()+1))+" 개");

                                    //총 가격
                                    String b = tvAllPrice.getText().toString();
                                    String[] bb = b.split(" ");
                                    String bbb = bb[0];
                                    String[] ff = bbb.split(",");
                                    String fff = ff[0];
                                    String ffff = ff[1];
                                    String g = fff+ffff;
                                    allPrice = Integer.parseInt(g);

                                    String c = tvCartPrice.getText().toString();
                                    String[] cc = c.split(" ");
                                    String ccc = cc[0];
                                    String[] dd = ccc.split(",");
                                    String ddd = dd[0];
                                    String dddd = dd[1];
                                    String e = ddd+dddd;

                                    int deletePrice = Integer.parseInt(e);
                                    tvAllPrice.setText(formatter.format(allPrice-deletePrice)+" 원");

                                    //해당 아이템 제거
                                    carts.remove(pos);
                                    notifyDataSetChanged();

                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                        break;
                }
            }
        }

    }

    public void addItem(CartDTO.Cart cart){
        carts.add(cart);

    }

    public void addItems(List<CartDTO.Cart> cart, final TextView tvAllCount, TextView tvAllPrice, Button btnCartPurchase, TextView tvNoCart, TextView tvNotLoginCart){

        this.carts = cart;
        this.tvAllCount = tvAllCount;
        this.tvAllPrice = tvAllPrice;
        this.btnCartPurchase = btnCartPurchase;
        this.tvNoCart = tvNoCart;
        this.tvNotLoginCart = tvNotLoginCart;
        //장바구니 진입 시 로그인 유무 및 물품 있어야 표시하게 해주는 것
        if(user.getId()!=0){
            tvNotLoginCart.setVisibility(View.GONE);
            if(carts.size()>0){
                tvNoCart.setVisibility(View.GONE);
            }
        }else{
            if(carts.size()>0){
                tvNoCart.setVisibility(View.GONE);
            }
        }

        //장바구니 진입 시 전체 개수 세팅
        tvAllCount.setText("총 "+carts.size()+" 개");
        //장바구니 진입 시 전체 가격 세팅
        for(int i = 0; i < carts.size(); i++) {
            allPrice = allPrice+carts.get(i).getPrice();
            tvAllPrice.setText(formatter.format(allPrice)+" 원");
        }
        //장바구니에서 구입버튼 클릭 시
        btnCartPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(carts.size()>0){
                    AlertDialog.Builder abQuestion = new AlertDialog.Builder(act);
                    abQuestion
                            .setMessage("사이렌 오더 이용 시 주문을 완료한 후에는\n일체의 주문 변경 또는 취소가 불가합니다.")
                            .setCancelable(false)
                            .setPositiveButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setNegativeButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    JsonObject jsonObject = new JsonObject();
                                    JsonArray trades = new JsonArray();
                                    for (int i =0; i<carts.size(); i++){
                                        JsonObject trade = new JsonObject();
                                        trade.addProperty("id",carts.get(i).getId());
                                        trade.addProperty("userId",user.getId());
                                        trade.addProperty("name",carts.get(i).getName());
                                        trade.addProperty("price",carts.get(i).getPrice());
                                        trade.addProperty("amount",carts.get(i).getTempCount()+1);
                                        trades.add(trade);
                                    }
                                    jsonObject.add("trade", trades);


                                    final SirenService sirenService = SirenService.retrofit.create(SirenService.class);
                                    Call<ResponseBody> call = sirenService.orderCart(jsonObject);
                                    call.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            String res = null;
                                            try {
                                                res = response.body().string();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            final String restPoint = res;

                                            if (res.equals("noCard")) {
                                                //카드가 없는 경우
                                                AlertDialog.Builder abNotExistCard = new AlertDialog.Builder(act);
                                                abNotExistCard
                                                        .setMessage("카드를 먼저 등록해주시기 바랍니다.")
                                                        .setCancelable(false)
                                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                            }
                                                        })
                                                        .setNegativeButton("등록", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Intent intent = new Intent(act.getApplicationContext(), CardActivity.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                                act.startActivity(intent);
                                                            }
                                                        });
                                                adNotExistCard = abNotExistCard.create();
                                                adNotExistCard.show();
                                            } else if (res.equals("noPoint")) {
                                                //포인트가 모자란 경우
                                                AlertDialog.Builder abNotPoint = new AlertDialog.Builder(act);
                                                abNotPoint
                                                        .setMessage("카드 잔액이 부족합니다.")
                                                        .setCancelable(false)
                                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                            }
                                                        })
                                                        .setNegativeButton("충전", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Intent intent = new Intent(act.getApplicationContext(), MyPageActivity.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                                act.startActivity(intent);
                                                            }
                                                        });
                                                adNotPoint = abNotPoint.create();
                                                adNotPoint.show();
                                            } else {
                                                //구매성공
                                                AlertDialog.Builder abSuccess = new AlertDialog.Builder(act);
                                                abSuccess
                                                        .setMessage("구매완료.\n가까운 매장에서 수령해주시기 바랍니다.")
                                                        .setCancelable(false)
                                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Intent intent = new Intent(act.getApplicationContext(), PurchaseDoneActivity.class);
                                                                intent.putExtra("restPoint", Integer.parseInt(restPoint));
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                                act.startActivity(intent);
                                                            }
                                                        });
                                                adSuccess = abSuccess.create();
                                                adSuccess.show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                                        }
                                    });

                                }
                            });
                    adQuestion = abQuestion.create();
                    adQuestion.show();
                }else{
                    Toast toast = Toast.makeText(act.getApplicationContext(), "등록된 상품이 없습니다.", Toast.LENGTH_LONG); toast.show();
                }



            }
        });
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public RvAdapterCart.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cartItemView = inflater.inflate(R.layout.recylerview_cartitem, parent, false);
        return new ViewHolder(cartItemView);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RvAdapterCart.ViewHolder holder, int position) {
        CartDTO.Cart cart = carts.get(position);

        holder.setItem(cart);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {

        return carts.size();

    }

}
