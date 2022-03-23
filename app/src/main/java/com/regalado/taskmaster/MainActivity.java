package com.regalado.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // create a string for logging
    public String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // super and setContentView needs to remain at the top
        // setContentView creates all of your UI elements.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // step 1: Get UI components
        Button buttonToAddTaskPage = (Button) findViewById(R.id.addTaskButtonMainActivity);
        Button buttonToAllTaskPage = (Button) findViewById(R.id.allTasksButtonMainActivity);
        ImageButton imageButtonToSettingsPage = (ImageButton) findViewById(R.id.goToSettingsIcon);
        TextView taskOneToTaskPage = (TextView) findViewById(R.id.taskOneTextView);
        TextView taskTwoToTaskPage = (TextView) findViewById(R.id.taskTwoTextView);
        TextView taskThreeToTaskPage = (TextView) findViewById(R.id.taskThreeTextView);
        TextView taskTitle = (TextView) findViewById(R.id.taskTitle);

        // step 2: Set onClickListener
        // navigate to our addTaskPage
        buttonToAddTaskPage.setOnClickListener(new View.OnClickListener()
        {
            // step 3: Define the onClick() callback
            // navigate to our addTaskPage
            @Override
            public void onClick(View view)
            {
                // step 4: log some text
                System.out.println("submitted!");
                Log.e(TAG, "Logging");

                // target textview and change what gets printed to that view- don't hardcode values, set values in the string.xml file.
                //((TextView)findViewById(R.id.totalTasksTextView)).setText(R.string.submitted);

                // use intent to navigate to different pages
                // Intent has two arguments: the context where you're coming from (aka the source Activity), and the place where you're going (the destination Activity)
                Intent goToAddTaskPage = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(goToAddTaskPage);

                // Alternate form on Intent
                // MainActivity.this.startActivity(goToAddTaskPage);

            }
        });

        // navigate to our allTaskPage
        buttonToAllTaskPage.setOnClickListener(new View.OnClickListener()
        {
            // Define the onClick() callback
            @Override
            public void onClick(View view)
            {
                System.out.println("submitted!");
                Log.e(TAG, "Logging");

                Intent goToAllTaskPage = new Intent(MainActivity.this, AllTasksActivity.class);
                startActivity(goToAllTaskPage);
            }
        });

        // navigate to our allTaskPage
        imageButtonToSettingsPage.setOnClickListener(new View.OnClickListener()
        {
            // Define the onClick() callback
            @Override
            public void onClick(View view)
            {
                System.out.println("submitted!");
                Log.e(TAG, "Logging");

                Intent goToSettings = new Intent(MainActivity.this, Settings.class);
                startActivity(goToSettings);
            }
        });

        // navigate to task detail page
        taskOneToTaskPage.setOnClickListener(new View.OnClickListener()
        {
            // Define the onClick() callback
            @Override
            public void onClick(View view)
            {
                System.out.println("submitted!");
                Log.e(TAG, "Logging");

//                ((TextView)findViewById(R.id.taskTitle)).setText(R.string.taskOne);

                Intent goToTaskOne = new Intent(MainActivity.this, TaskDetail.class);
                startActivity(goToTaskOne);
            }
        });

        // navigate to task detail page
        taskTwoToTaskPage.setOnClickListener(new View.OnClickListener()
        {
            // Define the onClick() callback
            @Override
            public void onClick(View view)
            {
                System.out.println("submitted!");
                Log.e(TAG, "Logging");
//                ((TextView)findViewById(R.id.taskTitle)).setText(R.string.taskTwo);

                Intent goToTaskTwo = new Intent(MainActivity.this, TaskDetail.class);
                startActivity(goToTaskTwo);
            }
        });

        // navigate to task detail page
        taskThreeToTaskPage.setOnClickListener(new View.OnClickListener()
        {
            // Define the onClick() callback
            @Override
            public void onClick(View view)
            {
                System.out.println("submitted!");
                Log.e(TAG, "Logging");

//                ((TextView)findViewById(R.id.taskTitle)).setText(R.string.taskThree);

                Intent goToTaskThree = new Intent(MainActivity.this, TaskDetail.class);
                startActivity(goToTaskThree);
            }
        });
    }
}