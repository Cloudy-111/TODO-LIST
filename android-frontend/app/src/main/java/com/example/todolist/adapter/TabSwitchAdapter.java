package com.example.todolist.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.todolist.Utils.DateTimeUtils;
import com.example.todolist.fragment.MonthFragment;
import com.example.todolist.fragment.TodayFragment;

public class TabSwitchAdapter extends FragmentStateAdapter {
    public TabSwitchAdapter(@NonNull Fragment fa){
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position){
        if(position == 0) return new TodayFragment();
        else return new MonthFragment();
    }

    @Override
    public int getItemCount(){
        return 2;
    }
}