package com.regalado.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.regalado.taskmaster.R;
import com.regalado.taskmaster.model.State;
import com.regalado.taskmaster.model.Task;

import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {

    public String TAG = "AddTaskActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        saveNewTask();
    }

    public void saveNewTask()
    {
        Button submitButton = (Button) findViewById(R.id.addTaskButton);
        Spinner taskStateSpinner = (Spinner)findViewById(R.id.spinnerTaskStateAddTaskActivity);
        taskStateSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                State.values()));

        submitButton.setOnClickListener(view -> {
            Task newTask = new Task(
                    ((EditText)findViewById(R.id.editTextTaskNameAddTaskActivity)).getText().toString(),
                    ((EditText)findViewById(R.id.editTextTaskDescriptionAddTaskActivity)).getText().toString(),
                    new Date(),
                    State.fromString(taskStateSpinner.getSelectedItem().toString())
            );

            ((TextView)findViewById(R.id.textViewSubmit)).setText(R.string.submitted);
            // TODO: Change this to a dynamoDB /GraphQl query
            // taskMasterDatabase.taskDao().insertTask(newTask);
            submitButton.onEditorAction(EditorInfo.IME_ACTION_DONE);
            Snackbar.make(findViewById(R.id.textViewSubmit), "Task Saved!", Snackbar.LENGTH_SHORT).show();
        });
    }
}