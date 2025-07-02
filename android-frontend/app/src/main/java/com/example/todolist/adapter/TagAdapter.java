package com.example.todolist.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.model.Tag;
import com.example.todolist.viewHolder.TagViewHolder;

import java.util.ArrayList;
import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagViewHolder> {
    private List<Tag> tagList;
    private OnTagClickListener listener;

    public interface OnTagClickListener {
        void onTagClick(Tag tag);
    }

    public TagAdapter(List<Tag> tagList, OnTagClickListener listener) {
        this.tagList = tagList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        Tag tag = tagList.get(position);
        holder.tagName.setText(tag.getName());

        try {
            int color = Color.parseColor(tag.getColor());
            holder.tagColorView.setBackgroundTintList(ColorStateList.valueOf(color));
        } catch (Exception e) {
            holder.tagColorView.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        holder.itemView.setOnClickListener(v -> listener.onTagClick(tag));
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public void updateData(List<Tag> tagList){
        this.tagList = tagList != null ? tagList : new ArrayList<>();
        notifyDataSetChanged();
    }

}
