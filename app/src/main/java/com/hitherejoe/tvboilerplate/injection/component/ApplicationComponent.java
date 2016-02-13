package com.hitherejoe.tvboilerplate.injection.component;

import android.app.Application;
import android.content.Context;

import com.hitherejoe.tvboilerplate.data.DataManager;
import com.hitherejoe.tvboilerplate.data.local.PreferencesHelper;
import com.hitherejoe.tvboilerplate.injection.ApplicationContext;
import com.hitherejoe.tvboilerplate.injection.module.ApplicationModule;

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