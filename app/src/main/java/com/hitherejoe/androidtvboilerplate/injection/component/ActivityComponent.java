package com.hitherejoe.androidtvboilerplate.injection.component;

import com.hitherejoe.androidtvboilerplate.injection.PerActivity;
import com.hitherejoe.androidtvboilerplate.injection.module.ActivityModule;
import com.hitherejoe.androidtvboilerplate.ui.activity.MainActivity;
import com.hitherejoe.androidtvboilerplate.ui.fragment.MainFragment;
import com.hitherejoe.androidtvboilerplate.ui.fragment.SearchFragment;

import dagger.Component;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);
    void inject(MainFragment mainFragment);
    void inject(SearchFragment searchFragment);

}