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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Team;
import com.regalado.taskmaster.R;
import com.regalado.taskmaster.adapter.TaskListRecyclerViewAdapter;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.State;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    // create a string for logging
    public String TAG = "MainActivity";
    public static final String TASK_TITLE_TAG = "taskTitle";
    public static final String TASK_BODY_TAG = "BODY";
    public static final String TASK_STATE_TAG = "STATE";
    public static final String TASK_IMAGE_TAG = "IMAGE";

    SharedPreferences preferences;

    // Create and attach the RV adapter
    TaskListRecyclerViewAdapter myTasksListRecyclerviewAdapter;
    List<Task> taskArrayList = null;
    CompletableFuture<List<String>> teamNamesFuture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        // Manually create an S3 file for testing

        String emptyFilename = "emptyTestFilename";
        File emptyFile = new File(getApplicationContext().getFilesDir(), "emptyTestFileName");

        try
        {
            BufferedWriter emptyFileBufferedWriter = new BufferedWriter(new FileWriter(emptyFile));
            emptyFileBufferedWriter.append("Some test text here\n Another line of test test");
            emptyFileBufferedWriter.close(); // make sure to do this or the text may not be saved!
        } catch(IOException ioe)
        {
            Log.e(TAG, "could not write file locally with fileman");
        }

        String emptyFileS3Key = "someFileOnS3";

        Amplify.Storage.uploadFile(
                emptyFileS3Key,
                emptyFile,
                success ->
                {
                    Log.i(TAG, "S3 upload succeeded! Key is: " + success.getKey());
                },
                failure ->
                {
                    Log.i(TAG, "S3 upload failed! " + failure.getMessage());
                }
        );

    }

    private void init()
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        taskArrayList = new ArrayList<>();
        addTaskNavigationButton();
        allTasksNavigationButton();
        settingsNavigationButton();
        taskListRecyclerView();
        setupLoginLogoutButtons();
        filterTaskListFromDatabase();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        taskListRecyclerView();
        filterTaskListFromDatabase();
        handleLoginAndLogoutButtonVisibility();
    }

    public void filterTaskListFromDatabase()
    {
        String team = preferences.getString(SettingsActivity.USER_TEAM_TAG, "No team name");
        String userNickname = preferences.getString(SettingsActivity.USER_NAME_TAG, "No nickname");
        ((TextView)findViewById(R.id.textViewUsernameMainActivity)).setText(getString(R.string.nickname_main_activity, userNickname));


        Amplify.API.query(
                ModelQuery.list(Task.class),
                success ->
                {
                    Log.i(TAG, "Read teams successfully");
                    taskArrayList.clear();
                    for(Task task : success.getData())
                    {
                        if(task.getTeam().getTeamName().equals(team))
                            taskArrayList.add(task);
                    }
                    runOnUiThread(() -> myTasksListRecyclerviewAdapter.notifyDataSetChanged());
                },
                failure ->
                {
                    Log.i(TAG, "did not read team names successfully");
                }
        );
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

    public void setupLoginLogoutButtons()
    {
        Button loginButton = (Button) findViewById(R.id.buttonLoginMainActivity);
        loginButton.setOnClickListener(v ->
        {
            Intent goToLogInIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(goToLogInIntent);
        });

        Button logoutButton = (Button) findViewById(R.id.buttonLogoutMainActivity);
        logoutButton.setOnClickListener(v ->
        {
            Amplify.Auth.signOut(
                    () ->
                    {
                        Log.i(TAG, "Logout succeeded!");
                        runOnUiThread(() ->
                                {
                                    ((TextView) findViewById(R.id.textViewUsernameMainActivity)).setText("");
                                }
                        );
                        Intent goToLogInIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(goToLogInIntent);
                    },
                    failure ->
                    {
                        Log.i(TAG, "Logout failed: " + failure.toString());
                        runOnUiThread(() ->
                        {
                            Toast.makeText(MainActivity.this, "Log out failed!", Toast.LENGTH_SHORT).show();
                        });
                    }
            );
        });
    }

    public void handleLoginAndLogoutButtonVisibility()
    {
        AuthUser authUser = Amplify.Auth.getCurrentUser();
        String username = "";
        if (authUser == null)
        {
            Button loginButton = (Button) findViewById(R.id.buttonLoginMainActivity);
            loginButton.setVisibility(View.VISIBLE);
            Button logoutButton = (Button) findViewById(R.id.buttonLogoutMainActivity);
            logoutButton.setVisibility(View.INVISIBLE);
        }
        else  // authUser is not null
        {
            Log.i(TAG, "Username is: " + username);
            Button loginButton = (Button) findViewById(R.id.buttonLoginMainActivity);
            loginButton.setVisibility(View.INVISIBLE);
            Button logoutButton = (Button) findViewById(R.id.buttonLogoutMainActivity);
            logoutButton.setVisibility(View.VISIBLE);


            Amplify.Auth.fetchUserAttributes(
                    success ->
                    {
                        Log.i(TAG, "Fetch user attributes succeeded for username: " + username);

                        for (AuthUserAttribute userAttribute : success)
                        {
                            if (userAttribute.getKey().getKeyString().equals("nickname"))
                            {
                                String userNickname = userAttribute.getValue();
                                runOnUiThread(() ->
                                        {
                                            ((TextView)findViewById(R.id.textViewUsernameMainActivity)).setText(userNickname);
                                        }
                                );
                            }
                        }
                    },
                    failure ->
                    {
                        Log.i(TAG, "Fetch user attributes failed: " + failure.toString());
                    }
            );
        }
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



























/////// HARD CODING TEAMS ///////////////////

//        Team team1 = Team.builder().teamName("Code Fellows").build();
//        Amplify.API.mutate(
//                ModelMutation.create(team1),
//                success -> Log.i(TAG, "MainActivity.onCreate(): made a team successfully"),
//                failureResponse -> Log.i(TAG, "MainActivity.onCreate(): team failed with this response " + failureResponse)
//        );
//
//        Team team2 = Team.builder().teamName("Jedi Grey").build();
//        Amplify.API.mutate(
//                ModelMutation.create(team2),
//                success -> Log.i(TAG, "MainActivity.onCreate(): made a team successfully"),
//                failureResponse -> Log.i(TAG, "MainActivity.onCreate(): team failed with this response " + failureResponse)
//        );
//
//        Team team3 = Team.builder().teamName("Crud Alchemy").build();
//        Amplify.API.mutate(
//                ModelMutation.create(team3),
//                success -> Log.i(TAG, "MainActivity.onCreate(): made a team successfully"),
//                failureResponse -> Log.i(TAG, "MainActivity.onCreate(): team failed with this response " + failureResponse)
//        );
