package com.regalado.taskmaster.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.regalado.taskmaster.R;
import com.regalado.taskmaster.activity.MainActivity;
import com.regalado.taskmaster.activity.TaskDetailActivity;
//import com.regalado.taskmaster.viewmodel.Task;
import com.amplifyframework.datastore.generated.model.Task;

import java.util.List;

public class TaskListRecyclerViewAdapter extends RecyclerView.Adapter<TaskListRecyclerViewAdapter.TaskListViewHolder>
{
    // Hand in data items
    List<Task> taskArrayList;
    Context callingActivity;

    // Hand in data items
    public TaskListRecyclerViewAdapter(List<Task> tasksArrayList, Context callingActivity)
    {
        this.taskArrayList = tasksArrayList;
        this.callingActivity = callingActivity;
    }

    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View taskFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task_list, parent, false);
        return new TaskListViewHolder(taskFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position)
    {
        TextView taskFragmentTextView = (TextView) holder.itemView.findViewById(R.id.textViewTaskFragmentTaskListFragment);
        String taskTitle = taskArrayList.get(position).getName();
        String taskBody = taskArrayList.get(position).getBody();
        String taskState = taskArrayList.get(position).getState().toString();
        taskFragmentTextView.setText(taskArrayList.get(position).getName());

        View taskViewHolder = holder.itemView;
        taskViewHolder.setOnClickListener(view -> {

            Intent goToTaskDetailIntent = new Intent(callingActivity, TaskDetailActivity.class);
            goToTaskDetailIntent.putExtra(MainActivity.TASK_TITLE_TAG, taskTitle);
            goToTaskDetailIntent.putExtra(MainActivity.TASK_BODY_TAG, taskBody);
            goToTaskDetailIntent.putExtra(MainActivity.TASK_STATE_TAG, taskState);
            callingActivity.startActivity(goToTaskDetailIntent);
        });
    }

    @Override
    public int getItemCount()
    {
        return taskArrayList.size();
    }

    // Make ViewHolder sub-class to hold/store a fragment
    public static class TaskListViewHolder extends RecyclerView.ViewHolder
    {
        public TaskListViewHolder(View fragmentItemView)
        {
            super(fragmentItemView);
        }
    }
}
