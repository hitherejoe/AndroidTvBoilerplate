package com.hitherejoe.androidtvboilerplate.ui.search;

import com.hitherejoe.androidtvboilerplate.data.model.Cat;
import com.hitherejoe.androidtvboilerplate.ui.base.MvpView;

import java.util.List;

public interface SearchContentMvpView extends MvpView {

    void showCats(List<Cat> cats);

    void showCatsError();

}