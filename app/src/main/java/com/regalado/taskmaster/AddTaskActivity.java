package com.regalado.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddTaskActivity extends AppCompatActivity {

    // create a string for logging
    public String TAG = "AddTaskActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // target our button
        Button submitButton = (Button) findViewById(R.id.addTaskButton);

        // create onClickListener
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                System.out.println("submitted!");
                Log.e(TAG, "Logging");
                ((TextView)findViewById(R.id.textViewSubmit)).setText(R.string.submitted);
            }
        });
    }
}