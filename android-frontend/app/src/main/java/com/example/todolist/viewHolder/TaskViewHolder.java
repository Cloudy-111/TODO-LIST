package com.example.todolist.viewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.imageview.ShapeableImageView;

public class TaskViewHolder extends RecyclerView.ViewHolder{
    public ShapeableImageView imageTask;
    public TextView nameTask, timeTask;
    public MaterialCheckBox checkBox;
    public View colorBar;
    public LinearLayout infoTask;

    public TaskViewHolder(@NonNull View itemView){
        super(itemView);
        imageTask = itemView.findViewById(R.id.image_task);
        nameTask = itemView.findViewById(R.id.name_task);
        timeTask = itemView.findViewById(R.id.time_task);
        checkBox = itemView.findViewById(R.id.check_task);
        colorBar = itemView.findViewById(R.id.color_bar);
        infoTask = itemView.findViewById(R.id.info_task);
    }
}
