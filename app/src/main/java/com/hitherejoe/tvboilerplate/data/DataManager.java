package com.hitherejoe.tvboilerplate.data;

import android.content.Context;
import android.content.res.Resources;

import com.hitherejoe.tvboilerplate.R;
import com.hitherejoe.tvboilerplate.data.local.PreferencesHelper;
import com.hitherejoe.tvboilerplate.data.model.Cat;
import com.hitherejoe.tvboilerplate.data.remote.BoilerplateService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Single;
import rx.SingleSubscriber;

@Singleton
public class DataManager {

    private final BoilerplateService mTvBoilerplateService;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper, BoilerplateService boilerplateService) {
        mPreferencesHelper = preferencesHelper;
        mTvBoilerplateService = boilerplateService;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Single<List<Cat>> getCats(final Context context) {
        return Single.create(new Single.OnSubscribe<List<Cat>>() {
            @Override
            public void call(SingleSubscriber<? super List<Cat>> singleSubscriber) {
                Resources resources = context.getResources();
                String[] names = resources.getStringArray(R.array.cat_names);
                String[] descriptions = resources.getStringArray(R.array.cat_descriptions);

                List<Cat> cats = new ArrayList<>();
                for (int i = 0; i < names.length; i++) {
                    cats.add(new Cat(names[i], descriptions[i]));
                }
                singleSubscriber.onSuccess(cats);
            }
        });
    }

}
