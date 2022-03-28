package com.regalado.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.regalado.taskmaster.R;
import com.regalado.taskmaster.adapter.TaskListRecyclerViewAdapter;
import com.regalado.taskmaster.model.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // create a string for logging
    public String TAG = "MainActivity";
    public static final String TASK_DETAIL_TITLE_TAG = "taskTitle";
    SharedPreferences preferences;

    // Create and attach the RV adapter
    TaskListRecyclerViewAdapter myTasksListRecyclerviewAdapter;
    List<Task> taskArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // super and setContentView needs to remain at the top
        // setContentView creates all of your UI elements.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialization
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        Log.d(TAG, "onCreate() got called!");

        addTaskNavigationButton();
        allTasksNavigationButton();
        settingsNavigationButton();
        taskListRecyclerView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume() got called!");

        String userNickname = preferences.getString(SettingsActivity.USER_NAME_TAG, "No nickname");
        ((TextView)findViewById(R.id.textViewUsernameMainActivity)).setText(getString(R.string.nickname_main_activity, userNickname));
    }

    public void addTaskNavigationButton()
    {
        Button buttonToAddTaskPage = (Button) findViewById(R.id.buttonAddTaskMainActivity);
        buttonToAddTaskPage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                System.out.println("submitted!");
                Log.e(TAG, "Logging");
                // target textview and change what gets printed to that view- don't hardcode values, set values in the string.xml file.
                //((TextView)findViewById(R.id.totalTasksTextView)).setText(R.string.submitted);

                // use intent to navigate to different pages
                // Intent has two arguments: the context where you're coming from (aka the source Activity), and the place where you're going (the destination Activity)
                Intent goToAddTaskPage = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(goToAddTaskPage);

                // Alternate way of using Intent
                // MainActivity.this.startActivity(goToAddTaskPage);

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
                System.out.println("submitted!");
                Log.e(TAG, "Logging");

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
                System.out.println("submitted!");
                Log.e(TAG, "Logging");

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
        //for horizontal layout
        //((LinearLayoutManager)layoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);

        taskArrayList.add(new Task("Workout"));
        taskArrayList.add(new Task("Walk Dog"));
        taskArrayList.add(new Task("Return Book"));
        taskArrayList.add(new Task("Laundry"));
        taskArrayList.add(new Task("Wash Car"));
        taskArrayList.add(new Task("Homework"));
        taskArrayList.add(new Task("Pay Phone Bill"));
        taskArrayList.add(new Task("Pay Rent"));
        taskArrayList.add(new Task("Dinner with Friends"));
        taskArrayList.add(new Task("Clean Room"));
        taskArrayList.add(new Task("Prep For Finals"));

        // Hand in data items
        myTasksListRecyclerviewAdapter = new TaskListRecyclerViewAdapter(taskArrayList, this);
        taskListRecyclerView.setAdapter(myTasksListRecyclerviewAdapter);

    }
}




//    public void taskOneButton()
//    {
//        TextView taskOneToTaskPage = (TextView) findViewById(R.id.textViewTaskOneMainActivity);
//        taskOneToTaskPage.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                System.out.println("submitted!");
//                Log.e(TAG, "Logging");
//
//                Intent goToTaskOneDetails = new Intent(MainActivity.this, TaskDetailActivity.class);
//                startActivity(goToTaskOneDetails);
//                String taskTitle = "Finish Homework";
//                SharedPreferences.Editor preferenceEditor = preferences.edit();
//                preferenceEditor.putString(TASK_DETAIL_TITLE_TAG, taskTitle);
//                preferenceEditor.apply();
//            }
//        });
//    }
//
//    public void taskTwoButton()
//    {
//        TextView taskTwoToTaskPage = (TextView) findViewById(R.id.taskViewTaskTwoMainActivity);
//        taskTwoToTaskPage.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                System.out.println("submitted!");
//                Log.e(TAG, "Logging");
//
//                Intent goToTaskTwoDetails = new Intent(MainActivity.this, TaskDetailActivity.class);
//                startActivity(goToTaskTwoDetails);
//                String taskTitle = "Walk Dog!";
//                SharedPreferences.Editor preferenceEditor = preferences.edit();
//                preferenceEditor.putString(TASK_DETAIL_TITLE_TAG, taskTitle);
//                preferenceEditor.apply();
//            }
//        });
//    }
//
//    public void taskThreeButton()
//    {
//        TextView taskThreeToTaskPage = (TextView) findViewById(R.id.textViewTaskThreeMainActivity);
//        taskThreeToTaskPage.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                System.out.println("submitted!");
//                Log.e(TAG, "Logging");
//
//                Intent goToTaskThreeDetails = new Intent(MainActivity.this, TaskDetailActivity.class);
//                startActivity(goToTaskThreeDetails);
//                String taskTitle = "Clean my room!";
//                SharedPreferences.Editor preferenceEditor = preferences.edit();
//                preferenceEditor.putString(TASK_DETAIL_TITLE_TAG, taskTitle);
//                preferenceEditor.apply();
//            }
//        });
//    }