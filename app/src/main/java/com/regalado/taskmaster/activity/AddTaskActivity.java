package com.regalado.taskmaster.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.FileNotFoundException;
import java.io.InputStream;
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
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        teamsFuture = new CompletableFuture<>();
        activityResultLauncher = getImageSelectedActivityResultLauncher();  // must set this up in onCreate() in the lifecycle: Do not set this up in an onClickHandler()

        saveNewTaskButton();
        setUpSpinners();
//        launchImageSelectedIntent();
//        getImageSelectedActivityResultLauncher();
        setUpSaveImageButton();
//        setUpDeleteImageButton();
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
//            Snackbar.make(findViewById(R.id.textViewSubmit), "Task Saved!", Snackbar.LENGTH_SHORT).show();
//            Toast.makeText(AddTaskActivity.this, "Log out failed!", Toast.LENGTH_SHORT).show();
        });
    }

    private void setUpDeleteTaskButton()
    {
//        Button deleteTaskButton = (Button) findViewById(R.id.buttonDeleteTaskAddTaskActivity);
    }

    private void setUpSaveImageButton()
    {
        Button addImageButton = (Button) findViewById(R.id.buttonAddImageAddTaskActivity);
        addImageButton.setOnClickListener(b->
        {
            launchImageSelectedIntent();
        });
    }

    private void launchImageSelectedIntent()
    {
        // 1: launch activity to pick a file
        Intent imageFileSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFileSelectionIntent.setType("*/*");
        imageFileSelectionIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg"});
//        startActivity(imageFileSelectionIntent); <- simple version for testing
        activityResultLauncher.launch(imageFileSelectionIntent);
    }

    private ActivityResultLauncher<Intent> getImageSelectedActivityResultLauncher()
    {
        // 2: create an image picking activity result launcher
        ActivityResultLauncher<Intent> imageSelectedActivityResultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>()
                        {
                            @Override
                            public void onActivityResult(ActivityResult result)
                            {
                                if(result.getResultCode() == Activity.RESULT_OK)
                                {
                                    if (result.getData() != null)
                                    {
                                        Uri selectedImageFileUri = result.getData().getData();
                                        try
                                        {
                                            InputStream selectedImageInputStream = getContentResolver().openInputStream(selectedImageFileUri);
                                            String selectedImageFilename = getFileNameFromUri(selectedImageFileUri);
                                            Log.i(TAG, "Succeeded in getting input stream from file on phone! Filename is: " + selectedImageFilename);
                                            uploadInputStreamToS3(selectedImageInputStream, selectedImageFilename);
                                        } catch (FileNotFoundException fnfe)
                                        {
                                            Log.e(TAG, "Could not get file from file picker! " + fnfe.getMessage(), fnfe);
                                        }
                                    }
                                }
                                else
                                {
                                    Log.e(TAG, "Activity result error in ActivityResultLauncher.onActivityResult");
                                }
                            }
                        }
                );
        return imageSelectedActivityResultLauncher;
    }

    private void uploadInputStreamToS3(InputStream selectedImageInputStream, String selectedImageFilename)
    {
        Amplify.Storage.uploadInputStream(
                selectedImageFilename, // S3 key
                selectedImageInputStream,
                success ->
                {
                    Log.i(TAG, "Succeeded in getting file uploaded to S3! Key is: " + success.getKey());
//                    saveUpdatedTaskToDb(success.getKey());
                },
                failure ->
                {
                  Log.e(TAG, "Failure in uploading file to S3 with filename: " + selectedImageFilename + " with error: " + failure.getMessage());
                }
        );
    }

    @SuppressLint("Range")
    public String getFileNameFromUri(Uri uri)
    {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    private void uploadInputStreamToS3()
    {
        System.out.println("upload to stream s3");
    }

    private void saveUpdatedTaskToDb()
    {
        System.out.println("save to database");
    }
}