package com.hitherejoe.tvboilerplate.data.recommendations;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;

import timber.log.Timber;

/*
 * This class builds up to MAX_RECOMMENDATIONS of ContentRecommendations and defines what happens
 * when they're selected from Recommendations section on the Home screen by creating an Intent.
 */
public class UpdateRecommendationsService extends IntentService {
    private static final String TAG = "UpdateRecommendationsService";
    private static final int MAX_RECOMMENDATIONS = 3;

    private NotificationManager mNotificationManager;

    public UpdateRecommendationsService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Timber.i("Retrieving popular posts for recommendations...");
        // fetch recommendations
    }

}