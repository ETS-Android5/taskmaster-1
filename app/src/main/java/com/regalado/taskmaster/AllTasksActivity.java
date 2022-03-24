package com.regalado.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AllTasksActivity extends AppCompatActivity {

    // create a string for logging
    public String TAG = "AllTaskActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);
    }
}