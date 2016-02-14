package com.hitherejoe.androidtvboilerplate.injection.module;

import android.app.Application;
import android.content.Context;

import com.hitherejoe.androidtvboilerplate.data.remote.AndroidTvBoilerplateService;
import com.hitherejoe.androidtvboilerplate.injection.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Provide application-level dependencies. Mainly singleton object that can be injected from
 * anywhere in the app.
 */
@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @Singleton
    AndroidTvBoilerplateService provideVineyardService() {
        return AndroidTvBoilerplateService.Creator.newVineyardService();
    }
}