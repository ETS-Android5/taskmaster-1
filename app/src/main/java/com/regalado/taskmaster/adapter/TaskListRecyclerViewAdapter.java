package com.regalado.taskmaster.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.regalado.taskmaster.R;
import com.regalado.taskmaster.activity.MainActivity;
import com.regalado.taskmaster.activity.TaskDetailActivity;
import com.regalado.taskmaster.model.Task;

import org.w3c.dom.Text;

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
        // ViewHolders are the boxes within our Recyclerview that holds our data
        // Inflate fragment
        View taskFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task_list, parent, false);
        // Attach fragment to ViewHolder
        return new TaskListViewHolder(taskFragment);
    }


    // Bind data items to ViewHolders
    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position)
    {
        // Manages the data that goes into the ViewHolder
        TextView taskFragmentTextView = (TextView) holder.itemView.findViewById(R.id.textViewTaskFragmentTaskListFragment);
        String taskTitle = taskArrayList.get(position).getName();
        taskFragmentTextView.setText(taskTitle);

        // Make onClickHandler to interact with RV items
        View taskViewHolder = holder.itemView;
        taskViewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {

                // Create Intent, populate data and call Intent
                Intent goToTaskDetailIntent = new Intent(callingActivity, TaskDetailActivity.class);
                goToTaskDetailIntent.putExtra(MainActivity.TASK_DETAIL_TITLE_TAG, taskTitle);
                callingActivity.startActivity(goToTaskDetailIntent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        // For testing purpose, hardcode a large number of items
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
