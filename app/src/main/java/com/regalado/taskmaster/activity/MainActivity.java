package com.regalado.taskmaster.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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


import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
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
    public static final String TASK_LAT_TAG = "LATITUDE";
    public static final String TASK_LONG_TAG = "LONGITUDE";

    SharedPreferences preferences;

    TaskListRecyclerViewAdapter myTasksListRecyclerviewAdapter;
    List<Task> taskArrayList = null;
    CompletableFuture<List<String>> teamNamesFuture = null;
    private InterstitialAd mInterstitialAd = null;
    private RewardedAd mRewardedAd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        ///// HARD CODING TEAMS ///////////////////

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
        setupAds();

        // Analytics
        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("openedApp")
                .addProperty("timeOpened", Long.toString(new Date().getTime()))
                .addProperty("evenDescription", "Opened MainActivity")
                .build();

        Amplify.Analytics.recordEvent(event);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        taskListRecyclerView();
        filterTaskListFromDatabase();
        handleLoginAndLogoutButtonVisibility();

        // Analytics
        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("resumedApp")
                .addProperty("timeResumed", Long.toString(new Date().getTime()))
                .addProperty("evenDescription", "Resumed MainActivity")
                .build();

        Amplify.Analytics.recordEvent(event);
    }

    private void setupAds()
    {
        TextView rewardTextView = (TextView) findViewById(R.id.textViewRewardMainActivity);
        // Banner ad
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView bannerAdView = findViewById(R.id.bannerAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAdView.loadAd(adRequest);

        // Interstitial ad
        AdRequest adRequestInterstitial = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequestInterstitial,
            new InterstitialAdLoadCallback()
            {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd)
                {
                    mInterstitialAd = interstitialAd;
                    Log.i(TAG, "onAdLoaded");
                }
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError)
                {
                    Log.i(TAG, loadAdError.getMessage());
                    mInterstitialAd = null;
                }
            });

        Button interstitialAdButton = (Button) findViewById(R.id.buttonInterstitialAdMainActivity);
        interstitialAdButton.setOnClickListener(b ->
        {
           if(mInterstitialAd != null)
           {
               mInterstitialAd.show(MainActivity.this);
           }
           else
           {
               Log.d(TAG, "The interstitial ad wasn't ready yet.");
           }
        });

        // Rewarded ad
        AdRequest rewardedAdRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                rewardedAdRequest, new RewardedAdLoadCallback()
                {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError)
                {
                    Log.d(TAG, loadAdError.getMessage());
                    mRewardedAd = null;
                }

                @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd)
                    {
                    mRewardedAd = rewardedAd;
                    Log.d(TAG, "Ad was loaded.");
                    mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback()
                    {
                        @Override
                        public void onAdShowedFullScreenContent()
                        {
                            // Called when ad is shown.
                            Log.d(TAG, "Ad was shown.");
                            runOnUiThread(() -> Toast.makeText(MainActivity.this, "Ad was shown!", Toast.LENGTH_SHORT).show());
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError)
                        {
                            // Called when ad fails to show.
                            Log.d(TAG, "Ad failed to show.");
                        }

                        @Override
                        public void onAdDismissedFullScreenContent()
                        {
                            // Called when ad is dismissed.
                            // Set the ad reference to null so you don't show the ad a second time.
                            Log.d(TAG, "Ad was dismissed.");
                            mRewardedAd = null;
                        }
                    });
                   }
                });
        Button rewardedAdButton = (Button) findViewById(R.id.buttonRewardAdMainActivity);
        rewardedAdButton.setOnClickListener(b ->
        {
            if (mRewardedAd != null)
            {
                mRewardedAd.show(MainActivity.this, new OnUserEarnedRewardListener()
                {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem)
                    {
                        // Handle the reward.
                        Log.d(TAG, "The user earned the reward.");
                        int rewardAmount = rewardItem.getAmount();
                        String rewardType = rewardItem.getType();
                        Log.d(TAG, "The user earned the reward. Amount is: " + rewardAmount + ", and type is: " + rewardType);
                        rewardTextView.setText("Amount: " + rewardAmount +" " + "Type: " + rewardType);
                    }
                });
            }
            else
            {
                Log.d(TAG, "The rewarded ad wasn't ready yet.");
            }
        });
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
        else
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