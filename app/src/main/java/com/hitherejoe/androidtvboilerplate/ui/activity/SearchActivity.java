package com.hitherejoe.androidtvboilerplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hitherejoe.androidtvboilerplate.R;
import com.hitherejoe.androidtvboilerplate.ui.fragment.SearchFragment;

public class SearchActivity extends BaseActivity {

    private SearchFragment mSearchFragment;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchFragment =
                (SearchFragment) getFragmentManager().findFragmentById(R.id.search_fragment);
    }

    @Override
    public boolean onSearchRequested() {
        if (mSearchFragment.hasResults()) {
            startActivity(new Intent(this, SearchActivity.class));
        } else {
            mSearchFragment.startRecognition();
        }
        return true;
    }

}