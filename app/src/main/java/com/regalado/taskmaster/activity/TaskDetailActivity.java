package com.regalado.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.regalado.taskmaster.R;

import org.w3c.dom.Text;

public class TaskDetailActivity extends AppCompatActivity {

    // create a string for logging
    //public String TAG = "taskDetailActivity";
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        //preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Intent callingIntent = getIntent();
        String taskTitle = null;
        String taskBody = null;
        String taskState = null;

        if(callingIntent != null)
        {
            taskTitle = callingIntent.getStringExtra(MainActivity.TASK_TITLE_TAG);
            taskBody = callingIntent.getStringExtra(MainActivity.TASK_BODY_TAG);
            taskState = callingIntent.getStringExtra(MainActivity.TASK_STATE_TAG);
        }

        TextView taskDetailTitle = (TextView) findViewById(R.id.textViewTaskTitleDetailsActivity);
        TextView taskDetailBody = (TextView) findViewById(R.id.textViewTaskBodyTaskDetailActivity);
        TextView taskDetailState = (TextView) findViewById(R.id.textViewTaskStateTaskDetailActivity);

        if(taskTitle != null)
        {
            taskDetailTitle.setText(taskTitle);
        }
        else
        {
            taskDetailTitle.setText(R.string.no_task);
        }

        taskDetailBody.setText(taskBody);
        taskDetailState.setText(taskState);
    }
}