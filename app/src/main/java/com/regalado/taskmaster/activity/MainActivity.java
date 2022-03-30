package com.regalado.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.regalado.taskmaster.R;
import com.regalado.taskmaster.adapter.TaskListRecyclerViewAdapter;
import com.regalado.taskmaster.model.State;
import com.regalado.taskmaster.model.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // create a string for logging
    public String TAG = "MainActivity";
    public static final String TASK_TITLE_TAG = "taskTitle";
    public static final String TASK_BODY_TAG = "BODY";
    public static final String TASK_STATE_TAG = "STATE";

    SharedPreferences preferences;

    // Create and attach the RV adapter
    TaskListRecyclerViewAdapter myTasksListRecyclerviewAdapter;
    List<Task> taskArrayList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initialization
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        taskArrayList = new ArrayList<>();
        taskArrayList.add(new Task("Workout", "Run 5 miles", new Date(), State.NEW));


        // TODO: Change this to a Dynamo / GraphQl query
       //taskArrayList = taskMasterDatabase.taskDao().findAll();

        addTaskNavigationButton();
        allTasksNavigationButton();
        settingsNavigationButton();
        taskListRecyclerView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        String userNickname = preferences.getString(SettingsActivity.USER_NAME_TAG, "No nickname");
        ((TextView)findViewById(R.id.textViewUsernameMainActivity)).setText(getString(R.string.nickname_main_activity, userNickname));
        // TODO: Change this to a Dynamo / GraphQl query
        //taskArrayList = taskMasterDatabase.taskDao().findAll();
        taskListRecyclerView();
    }

    public void addTaskNavigationButton()
    {
        Button buttonToAddTaskPage = (Button) findViewById(R.id.buttonAddTaskMainActivity);
        buttonToAddTaskPage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent goToAddTaskPage = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(goToAddTaskPage);
            }
        });
    }

    public void allTasksNavigationButton()
    {
        Button buttonToAllTaskPage = (Button) findViewById(R.id.buttonAllTasksMainActivity);
        buttonToAllTaskPage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent goToAllTaskPage = new Intent(MainActivity.this, AllTasksActivity.class);
                startActivity(goToAllTaskPage);
            }
        });
    }

    public void settingsNavigationButton()
    {
        ImageButton imageButtonToSettingsPage = (ImageButton) findViewById(R.id.imageViewSettingsIconMainActivity);
        imageButtonToSettingsPage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent goToSettings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(goToSettings);
            }
        });
    }

    public void taskListRecyclerView()
    {
        RecyclerView taskListRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewTaskListMainActivity);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskListRecyclerView.setLayoutManager(layoutManager);
        myTasksListRecyclerviewAdapter = new TaskListRecyclerViewAdapter(taskArrayList, this);
        taskListRecyclerView.setAdapter(myTasksListRecyclerviewAdapter);
    }

}















//for horizontal layout
//((LinearLayoutManager)layoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);

//        taskArrayList.add(new Task("Workout", "Run 5 miles!", new Date(), State.NEW));
//        taskArrayList.add(new Task("Walk Dog", "Walk dog at noon.", new Date(), State.COMPLETE));
//        taskArrayList.add(new Task("Return Book", "Return Amazon book.", new Date(), State.COMPLETE));
//        taskArrayList.add(new Task("Laundry", "Wash Linens", new Date(), State.IN_PROGRESS));
//        taskArrayList.add(new Task("Wash Car", "Detail Jeep and Subarau", new Date(), State.IN_PROGRESS));
//        taskArrayList.add(new Task("Homework", "Finish readings for class 30.", new Date(), State.IN_PROGRESS));
//        taskArrayList.add(new Task("Phone Bill", "Pay T-mobile phone bill on the 15th.", new Date(), State.COMPLETE));
//        taskArrayList.add(new Task("Pay Rent", "Pay Rent and Storage Fees on the 1st", new Date(), State.NEW));
//        taskArrayList.add(new Task("Dinner with Friends", "Go to birthday dinner with Jimmy and Johnny", new Date(), State.ASSIGNED));
//        taskArrayList.add(new Task("Clean Room", "Vacuum and Organize desk.", new Date(), State.NEW));
//        taskArrayList.add(new Task("Prep For Finals", "Research Android projects", new Date(), State.NEW));
