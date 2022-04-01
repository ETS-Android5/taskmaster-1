package com.regalado.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.google.android.material.snackbar.Snackbar;
import com.regalado.taskmaster.R;
//import com.regalado.taskmaster.viewmodel.State;
//import com.regalado.taskmaster.viewmodel.Task;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.State;

import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {

    public static final String TAG = "AddTaskActivity";


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

            String name = ((EditText)findViewById(R.id.editTextTaskNameAddTaskActivity)).getText().toString();
            String body = ((EditText)findViewById(R.id.editTextTaskDescriptionAddTaskActivity)).getText().toString();
            String currentDataString = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());

            Task newTask = Task.builder()
                    .name(name)
                    .body(body)
                    .dateCreated(new Temporal.DateTime(currentDataString))
                    .state((State) taskStateSpinner.getSelectedItem())
                    .build();

            Amplify.API.mutate(
                ModelMutation.create(newTask), // making a GraphQL request to the cloud
                successResponse -> Log.i(TAG, "AddTaskActivity.onCreate(): made a task successfully"), // success callback
                failureResponse -> Log.i(TAG, "AddTaskActivity.onCreate(): failed with this response: " + failureResponse) // failure callback
                );
            submitButton.onEditorAction(EditorInfo.IME_ACTION_DONE);
            Snackbar.make(findViewById(R.id.textViewSubmit), "Task Saved!", Snackbar.LENGTH_SHORT).show();
        });
    }
}