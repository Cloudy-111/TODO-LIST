package com.example.todolist.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.todolist.R;
import com.example.todolist.Utils.ChangeColorUtils;
import com.example.todolist.model.Tag;
import com.example.todolist.model.TaskItem;
import com.example.todolist.model.TaskLog;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.imageview.ShapeableImageView;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<TaskItem> taskList;
    private List<Tag> tagList;
    private List<TaskLog> taskLogsToday;
    private OnCheckTaskListener checkTaskListener;

    public interface OnCheckTaskListener{
        void onTaskChecked(TaskItem task, boolean isChecked);
    }

    public TaskAdapter(List<TaskItem> taskList, List<Tag> tagList, List<TaskLog> taskLogsToday, OnCheckTaskListener listener){
        this.tagList = tagList;
        this.taskList = taskList;
        this.taskLogsToday = taskLogsToday;
        this.checkTaskListener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position){
        TaskItem item = taskList.get(position);

        holder.nameTask.setText(item.getName());
        holder.timeTask.setText(item.getDue_time());
        Glide.with(holder.itemView.getContext())
                .load(item.getImage_task())
                .placeholder(R.drawable.cat) // Ảnh tạm khi đang tải
                .error(R.drawable.ic_error)  // Ảnh lỗi nếu load fail
                .into(holder.imageTask);
//        holder.checkBox.setChecked(item.get);

        Tag matchedTag = null;
        for(Tag tag : tagList){
            if(tag.getId() == item.getTagId()){
                matchedTag = tag;
                break;
            }
        }

        if (matchedTag != null) {
            try {
                int color = Color.parseColor(matchedTag.getColor());
                Drawable background = holder.colorBar.getBackground();
                if (background instanceof GradientDrawable) {
                    ((GradientDrawable) background).setColor(color);
                }
            } catch (IllegalArgumentException e) {
                ChangeColorUtils.fallbackGray(holder.colorBar); // fallback nếu sai mã màu
            }
        } else {
            ChangeColorUtils.fallbackGray(holder.colorBar); // fallback nếu không tìm thấy Tag
        }

        boolean isChecked = false;
        for(TaskLog log : taskLogsToday){
            if(log.getTaskId() == item.getId()){
                isChecked = true;
                break;
            }
        }

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(isChecked);

        if(isChecked){
            holder.nameTask.setPaintFlags(holder.nameTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.timeTask.setPaintFlags(holder.timeTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            holder.nameTask.setTextColor(Color.GRAY);
            holder.timeTask.setTextColor(Color.GRAY);
            holder.imageTask.setAlpha(0.4f);
        } else {
            holder.nameTask.setPaintFlags(holder.nameTask.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.timeTask.setPaintFlags(holder.timeTask.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

            holder.nameTask.setTextColor(Color.BLACK);
            holder.timeTask.setTextColor(Color.DKGRAY);
            holder.imageTask.setAlpha(1f);
        }

        holder.checkBox.setOnCheckedChangeListener(((buttonView, checked) -> {
            if(checked){
                new AlertDialog.Builder(holder.itemView.getContext())
                        .setTitle("Confirm")
                        .setMessage("Are you sure about that?")
                        .setPositiveButton("YES", ((dialog, which) -> {
                            if(checkTaskListener != null){
                                checkTaskListener.onTaskChecked(item, true);
                            }
                        }))
                        .setNegativeButton("NO", ((dialog, which) -> {
                            holder.checkBox.setChecked(false);
                        }))
                        .setCancelable(false)
                        .show();
            } else {
                if (checkTaskListener != null) {
                    checkTaskListener.onTaskChecked(item, false);
                }
            }
        }));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView imageTask;
        TextView nameTask, timeTask;
        MaterialCheckBox checkBox;
        View colorBar;

        public TaskViewHolder(@NonNull View itemView){
            super(itemView);
            imageTask = itemView.findViewById(R.id.image_task);
            nameTask = itemView.findViewById(R.id.name_task);
            timeTask = itemView.findViewById(R.id.time_task);
            checkBox = itemView.findViewById(R.id.check_task);
            colorBar = itemView.findViewById(R.id.color_bar);
        }
    }

    public void updateData(List<TaskItem> listTaskItem){
        this.taskList = listTaskItem != null ? listTaskItem : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void updateTags(List<Tag> tagList){
        this.tagList = tagList != null ? tagList : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void updateLogs(List<TaskLog> taskLogsToday){
        this.taskLogsToday = taskLogsToday != null ? taskLogsToday : new ArrayList<>();
        notifyDataSetChanged();
    }
}