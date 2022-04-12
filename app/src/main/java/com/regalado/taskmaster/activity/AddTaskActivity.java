package com.regalado.taskmaster.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.regalado.taskmaster.R;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.State;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    Spinner taskStateSpinner = null;
    String imageS3Key = "";
    String selectedFileName;
    CompletableFuture<List<Team>> teamsFuture = null;
    ActivityResultLauncher<Intent> activityResultLauncher;
    InputStream selectedImageInputStream = null;
    Uri selectedImageFileUri;
    String selectedImageFileName = "";
    FusedLocationProviderClient locationProviderClient = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        teamsFuture = new CompletableFuture<>();

        activityResultLauncher = getImageSelectedActivityResultLauncher();
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        locationProviderClient.flushLocations();

        init();
    }

    private void init() {
        saveNewTaskButton();
        setUpSpinners();
        setUpSaveImageButton();
        setUpVisibilityImageButtons();
        setUpDeleteImageButton();
        setUpCallingIntent();
    }

    private void setUpCallingIntent() {
        Intent callingIntent = getIntent();
        if ((callingIntent != null) && (callingIntent.getType() != null)
                && (callingIntent.getType().startsWith("image"))) {
            Uri incomingImageFileUri = callingIntent.getParcelableExtra(Intent.EXTRA_STREAM);
            if (incomingImageFileUri != null) {
                try {
                    selectedImageFileUri = incomingImageFileUri;
                    selectedImageFileName = getFileNameFromUri(selectedImageFileUri);
                    Log.i(TAG, "Succeeded in getting input stream from file from calling intent! Filename is: " + selectedImageFileName);
                    InputStream incomingImageFileInputStream = getContentResolver().openInputStream(incomingImageFileUri);
                    saveImageFromCallingIntentToS3(incomingImageFileInputStream, selectedImageFileName, selectedImageFileUri);
                } catch (FileNotFoundException fnfe) {
                    Log.e(TAG, "Could not get file stream from URI! " + fnfe.getMessage(), fnfe);
                }
            }
        }
    }

    private void saveImageFromCallingIntentToS3(InputStream selectedImageInputStream, String selectedImageFileName, Uri selectedImageFileUri) {
        Amplify.Storage.uploadInputStream(
                selectedImageFileName,
                selectedImageInputStream,
                success ->
                {
                    imageS3Key = success.getKey();
                    ImageView taskImageView = findViewById(R.id.imageViewAddTaskActivity);
                    InputStream selectedImageInputStreamUpload = null;
                    try {
                        selectedImageInputStreamUpload = getContentResolver().openInputStream(selectedImageFileUri);
                    } catch (FileNotFoundException fnfe) {
                        Log.e(TAG, "could not get file from uri " + fnfe.getMessage(), fnfe);
                    }
                    taskImageView.setImageBitmap(BitmapFactory.decodeStream(selectedImageInputStreamUpload));
                },
                failure ->
                {
                    Log.i(TAG, "could not upload file to S3. Fileman: " + selectedImageFileName + "with error: " + failure.getMessage());
                }
        );
    }

    private void setUpSpinners() {
        teamNameSpinner = (Spinner) findViewById(R.id.spinnerTeamNameAddTaskActivity);
        teamNames = new ArrayList<>();
        teamNameSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                State.values()));

        taskStateSpinner = (Spinner) findViewById(R.id.spinnerTaskStateAddTaskActivity);
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
                    for (Team team : success.getData()) {
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

    private void saveNewTaskButton() {
        Button submitButton = (Button) findViewById(R.id.addTaskButton);
        submitButton.setOnClickListener(view ->
                {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        Log.e(TAG, "Application does not have access to ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION");
                        return;
                    }

                    String name = ((EditText) findViewById(R.id.editTextTaskNameAddTaskActivity)).getText().toString();
                    String body = ((EditText) findViewById(R.id.editTextTaskDescriptionAddTaskActivity)).getText().toString();
                    String currentDateString = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());
                    String selectedNameString = teamNameSpinner.getSelectedItem().toString();

                    List<Team> teams = null;
                    try {
                        teams = teamsFuture.get();
                    } catch (InterruptedException ie) {
                        Log.e(TAG, "InterruptedException while getting teams");
                        Thread.currentThread().interrupt();
                    } catch (ExecutionException ee) {
                        Log.e(TAG, "ExecutionException while getting teams");
                    }

                    Team selectedTeam = teams.stream().filter(t -> t.getTeamName().equals(selectedNameString)).findAny().orElseThrow(RuntimeException::new);

                    locationProviderClient.getLastLocation().addOnSuccessListener(location ->
                            {
                                if (location == null) {
                                    Log.e(TAG, "Location callback was null");
                                }
                                String currentLatitude = Double.toString(location.getLatitude());
                                String currentLongitude = Double.toString(location.getLongitude());
                                Log.i(TAG, "Longitude: " + location.getLongitude());
                                saveTask(name, body, currentDateString, selectedTeam, currentLatitude, currentLongitude);
                            }
                    ).addOnCanceledListener(() -> {
                        Log.e(TAG, "location request was canceled");})
                            .addOnFailureListener(failure -> {
                                Log.e(TAG, "location request failed. Error was: " + failure.getMessage(), failure.getCause());})
                            .addOnCompleteListener(complete ->
                            {
                                Log.e(TAG, "Location request completed!");
                            });
                });

//            Task newTask = Task.builder()
//                    .name(name)
//                    .body(body)
//                    .dateCreated(new Temporal.DateTime(currentDataString))
//                    .state((State) taskStateSpinner.getSelectedItem())
//                    .team(selectedTeam)
//                    .taskLatitude(currentLatitude)
//                    .taskLongitude(currentLongitude)
//                    .taskImageS3Key(imageS3Key)
//                    .build();
//
//            Amplify.API.mutate(
//                    ModelMutation.create(newTask),
//                    successResponse -> Log.i(TAG, "AddTaskActivity.onCreate(): made a task successfully"),
//                    failureResponse -> Log.i(TAG, "AddTaskActivity.onCreate(): failed with this response: " + failureResponse)
//            );
            submitButton.onEditorAction(EditorInfo.IME_ACTION_DONE);
//            Toast.makeText(AddTaskActivity.this, "Task Saved!", Toast.LENGTH_SHORT).show();
//        });
    }


    private void saveTask(String name, String body, String currentDateString, Team selectedTeam, String latitude, String longitude)
    {
        Task newTask = Task.builder()
                .name(name)
                .body(body)
                .dateCreated(new Temporal.DateTime(currentDateString))
                .state((State) taskStateSpinner.getSelectedItem())
                .team(selectedTeam)
                .taskLatitude(latitude)
                .taskLongitude(longitude)
                .taskImageS3Key(imageS3Key)
                .build();

        Amplify.API.mutate(
                ModelMutation.create(newTask),
                successResponse -> Log.i(TAG, "AddTaskActivity.onCreate(): made a task successfully"),
                failureResponse -> Log.i(TAG, "AddTaskActivity.onCreate(): failed with this response: " + failureResponse)
        );
//        submitButton.onEditorAction(EditorInfo.IME_ACTION_DONE);
        Toast.makeText(AddTaskActivity.this, "Task Saved!", Toast.LENGTH_SHORT).show();
    }

    private void setUpVisibilityImageButtons()
    {
        Button addImageButton = (Button) findViewById(R.id.buttonAddImageAddTaskActivity);
        Button deleteImageButton = (Button) findViewById(R.id.buttonDeleteImageAddTaskActivity);

        if (imageS3Key.isEmpty())
        {
            runOnUiThread(() ->
            {
                deleteImageButton.setVisibility(View.INVISIBLE);
                addImageButton.setVisibility(View.VISIBLE);
            });
        }
        else
        {
            runOnUiThread(() ->
            {
                deleteImageButton.setVisibility(View.VISIBLE);
                addImageButton.setVisibility(View.INVISIBLE);
            });
        }
    }

    private void setUpSaveImageButton()
    {
        Button addImageButton = (Button) findViewById(R.id.buttonAddImageAddTaskActivity);
        addImageButton.setOnClickListener(b->
        {
            launchImageSelectedIntent();
        });
    }

    private void setUpDeleteImageButton()
    {
        Button deleteImageButton = (Button) findViewById(R.id.buttonDeleteImageAddTaskActivity);
        deleteImageButton.setOnClickListener(d ->
        {
            deleteImageFromS3();
        });
    }

    private void deleteImageFromS3()
    {
        if(!imageS3Key.isEmpty())
        {
            Amplify.Storage.remove(
                    imageS3Key,
                    success ->
                    {
                        imageS3Key = "";
                        ImageView taskImageView = findViewById(R.id.imageViewAddTaskActivity);
                        runOnUiThread(() ->
                        {
                            taskImageView.setImageResource(android.R.color.transparent);
                        });
                        setUpVisibilityImageButtons();
                        Log.i(TAG, "Succeeded in deleting file on S3. Key is: " + success.getKey());
                    },
                    failure ->
                    {
                        Log.e(TAG, "Failure in deleting file on S3. Key is: " + imageS3Key + "with error: " + failure.getMessage());
                    }
            );
        }
    }

    private void launchImageSelectedIntent()
    {
        Intent imageFileSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFileSelectionIntent.setType("*/*");
        imageFileSelectionIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"});
        activityResultLauncher.launch(imageFileSelectionIntent);
    }

    private ActivityResultLauncher<Intent> getImageSelectedActivityResultLauncher()
    {
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
                                            uploadInputStreamToS3(selectedImageInputStream, selectedImageFilename, selectedImageFileUri);
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

    private void uploadInputStreamToS3(InputStream selectedImageInputStream, String selectedImageFilename, Uri selectedImageFileUri)
    {
        Amplify.Storage.uploadInputStream(
                selectedImageFilename,
                selectedImageInputStream,
                success ->
                {
                    imageS3Key = success.getKey();
                    setUpVisibilityImageButtons();
                    saveNewTaskButton();
                    ImageView taskImageView = findViewById(R.id.imageViewAddTaskActivity);
                    Log.i(TAG, "Succeeding in getting file uploaded to S3. key is: " + success.getKey());
                    InputStream selectImageInputStreamUpload = null;
                    try
                    {
                        selectImageInputStreamUpload = getContentResolver().openInputStream(selectedImageFileUri);
                    } catch (FileNotFoundException fnfe)
                    {
                        Log.e(TAG, "Could not get file from Uri. " + fnfe.getMessage(), fnfe);
                    }
                    taskImageView.setImageBitmap(BitmapFactory.decodeStream(selectImageInputStreamUpload));
                },
                failure ->
                {
                    Log.i(TAG, "Failure in uploading file to S3. Filename: " + selectedImageFilename + "with error: " + failure.getMessage());
                }
        );
    }

    private void saveUpdatedTaskToDb()
    {
        System.out.println("save to database");
    }
}