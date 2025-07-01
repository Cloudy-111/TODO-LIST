package com.example.todolist.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.viewHolder.CalendarViewHolder;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>{
    private final ArrayList<LocalDate> daysOfMonth;
    private final OnItemListener onItemListener;
    private final LocalDate selectedDate;
    public CalendarAdapter(ArrayList<LocalDate> daysOfMonth, LocalDate selectedDate, OnItemListener onItemListener){
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
        this.selectedDate = selectedDate;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup container, int viewType){
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, container, false);
//        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
//        layoutParams.height = (int) (container.getHeight() * 0.166666666);
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        LocalDate date = daysOfMonth.get(position);
        int dayNumber = date.getDayOfMonth();
        holder.daysOfMonth.setText(String.valueOf(dayNumber));

        LocalDate today = LocalDate.now();
        if (!date.isAfter(today)) {
            holder.daysOfMonth.setTextColor(Color.BLACK);
        } else {
            holder.daysOfMonth.setTextColor(Color.GRAY);
        }
        if (date.equals(selectedDate)) {
            // highlight màu nền khác
//            holder.itemView.setBackgroundResource(R.drawable.bg_selected_date);
        } else {
//            holder.itemView.setBackgroundResource(0);
        }
    }

    @Override
    public int getItemCount()
    {
        return daysOfMonth.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, String dayText);
    }
}
