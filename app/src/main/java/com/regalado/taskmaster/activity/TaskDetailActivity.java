package com.regalado.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Task;
import com.regalado.taskmaster.R;

import org.w3c.dom.Text;

import java.io.File;

public class TaskDetailActivity extends AppCompatActivity {

    SharedPreferences preferences;
    public final String TAG = "TaskDetailActivity";
    public String taskLat = "";
    public String taskLong = "";
    public String taskTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Intent callingIntent = getIntent();
        String taskTitle = null;
        String taskBody = null;
        String taskState = null;
        String imageS3Key = null;;
        String taskLat = null;
        String taskLong = null;

        if (callingIntent != null) {
            taskTitle = callingIntent.getStringExtra(MainActivity.TASK_TITLE_TAG);
            taskBody = callingIntent.getStringExtra(MainActivity.TASK_BODY_TAG);
            taskState = callingIntent.getStringExtra(MainActivity.TASK_STATE_TAG);
            imageS3Key = callingIntent.getStringExtra(MainActivity.TASK_IMAGE_TAG);
            taskLat = callingIntent.getStringExtra(MainActivity.TASK_LAT_TAG);
            taskLong = callingIntent.getStringExtra(MainActivity.TASK_LONG_TAG);
        }

        TextView taskDetailTitle = (TextView) findViewById(R.id.textViewTaskTitleDetailsActivity);
        TextView taskDetailBody = (TextView) findViewById(R.id.textViewTaskBodyTaskDetailActivity);
        TextView taskDetailState = (TextView) findViewById(R.id.textViewTaskStateTaskDetailActivity);
        TextView taskDetailLat = (TextView) findViewById(R.id.textViewLatitudeTaskDetailActivity);
        TextView taskDetailLong = (TextView) findViewById(R.id.textViewLongitudeTaskDetailActivity);

        if (taskTitle != null) {
            taskDetailTitle.setText(taskTitle);
            taskDetailLat.setText(taskLat);
            taskDetailLong.setText(taskLong);


        } else {
            taskDetailTitle.setText(R.string.no_task);
        }

        taskDetailBody.setText(taskBody);
        taskDetailState.setText(taskState);
        taskDetailLat.setText(taskLat);
        taskDetailLong.setText(taskLong);


        if (imageS3Key != null && !imageS3Key.isEmpty()) {
            String finalImageS3Key = imageS3Key;
            Amplify.Storage.downloadFile(
                    imageS3Key,
                    new File(getApplication().getFilesDir(), imageS3Key),
                    success ->
                    {
                        ImageView viewTaskImageUpload = findViewById(R.id.imageViewTaskDetail);
                        viewTaskImageUpload.setImageBitmap(BitmapFactory.decodeFile(success.getFile().getPath()));
                    },
                    failure ->
                    {
                        Log.e(TAG, "Unable to get image from S3 for: " + finalImageS3Key);
                    }
            );
        }
    }
}