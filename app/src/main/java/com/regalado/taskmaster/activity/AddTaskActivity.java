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
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.snackbar.Snackbar;
import com.regalado.taskmaster.R;
//import com.regalado.taskmaster.viewmodel.State;
//import com.regalado.taskmaster.viewmodel.Task;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.State;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTaskActivity extends AppCompatActivity {

    public static final String TAG = "AddTaskActivity";
    List<String> teamNames;
    Spinner teamNameSpinner = null;
    Spinner statusSpinner = null;
    Spinner taskStateSpinner = null;
    CompletableFuture<List<Team>> teamsFuture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        teamsFuture = new CompletableFuture<>();

        saveNewTaskButton();
        setUpSpinners();
    }

    private void setUpSpinners()
    {
        teamNameSpinner = (Spinner)findViewById(R.id.spinnerTeamNameAddTaskActivity);
        teamNames = new ArrayList<>();
        teamNameSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                State.values())); // TODO: change these values

        taskStateSpinner = (Spinner)findViewById(R.id.spinnerTaskStateAddTaskActivity);
        taskStateSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                State.values()));

        Amplify.API.query(
                ModelQuery.list(Team.class),
                success ->
                {
                    Log.i(TAG, "Read teams successfully");
                    ArrayList<String> teamNames = new ArrayList<>();
                    ArrayList<Team> teams = new ArrayList<>();
                    for(Team team : success.getData())
                    {
                        teams.add(team);
                        teamNames.add(team.getTeamName());
                    }

                    teamsFuture.complete(teams);

                    runOnUiThread(() ->
                    {
                        teamNameSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                teamNames));
                    });
                },
                failure ->
                {
                    teamsFuture.complete(null);
                    Log.i(TAG, "did not read team names successfully");
                }
        );
    }

    private void saveNewTaskButton()
    {
        Button submitButton = (Button) findViewById(R.id.addTaskButton);
//        Spinner taskStateSpinner = (Spinner)findViewById(R.id.spinnerTaskStateAddTaskActivity);
//        taskStateSpinner.setAdapter(new ArrayAdapter<>(
//                this,
//                android.R.layout.simple_spinner_item,
//                State.values()));

        submitButton.setOnClickListener(view -> {

            String name = ((EditText)findViewById(R.id.editTextTaskNameAddTaskActivity)).getText().toString();
            String body = ((EditText)findViewById(R.id.editTextTaskDescriptionAddTaskActivity)).getText().toString();
            String currentDataString = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());
            String selectedNameString = teamNameSpinner.getSelectedItem().toString();

            List<Team> teams = null;
            try
            {
              teams = teamsFuture.get();
            }
            catch (InterruptedException ie)
            {
                Log.e(TAG, "InterruptedException while getting teams");
                Thread.currentThread().interrupt();
            }
            catch (ExecutionException ee)
            {
                Log.e(TAG, "ExecutionException while getting teams");
            }

            Team selectedTeam = teams.stream().filter(t -> t.getTeamName().equals(selectedNameString)).findAny().orElseThrow(RuntimeException::new);

            Task newTask = Task.builder()
                    .name(name)
                    .body(body)
                    .dateCreated(new Temporal.DateTime(currentDataString))
                    .state((State) taskStateSpinner.getSelectedItem())
                    .team(selectedTeam)
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