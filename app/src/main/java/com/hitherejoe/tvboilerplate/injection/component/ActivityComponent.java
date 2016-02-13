package com.hitherejoe.tvboilerplate.injection.component;

import com.hitherejoe.tvboilerplate.injection.PerActivity;
import com.hitherejoe.tvboilerplate.injection.module.ActivityModule;
import com.hitherejoe.tvboilerplate.ui.activity.MainActivity;
import com.hitherejoe.tvboilerplate.ui.fragment.MainFragment;

import dagger.Component;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);
    void inject(MainFragment mainFragment);

}