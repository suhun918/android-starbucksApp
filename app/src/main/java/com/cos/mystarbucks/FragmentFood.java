package com.cos.mystarbucks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class FragmentFood extends Fragment {

    // 내가 실행하는게 아님!!
    // fragment_first.xml 을 메모리에 로딩하고 Activity에 붙여서 return 하면 됨.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_food, container, false);
        ArrayList<String> list = new ArrayList<>();
        for (int i=0; i<100; i++) {
            list.add(String.format("TEXT %d", i)) ;
        }

        Context context = v.getContext();

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView menuFood = v.findViewById(R.id.menu_food) ;
        menuFood.setLayoutManager(new LinearLayoutManager(context)) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        RecyclerAdapterMenu menuAdapterFood = new RecyclerAdapterMenu(list) ;
        menuFood.setAdapter(menuAdapterFood) ;
        return v;
    }

}
