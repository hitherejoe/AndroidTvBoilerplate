package com.hitherejoe.androidtvboilerplate.ui.content;

import com.hitherejoe.androidtvboilerplate.data.model.Cat;
import com.hitherejoe.androidtvboilerplate.ui.base.MvpView;

import java.util.List;

public interface ContentMvpView extends MvpView {

    void showCats(List<Cat> cats);

    void showCatsError();

}