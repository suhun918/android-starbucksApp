package com.cos.mystarbucks;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cos.mystarbucks.Adapter.RvAdapterMenuCoffee;
import com.cos.mystarbucks.Adapter.RvAdapterMyMenu;

public class FragmentMyMenu extends Fragment {

    private MyPageActivity activity;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;


    public FragmentMyMenu(Activity activity) {
        this.activity = (MyPageActivity)activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_my_menu, container, false);

        if( activity.myPageDTO.getMyCoffeeList().size() > 0 || activity.myPageDTO.getMyBeverageList().size() > 0 ){
            v.findViewById(R.id.tv_mymenu).setVisibility(View.GONE);

            layoutManager = new LinearLayoutManager(activity);
            recyclerView = v.findViewById(R.id.mypage_mymenu);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);

            addItems();
        }

        return v;
    }

    private void addItems(){
        RvAdapterMyMenu Adapter = new RvAdapterMyMenu(activity);
        Adapter.addItems(activity.myPageDTO.getMyCoffeeList(), activity.myPageDTO.getMyBeverageList());
        recyclerView.setAdapter(Adapter);
    }

}
