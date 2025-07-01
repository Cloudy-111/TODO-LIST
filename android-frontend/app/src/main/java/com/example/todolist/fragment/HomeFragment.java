package com.example.todolist.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.todolist.R;
import com.example.todolist.Utils.DateTimeUtils;
import com.example.todolist.adapter.TabSwitchAdapter;
import com.example.todolist.databinding.FragmentHomeBinding;

import java.lang.reflect.Type;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ViewPager2 viewPager;
    public TextView tabToday, tabMonth;
    private SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        preferences = getActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", 0);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabToday = binding.tabToday;
        tabMonth = binding.tabMonth;
        viewPager = binding.viewPager;

        viewPager.setAdapter(new TabSwitchAdapter(this));
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateTableUI(position);
            }
        });

        tabToday.setOnClickListener(v -> {
            Log.d("TabToday", "CLICKED");
            viewPager.setCurrentItem(0);
        });

        tabMonth.setOnClickListener(v -> {
            Log.d("TabMonth", "CLICKED");
            viewPager.setCurrentItem(1);
        });
    }


    private void updateTableUI(int position){
        if(position == 0){
            tabToday.setBackgroundResource(R.drawable.tab_selected_bg);
            tabToday.setTypeface(null, Typeface.BOLD);

            tabMonth.setBackgroundResource(R.drawable.tab_unselected_bg);
            tabMonth.setTypeface(null, Typeface.NORMAL);
        } else {
            tabToday.setBackgroundResource(R.drawable.tab_unselected_bg);
            tabToday.setTypeface(null, Typeface.NORMAL);

            tabMonth.setBackgroundResource(R.drawable.tab_selected_bg);
            tabMonth.setTypeface(null, Typeface.BOLD);
        }
    }

}