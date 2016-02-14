package com.hitherejoe.androidtvboilerplate.ui.content;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.hitherejoe.androidtvboilerplate.R;
import com.hitherejoe.androidtvboilerplate.ui.base.BaseActivity;
import com.hitherejoe.androidtvboilerplate.ui.search.SearchContentActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContentActivity extends BaseActivity {

    @Bind(R.id.frame_container) FrameLayout mFragmentContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getFragmentManager().beginTransaction()
                .replace(mFragmentContainer.getId(), ContentFragment.newInstance()).commit();
    }

    @Override
    public boolean onSearchRequested() {
        startActivity(SearchContentActivity.getStartIntent(this));
        return true;
    }

}