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

import com.cos.mystarbucks.Adapter.RvAdapterMenuCoffee;
import com.cos.mystarbucks.model.Menu;
import com.cos.mystarbucks.service.MenuService;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCoffee extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    // 내가 실행하는게 아님!!
    // fragment_first.xml 을 메모리에 로딩하고 Activity에 붙여서 return 하면 됨.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_coffee, container, false);
        Context context = v.getContext();
        layoutManager = new LinearLayoutManager(context);
        recyclerView = v.findViewById(R.id.menu_coffee);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        rvDataSetting();

        return v;
    }


    private void rvDataSetting(){
        final RvAdapterMenuCoffee Adapter = new RvAdapterMenuCoffee();

        final MenuService menuService = MenuService.retrofit.create(MenuService.class);
        Call<Menu> call = menuService.repoContributors();
        call.enqueue(new Callback<Menu>() {
            @Override
            public void onResponse(Call<Menu> call,
                                   Response<Menu> response) {
                Menu menu = response.body();
                Adapter.addItems(menu.getCoffees());
                recyclerView.setAdapter(Adapter);
            }
            @Override
            public void onFailure(Call<Menu> call, Throwable t) {
            }
        });
    }

}
