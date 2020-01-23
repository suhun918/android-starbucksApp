package com.cos.mystarbucks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList = new ArrayList<>();

    // FragmentPagerAdapter를 상속하면 해당 생성자가 꼭 필요하다.
    public FragmentAdapter(@NonNull FragmentManager fm, int behavior){
        super(fm, behavior);
    }

    public void addFragment(Fragment fragment){
        mFragmentList.add(fragment);
    }

    @NonNull
    @Override
    public Fragment getItem(int position){
        return mFragmentList.get(position);
    }

    @Override
    public int getCount(){
        return mFragmentList.size();
    }


}
