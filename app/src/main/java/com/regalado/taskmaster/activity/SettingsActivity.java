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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.State;
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
    Spinner teamNameSpinner = null;
    List<Team> teams;
    List<String> teamListString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        teamsFuture = new CompletableFuture<>();

        setUserNickname();
        setTeamName();
        saveUserNickname();
        setupTeamNameSpinner();
    }

    public void setupTeamNameSpinner()
    {
        teamNameSpinner = findViewById(R.id.spinnerSelectTeamNameSettingsActivity);
        teamNameSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                State.values()));

//        teamsFuture = new CompletableFuture<>();
        teams = new ArrayList<>();
        teamListString = new ArrayList<>();

        Amplify.API.query(
                ModelQuery.list(Team.class),
                success ->
                {
                    Log.i(TAG, "Read teams successfully");
                    for (Team team : success.getData()) {
                        teams.add(team);
                        teamListString.add(team.getTeamName());
                    }

                    teamsFuture.complete(teams);

                    runOnUiThread(() ->
                    {
//                        teamListString.add("All");
//                        for (Team team : teams)
//                            teamListString.add(team.getTeamName());
                        teamNameSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                teamListString));
                        if (!teams.isEmpty()) {
                            teamNameSpinner.setSelection(teamListString.indexOf(teamListString));
                        }

                    });
                },
                failure ->
                {
                    teamsFuture.complete(null);
                    Log.i(TAG, "did not read team names successfully");
                }
        );
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

    public void setTeamName()
    {
        String teamName = preferences.getString(USER_TEAM_TAG, "");
        if(!teamName.isEmpty())
        {
            Spinner teamNameSpinner = findViewById(R.id.spinnerSelectTeamNameSettingsActivity);
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

                String teamNameSelected = teamNameSpinner.getSelectedItem().toString();
                preferencesEditor.putString(USER_TEAM_TAG, teamNameSelected);

                String userNicknameString = userNameEditText.getText().toString();
                preferencesEditor.putString(USER_NAME_TAG, userNicknameString);
                preferencesEditor.apply();

//                Snackbar.make(findViewById(R.id.textViewSavedSettingsActivity), "Saved!", Snackbar.LENGTH_SHORT).show();
                Toast.makeText(SettingsActivity.this, "Log out failed!", Toast.LENGTH_SHORT).show();
                buttonToSaveUsername.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });
    }
}