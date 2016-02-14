package com.hitherejoe.androidtvboilerplate.injection.component;

import android.app.Application;
import android.content.Context;

import com.hitherejoe.androidtvboilerplate.data.DataManager;
import com.hitherejoe.androidtvboilerplate.data.local.PreferencesHelper;
import com.hitherejoe.androidtvboilerplate.injection.ApplicationContext;
import com.hitherejoe.androidtvboilerplate.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;
import rx.subscriptions.CompositeSubscription;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();
    Application application();
    PreferencesHelper preferencesHelper();
    DataManager dataManager();
    CompositeSubscription compositeSubscription();

}