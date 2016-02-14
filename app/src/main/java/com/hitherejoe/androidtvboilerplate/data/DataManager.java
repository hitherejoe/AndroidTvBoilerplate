package com.hitherejoe.androidtvboilerplate.data;

import com.hitherejoe.androidtvboilerplate.data.local.PreferencesHelper;
import com.hitherejoe.androidtvboilerplate.data.model.Cat;
import com.hitherejoe.androidtvboilerplate.data.remote.AndroidTvBoilerplateService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Single;

@Singleton
public class DataManager {

    private final AndroidTvBoilerplateService mTvAndroidTvBoilerplateService;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper,
                       AndroidTvBoilerplateService androidTvBoilerplateService) {
        mPreferencesHelper = preferencesHelper;
        mTvAndroidTvBoilerplateService = androidTvBoilerplateService;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Single<List<Cat>> getCats(List<Cat> cats) {
        // This just for example, usually here we'd make an API request and not pass a useless
        // list of cats back that we passed in!
        return Single.just(cats);
    }

}
