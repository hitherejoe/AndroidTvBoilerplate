package com.hitherejoe.tvboilerplate.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.hitherejoe.tvboilerplate.TvBoilerplateApplication;
import com.hitherejoe.tvboilerplate.injection.component.ActivityComponent;
import com.hitherejoe.tvboilerplate.injection.component.DaggerActivityComponent;
import com.hitherejoe.tvboilerplate.injection.module.ActivityModule;

public class BaseActivity extends Activity {

    private ActivityComponent mActivityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ActivityComponent getActivityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(TvBoilerplateApplication.get(this).getComponent())
                    .build();
        }
        return mActivityComponent;
    }

}
