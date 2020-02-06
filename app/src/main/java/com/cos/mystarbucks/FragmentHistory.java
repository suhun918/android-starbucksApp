package com.cos.mystarbucks;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cos.mystarbucks.Adapter.RvAdapterHistory;
import com.cos.mystarbucks.Adapter.RvAdapterMyMenu;

public class FragmentHistory extends Fragment {

    private MyPageActivity activity;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;



    public FragmentHistory(Activity activity) {
        this.activity = (MyPageActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_history, container, false);

        if(activity.myPageDTO.getTradeList().size() > 0){
            v.findViewById(R.id.tv_mypage_history).setVisibility(View.GONE);

            layoutManager = new LinearLayoutManager(activity);
            recyclerView = v.findViewById(R.id.rv_mypage_history);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);

            addItems();
        }

        return v;
    }

    private void addItems(){
        RvAdapterHistory Adapter = new RvAdapterHistory(activity);
        Adapter.addItems(activity.myPageDTO.getTradeList());
        recyclerView.setAdapter(Adapter);
    }

}
