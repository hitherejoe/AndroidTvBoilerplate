package com.hitherejoe.androidtvboilerplate.ui.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.hitherejoe.androidtvboilerplate.R;
import com.hitherejoe.androidtvboilerplate.data.model.Cat;

public class CardPresenter extends Presenter {

    private static final int CARD_WIDTH = 300;
    private static final int CARD_HEIGHT = 300;

    private int mSelectedBackgroundColor = -1;
    private int mDefaultBackgroundColor = -1;
    private Drawable mDefaultCardImage;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        Context context = parent.getContext();
        mDefaultBackgroundColor = ContextCompat.getColor(context, R.color.primary);
        mSelectedBackgroundColor = ContextCompat.getColor(context, R.color.primary_dark);
        mDefaultCardImage = ContextCompat.getDrawable(context, R.drawable.card_default);

        ImageCardView cardView = new ImageCardView(parent.getContext()) {
            @Override
            public void setSelected(boolean selected) {
                updateCardBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };

        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        updateCardBackgroundColor(cardView, false);
        return new ViewHolder(cardView);
    }

    private void updateCardBackgroundColor(ImageCardView view, boolean selected) {
        int color = selected ? mSelectedBackgroundColor : mDefaultBackgroundColor;
        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        Cat cat = (Cat) item;

        ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setTitleText(cat.name);
        cardView.setContentText(cat.description);

        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);

        Glide.with(cardView.getContext())
                .load(cat.imageUrl)
                .error(mDefaultCardImage)
                .into(cardView.getMainImageView());
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }
}