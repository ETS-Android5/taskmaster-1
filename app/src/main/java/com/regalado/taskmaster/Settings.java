package com.regalado.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    // create a string for logging
    public String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button buttonToSaveUsername = (Button) findViewById(R.id.saveUserNameButton);
        TextView displaySavedUsernamme = (TextView) findViewById(R.id.displaySavedUsername);

        buttonToSaveUsername.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                System.out.println("saved!");
                Log.e(TAG, "Logging");

                ((TextView)findViewById(R.id.displaySavedUsername)).setText(R.string.submitted);

            }
        });
    }
}