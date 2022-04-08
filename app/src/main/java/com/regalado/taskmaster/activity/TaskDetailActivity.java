package com.regalado.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;
import com.regalado.taskmaster.R;

import org.w3c.dom.Text;

import java.io.File;

public class TaskDetailActivity extends AppCompatActivity {

    SharedPreferences preferences;
    public final String TAG = "TaskDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent callingIntent = getIntent();
        String taskTitle = null;
        String taskBody = null;
        String taskState = null;
        String imageS3Key = "";

        if(callingIntent != null)
        {
            taskTitle = callingIntent.getStringExtra(MainActivity.TASK_TITLE_TAG);
            taskBody = callingIntent.getStringExtra(MainActivity.TASK_BODY_TAG);
            taskState = callingIntent.getStringExtra(MainActivity.TASK_STATE_TAG);
            imageS3Key = callingIntent.getStringExtra(MainActivity.TASK_IMAGE_TAG);
        }

        TextView taskDetailTitle = (TextView) findViewById(R.id.textViewTaskTitleDetailsActivity);
        TextView taskDetailBody = (TextView) findViewById(R.id.textViewTaskBodyTaskDetailActivity);
        TextView taskDetailState = (TextView) findViewById(R.id.textViewTaskStateTaskDetailActivity);

        if(taskTitle != null)
        {
            taskDetailTitle.setText(taskTitle);
        }
        else
        {
            taskDetailTitle.setText(R.string.no_task);
        }

        taskDetailBody.setText(taskBody);
        taskDetailState.setText(taskState);

        if (imageS3Key != null && !imageS3Key.isEmpty())
        {
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