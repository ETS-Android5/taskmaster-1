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
        if(callingIntent != null)
        {
            taskTitle = callingIntent.getStringExtra(MainActivity.TASK_DETAIL_TITLE_TAG);
        }
        TextView taskDetailTextView = (TextView) findViewById(R.id.textViewTaskTitleDetailsActivity);
        if(taskTitle != null)
        {
            taskDetailTextView.setText(taskTitle);
        }
        else
        {
            taskDetailTextView.setText(R.string.no_task);
        }
    }

//    @Override
//    public void onResume()
//    {
//        super.onResume();
//        String taskTitle = preferences.getString(MainActivity.TASK_DETAIL_TITLE_TAG, getString(R.string.no_task));
////        ((TextView)findViewById(R.id.textViewTaskTitleDetailsActivity)).setText(getString(R.string.task_title, taskTitle));
//        ((TextView)findViewById(R.id.textViewTaskTitleDetailsActivity)).setText(getString(R.string.task_title, taskTitle));
//    }
}