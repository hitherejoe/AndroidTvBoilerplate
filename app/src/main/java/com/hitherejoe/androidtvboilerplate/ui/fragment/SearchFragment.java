package com.hitherejoe.androidtvboilerplate.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.SpeechRecognitionCallback;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hitherejoe.androidtvboilerplate.R;
import com.hitherejoe.androidtvboilerplate.data.DataManager;
import com.hitherejoe.androidtvboilerplate.data.model.Cat;
import com.hitherejoe.androidtvboilerplate.ui.activity.BaseActivity;
import com.hitherejoe.androidtvboilerplate.ui.presenter.CardPresenter;
import com.hitherejoe.androidtvboilerplate.util.NetworkUtil;
import com.hitherejoe.androidtvboilerplate.util.ToastFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class SearchFragment extends android.support.v17.leanback.app.SearchFragment
        implements android.support.v17.leanback.app.SearchFragment.SearchResultProvider {

    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int REQUEST_SPEECH = 0x00000010;

    @Inject DataManager mDataManager;

    private ArrayObjectAdapter mResultsAdapter;
    private BackgroundManager mBackgroundManager;
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Handler mHandler;
    private Runnable mBackgroundRunnable;
    private Subscription mSearchResultsSubscription;

    private String mSearchQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        mResultsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        mHandler = new Handler();
        setSearchResultProvider(this);
        setupBackgroundManager();
        setListeners();
    }

    public void onDestroy() {
        if (mBackgroundRunnable != null) {
            mHandler.removeCallbacks(mBackgroundRunnable);
            mBackgroundRunnable = null;
        }
        mBackgroundManager = null;
        if (mSearchResultsSubscription != null) mSearchResultsSubscription.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBackgroundManager.release();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SPEECH:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        setSearchQuery(data, false);
                        break;
                    case Activity.RESULT_CANCELED:
                        Timber.i("Recognizer canceled");
                        break;
                }
                break;
        }
    }

    @Override
    public ObjectAdapter getResultsAdapter() {
        return mResultsAdapter;
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {
        loadQuery(newQuery);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        loadQuery(query);
        return true;
    }

    public boolean hasResults() {
        return mResultsAdapter.size() > 0;
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

    private void setupBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mBackgroundManager.setColor(ContextCompat.getColor(getActivity(), R.color.primary_light));
        mDefaultBackground =
                new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.primary_light));
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

    private void setListeners() {
        setOnItemViewClickedListener(mOnItemViewClickedListener);
        setOnItemViewSelectedListener(mOnItemViewSelectedListener);
        if (!hasPermission(Manifest.permission.RECORD_AUDIO)) {
            setSpeechRecognitionCallback(new SpeechRecognitionCallback() {
                @Override
                public void recognizeSpeech() {
                    try {
                        startActivityForResult(getRecognizerIntent(), REQUEST_SPEECH);
                    } catch (ActivityNotFoundException e) {
                        Timber.e("Cannot find activity for speech recognizer", e);
                    }
                }
            });
        }
    }

    private boolean hasPermission(final String permission) {
        final Context context = getActivity();
        return PackageManager.PERMISSION_GRANTED == context.getPackageManager().checkPermission(
                permission, context.getPackageName());
    }

    private void loadQuery(String query) {
        if ((mSearchQuery != null && !mSearchQuery.equals(query)) && query.trim().length() > 0
                || (!TextUtils.isEmpty(query) && !query.equals("nil"))) {
            if (NetworkUtil.isNetworkConnected(getActivity())) {
                mSearchQuery = query;
                searchCats(query);
            } else {
                ToastFactory.createWifiErrorToast(getActivity()).show();
            }
        }
    }

    private void searchCats(String query) {
        mResultsAdapter.clear();
        HeaderItem resultsHeader = new HeaderItem(0, getString(R.string.text_search_results));
        ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(new CardPresenter());
        ListRow listRow = new ListRow(resultsHeader, arrayObjectAdapter);
        mResultsAdapter.add(listRow);
        performSearch(arrayObjectAdapter);
    }

    private void performSearch(final ArrayObjectAdapter arrayObjectAdapter) {
        arrayObjectAdapter.clear();

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

        mSearchResultsSubscription = mDataManager.getCats(cats)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<List<Cat>>() {
                    @Override
                    public void onSuccess(List<Cat> cats) {
                        arrayObjectAdapter.addAll(0, cats);
                    }

                    @Override
                    public void onError(Throwable error) {
                        Timber.e(error, "There was an error loading the cats!");
                    }
                });
    }

    private OnItemViewClickedListener mOnItemViewClickedListener = new OnItemViewClickedListener() {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            // Handle item click
        }
    };

    private OnItemViewSelectedListener mOnItemViewSelectedListener = new OnItemViewSelectedListener() {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof Cat) {
                String backgroundUrl = ((Cat) item).imageUrl;
                if (backgroundUrl != null) startBackgroundTimer(URI.create(backgroundUrl));
            }
        }
    };

}