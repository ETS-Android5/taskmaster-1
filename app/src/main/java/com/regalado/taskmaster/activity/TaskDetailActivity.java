package com.regalado.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.regalado.taskmaster.R;
import com.regalado.taskmaster.activity.MainActivity;

public class TaskDetailActivity extends AppCompatActivity {

    // create a string for logging
    public String TAG = "taskDetailActivity";
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        String taskTitle = preferences.getString(MainActivity.TASK_DETAIL_TITLE_TAG, "Empty");
        ((TextView)findViewById(R.id.textViewTaskTitleDetailsActivity)).setText(getString(R.string.task_title, taskTitle));
    }
}