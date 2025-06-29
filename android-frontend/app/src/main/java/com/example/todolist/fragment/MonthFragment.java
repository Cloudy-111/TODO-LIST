package com.example.todolist.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.todolist.databinding.FragmentMonthBinding;

public class MonthFragment extends Fragment {
    private FragmentMonthBinding binding;

    public MonthFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentMonthBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}