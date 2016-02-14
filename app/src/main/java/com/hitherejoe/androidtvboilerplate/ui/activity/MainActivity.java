package com.hitherejoe.androidtvboilerplate.ui.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v17.leanback.app.ErrorFragment;
import android.view.View;
import android.widget.FrameLayout;

import com.hitherejoe.androidtvboilerplate.R;
import com.hitherejoe.androidtvboilerplate.ui.fragment.MainFragment;
import com.hitherejoe.androidtvboilerplate.util.NetworkUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.frame_container) FrameLayout mFragmentContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Fragment mFragment;

        if (NetworkUtil.isNetworkConnected(this)) {
            mFragment = MainFragment.newInstance();
        } else {
            mFragment = buildErrorFragment();
        }
        getFragmentManager().beginTransaction()
                .replace(mFragmentContainer.getId(), mFragment).commit();
    }

    @Override
    public boolean onSearchRequested() {
        // Start search activity
        return true;
    }

    private ErrorFragment buildErrorFragment() {
        ErrorFragment errorFragment = new ErrorFragment();
        errorFragment.setTitle(getString(R.string.text_error_oops_title));
        errorFragment.setMessage(getString(R.string.error_message_network_needed_app));
        errorFragment.setButtonText(getString(R.string.text_close));
        errorFragment.setButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return errorFragment;
    }

}
