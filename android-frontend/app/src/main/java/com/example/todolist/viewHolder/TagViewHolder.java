package com.example.todolist.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;

public class TagViewHolder extends RecyclerView.ViewHolder {
    public TextView tagName;
    public View tagColorView;

    public TagViewHolder(@NonNull View itemView) {
        super(itemView);
        tagName = itemView.findViewById(R.id.tag_name);
        tagColorView = itemView.findViewById(R.id.tag_color_view);
    }
}
