package com.regalado.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.snackbar.Snackbar;
import com.regalado.taskmaster.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.prefs.Preferences;

public class SettingsActivity extends AppCompatActivity {


    public String TAG = "SettingsActivity";
    public static final String USER_NAME_TAG = "userNickname";
    public static final String USER_TEAM_TAG = "teamName";
    SharedPreferences preferences;
    CompletableFuture<List<Team>> teamsFuture = null;
    List<Team> teams = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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

                SharedPreferences.Editor preferencesEditor = preferences.edit();
                EditText userNameEditText = (EditText) findViewById(R.id.editTextUsernameSettingsActivity);
                String userNicknameString = userNameEditText.getText().toString();
                preferencesEditor.putString(USER_NAME_TAG, userNicknameString);
                preferencesEditor.apply();

                Snackbar.make(findViewById(R.id.textViewSavedSettingsActivity), "Saved!", Snackbar.LENGTH_SHORT).show();
                buttonToSaveUsername.onEditorAction(EditorInfo.IME_ACTION_DONE);


            }
        });
    }
}