package com.hitherejoe.androidtvboilerplate.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.hitherejoe.androidtvboilerplate.AndroidTvBoilerplateApplication;
import com.hitherejoe.androidtvboilerplate.injection.component.ActivityComponent;
import com.hitherejoe.androidtvboilerplate.injection.component.DaggerActivityComponent;
import com.hitherejoe.androidtvboilerplate.injection.module.ActivityModule;

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
                    .applicationComponent(AndroidTvBoilerplateApplication.get(this).getComponent())
                    .build();
        }
        return mActivityComponent;
    }

}
