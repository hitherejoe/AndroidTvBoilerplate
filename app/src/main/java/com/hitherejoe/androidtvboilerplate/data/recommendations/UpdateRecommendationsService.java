package com.hitherejoe.androidtvboilerplate.data.recommendations;

import android.app.IntentService;
import android.content.Intent;

import timber.log.Timber;

public class UpdateRecommendationsService extends IntentService {
    private static final String TAG = "UpdateRecommendationsService";
    private static final int MAX_RECOMMENDATIONS = 3;

    public UpdateRecommendationsService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Timber.i("Retrieving popular posts for recommendations...");
        // fetch and add recommendations
    }

}