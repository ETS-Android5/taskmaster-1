package com.regalado.taskmaster.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.predictions.models.LanguageType;
import com.regalado.taskmaster.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TaskDetailActivity extends AppCompatActivity {

    SharedPreferences preferences;
    public final String TAG = "TaskDetailActivity";
    public String taskLat = "";
    public String taskLong = "";
    public String taskTitle = "";

    private MediaPlayer mp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        setUpTextToSpeakButton();

        mp = new MediaPlayer();
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

        public void setUpTextToSpeakButton()
    {
        Button voiceButton = (Button) findViewById(R.id.buttonTextToSpeechTaskDetailActivity);
        voiceButton.setOnClickListener(b ->
        {
            String textToSpeakDescription = ((TextView)findViewById(R.id.textViewTaskBodyTaskDetailActivity)).getText().toString();
            Amplify.Predictions.convertTextToSpeech(
                    textToSpeakDescription,
                    result -> playAudio(result.getAudioData()),
                    error -> Log.e(TAG, "Conversion failed", error)
            );
            Amplify.Predictions.translateText(
                    textToSpeakDescription, LanguageType.ENGLISH, LanguageType.KOREAN,
                    result -> Log.i(TAG, result.getTranslatedText()),
                    error -> Log.e(TAG, "Translation failed", error)
            );

        });
    }

    private void playAudio(InputStream data) {
        File mp3File = new File(getCacheDir(), "audio.mp3");

        try (OutputStream out = new FileOutputStream(mp3File)) {
            byte[] buffer = new byte[8 * 1_024];
            int bytesRead;
            while ((bytesRead = data.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            mp.reset();
            mp.setOnPreparedListener(MediaPlayer::start);
            mp.setDataSource(new FileInputStream(mp3File).getFD());
            mp.prepareAsync();
        } catch (IOException error) {
            Log.e(TAG, "Error writing audio file", error);
        }
    }
}