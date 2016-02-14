package com.hitherejoe.androidtvboilerplate.ui.fragment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hitherejoe.androidtvboilerplate.R;
import com.hitherejoe.androidtvboilerplate.data.DataManager;
import com.hitherejoe.androidtvboilerplate.data.model.Cat;
import com.hitherejoe.androidtvboilerplate.ui.activity.BaseActivity;
import com.hitherejoe.androidtvboilerplate.ui.presenter.CardPresenter;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MainFragment extends BrowseFragment {

    private static final int BACKGROUND_UPDATE_DELAY = 300;

    @Inject DataManager mDataManager;

    private ArrayObjectAdapter mRowsAdapter;
    private BackgroundManager mBackgroundManager;
    private DisplayMetrics mMetrics;
    private Drawable mDefaultBackground;
    private Handler mHandler;
    private Runnable mBackgroundRunnable;
    private Subscription mSubscription;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        mHandler = new Handler();

        loadPosts();
        setAdapter(mRowsAdapter);
        prepareBackgroundManager();
        setupUIElements();
        setupListeners();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBackgroundRunnable != null) {
            mHandler.removeCallbacks(mBackgroundRunnable);
            mBackgroundRunnable = null;
        }
        mBackgroundManager = null;
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBackgroundManager.release();
    }

    protected void updateBackground(String uri) {
        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;
        Glide.with(getActivity())
                .load(uri)
                .asBitmap()
                .centerCrop()
                .error(mDefaultBackground)
                .into(new SimpleTarget<Bitmap>(width, height) {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap>
                                                        glideAnimation) {
                        mBackgroundManager.setBitmap(resource);
                    }
                });
        if (mBackgroundRunnable != null) mHandler.removeCallbacks(mBackgroundRunnable);
    }

    private void setupUIElements() {
        setBadgeDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.banner_browse));
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.primary));
        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.accent));
    }

    private void setupListeners() {
        setOnItemViewClickedListener(mOnItemViewClickedListener);
        setOnItemViewSelectedListener(mOnItemViewSelectedListener);

        setOnSearchClickedListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // start search activity
            }
        });
    }

    private void loadPosts() {
        final ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
        HeaderItem header = new HeaderItem(0, getString(R.string.header_title_cats));
        mRowsAdapter.add(new ListRow(header, listRowAdapter));

        // Usually we'd load things from an API or database, for example here we just create
        // a list of cats from resources and return them back after passing them to the datamanager.
        // Obviously we wouldn't usually do this, but this is just for example and allows us
        // to still have an example unit test that doesn't require robolectric!
        Resources resources = getResources();
        String[] names = resources.getStringArray(R.array.cat_names);
        String[] descriptions = resources.getStringArray(R.array.cat_descriptions);
        String[] images = resources.getStringArray(R.array.cat_images);

        List<Cat> cats = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            cats.add(new Cat(names[i], descriptions[i], images[i]));
        }

        mSubscription = mDataManager.getCats(cats)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<List<Cat>>() {
                    @Override
                    public void onSuccess(List<Cat> cats) {
                        listRowAdapter.addAll(0, cats);
                    }

                    @Override
                    public void onError(Throwable error) {
                        Timber.e(error, "There was an error loading the cats!");
                    }
                });
    }


    private void prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mDefaultBackground =
                new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.primary_light));
        mBackgroundManager.setColor(ContextCompat.getColor(getActivity(), R.color.primary_light));
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void startBackgroundTimer(final URI backgroundURI) {
        if (mBackgroundRunnable != null) mHandler.removeCallbacks(mBackgroundRunnable);
        mBackgroundRunnable = new Runnable() {
            @Override
            public void run() {
                if (backgroundURI != null) updateBackground(backgroundURI.toString());
            }
        };
        mHandler.postDelayed(mBackgroundRunnable, BACKGROUND_UPDATE_DELAY);
    }

    private OnItemViewClickedListener mOnItemViewClickedListener = new OnItemViewClickedListener() {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            // respond to item clicks
        }
    };

    private OnItemViewSelectedListener mOnItemViewSelectedListener =
            new OnItemViewSelectedListener() {
                @Override
                public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                           RowPresenter.ViewHolder rowViewHolder, Row row) {
                    // respond to item selection
                    if (item instanceof Cat) {
                        Cat cat = (Cat) item;
                        String backgroundUrl = cat.imageUrl;
                        if (backgroundUrl != null) startBackgroundTimer(URI.create(backgroundUrl));
                    }
                }
            };

}