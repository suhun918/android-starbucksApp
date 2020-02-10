package com.cos.mystarbucks.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cos.mystarbucks.MyPageActivity;
import com.cos.mystarbucks.R;
import com.cos.mystarbucks.model.MyPageDTO;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class RvAdapterHistory extends RecyclerView.Adapter<RvAdapterHistory.ViewHolder> {
    private List<MyPageDTO.Trade> tradeList = new ArrayList<>();
    private SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd\nHH : mm");
    private DecimalFormat formatter = new DecimalFormat("###,###");
    private MyPageActivity activity;

    public RvAdapterHistory(Activity activity) {
        this.activity = (MyPageActivity) activity;
    }


    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvPrice, tvAmount, tvCreateDate;
        private LinearLayout loHistoryItem;

        ViewHolder(View itemView) {
            super(itemView) ;
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){

                    }
                }
            });

            // 뷰 객체에 대한 참조. (hold strong reference)
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvCreateDate = itemView.findViewById(R.id.tv_createdate);
            loHistoryItem = itemView.findViewById(R.id.lo_historyItem);
        }

        public void setItem(MyPageDTO.Trade trade){
            tvName.setText(trade.getName());
            tvPrice.setText(formatter.format(trade.getPrice())+" 원");
            tvAmount.setText(Integer.toString(trade.getAmount()));
            tvCreateDate.setText(sFormat.format(trade.getCreateDate()));
        }
    }

    public void addItems(List<MyPageDTO.Trade> tradeList){
        this.tradeList = tradeList;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public RvAdapterHistory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recyclerview_historyitem, parent, false);
        return new RvAdapterHistory.ViewHolder(view);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RvAdapterHistory.ViewHolder holder, int position) {
        MyPageDTO.Trade trade = tradeList.get(position);
        if(position%2 == 1){
            holder.loHistoryItem.setBackgroundColor(0xFFFFFFFF);
        }
        holder.setItem(trade);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return tradeList.size();
    }
}



