package com.hitherejoe.androidtvboilerplate.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.hitherejoe.androidtvboilerplate.injection.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesHelper {

    private final SharedPreferences mPref;

    public static final String PREF_FILE_NAME = "tv_boilerplate_pref_file";
    private static final String PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN";

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        mPref.edit().clear().apply();
    }

    public void putAccessToken(String accessToken) {
        mPref.edit().putString(PREF_KEY_ACCESS_TOKEN, accessToken).apply();
    }

    @Nullable
    public String getAccessToken() {
        return mPref.getString(PREF_KEY_ACCESS_TOKEN, null);
    }

}