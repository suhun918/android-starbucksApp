package com.cos.mystarbucks;

import android.app.Activity;
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

import com.cos.mystarbucks.Adapter.RvAdapterMenuBeverage;
import com.cos.mystarbucks.Adapter.RvAdapterMenuCoffee;
import com.cos.mystarbucks.model.Menu;
import com.cos.mystarbucks.service.MenuService;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentBeverage extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Activity actMenu;

    // 내가 실행하는게 아님!!
    // fragment_first.xml 을 메모리에 로딩하고 Activity에 붙여서 return 하면 됨.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_beverage, container, false);
        Context context = v.getContext();
        layoutManager = new LinearLayoutManager(context);
        recyclerView = v.findViewById(R.id.menu_beverage);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        rvDataSetting();

        return v;
    }
    public FragmentBeverage(Activity actMenu) {
        this.actMenu = actMenu;
    }


    private void rvDataSetting(){
        final RvAdapterMenuBeverage Adapter = new RvAdapterMenuBeverage(actMenu);

        final MenuService menuService = MenuService.retrofit.create(MenuService.class);
        Call<Menu> call = menuService.repoContributors();
        call.enqueue(new Callback<Menu>() {
            @Override
            public void onResponse(Call<Menu> call,
                                   Response<Menu> response) {
                Menu menu = response.body();
                Adapter.addItems(menu.getBeverages());
                recyclerView.setAdapter(Adapter);
            }
            @Override
            public void onFailure(Call<Menu> call, Throwable t) {
            }
        });
    }


}
