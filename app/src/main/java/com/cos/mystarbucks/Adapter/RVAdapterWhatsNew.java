package com.cos.mystarbucks.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cos.mystarbucks.R;
import com.cos.mystarbucks.WNDetailActivity;
import com.cos.mystarbucks.model.BoardDTO;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class RVAdapterWhatsNew extends RecyclerView.Adapter<RVAdapterWhatsNew.ViewHolder> {
    List<BoardDTO.Board> boards = new ArrayList<>();
    SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd\nHH : mm");
    Activity actWhat;

    public RVAdapterWhatsNew(Activity actWhat) {
        this.actWhat = actWhat;
    }


    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBoardId, tvBoardTitle, tvBoardCreateDate;

        ViewHolder(View itemView) {
            super(itemView) ;
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        int id = boards.get(pos).getId();
                        Intent what = new Intent(actWhat, WNDetailActivity.class);
                        what.putExtra("id",id); /*송신*/
                        actWhat.startActivity(what);
                    }
                }
            });

            // 뷰 객체에 대한 참조. (hold strong reference)
            tvBoardId = itemView.findViewById(R.id.tv_what_id);
            tvBoardTitle = itemView.findViewById(R.id.tv_what_title);
            tvBoardCreateDate = itemView.findViewById(R.id.tv_what_createdate);

        }
        public void setItem(BoardDTO.Board boards){

            tvBoardId.setText(boards.getId()+"");
            tvBoardTitle.setText(boards.getTitle());
            tvBoardCreateDate.setText(sFormat.format(boards.getCreateDate()));
        }
    }

    public void addItem(BoardDTO.Board board){

        boards.add(board);
    }

    public void addItems(List<BoardDTO.Board> board){

        this.boards = board;
    }


    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public RVAdapterWhatsNew.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View WhatsNewItemView = inflater.inflate(R.layout.recyclerview_whatsnewitem, parent, false);
        return new RVAdapterWhatsNew.ViewHolder(WhatsNewItemView);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RVAdapterWhatsNew.ViewHolder holder, int position) {
        BoardDTO.Board board = boards.get(position);
        holder.setItem(board);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {

        return boards.size();

    }
}



