package com.regalado.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.google.android.material.snackbar.Snackbar;
import com.regalado.taskmaster.R;

import java.util.prefs.Preferences;

public class SettingsActivity extends AppCompatActivity {

    // create a string for logging
    public String TAG = "SettingsActivity";
    public static final String USER_NAME_TAG = "userNickname";
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // need to set preferences outside of the onClick() function because of the "this" context.
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setUserNickname();
        saveUserNickname();

    }

    public void setUserNickname()
    {
        String userNickname = preferences.getString(USER_NAME_TAG, "");
        if(!userNickname.isEmpty())
        {
            EditText userNameEditText = (EditText) findViewById(R.id.editTextUsernameSettingsActivity);
            userNameEditText.setText(userNickname);
        }
    }

    public void saveUserNickname()
    {
        Button buttonToSaveUsername = (Button) findViewById(R.id.buttonSaveUsernameSettingsActivity);
        buttonToSaveUsername.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                System.out.println("saved!");
                Log.e(TAG, "Logging");

                // save username from edit text box, which gets passed to MainActivity
                SharedPreferences.Editor preferencesEditor = preferences.edit();
                EditText userNameEditText = (EditText) findViewById(R.id.editTextUsernameSettingsActivity);
                String userNicknameString = userNameEditText.getText().toString();
                preferencesEditor.putString(USER_NAME_TAG, userNicknameString);
                preferencesEditor.apply();
//                ((TextView)findViewById(R.id.textViewSavedSettingsActivity)).setText(R.string.save);
                Snackbar.make(findViewById(R.id.textViewSavedSettingsActivity), "Saved!", Snackbar.LENGTH_SHORT).show();

            }
        });
    }
}