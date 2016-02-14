package com.hitherejoe.androidtvboilerplate.injection.component;

import com.hitherejoe.androidtvboilerplate.injection.PerActivity;
import com.hitherejoe.androidtvboilerplate.injection.module.ActivityModule;
import com.hitherejoe.androidtvboilerplate.ui.content.ContentFragment;
import com.hitherejoe.androidtvboilerplate.ui.search.SearchContentFragment;

import dagger.Component;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(ContentFragment contentFragment);
    void inject(SearchContentFragment searchContentFragment);

}