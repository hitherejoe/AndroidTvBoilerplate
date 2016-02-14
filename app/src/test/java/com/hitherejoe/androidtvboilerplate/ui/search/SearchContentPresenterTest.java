package com.hitherejoe.androidtvboilerplate.ui.search;

import com.hitherejoe.androidtvboilerplate.data.DataManager;
import com.hitherejoe.androidtvboilerplate.data.model.Cat;
import com.hitherejoe.androidtvboilerplate.test.common.TestDataFactory;
import com.hitherejoe.androidtvboilerplate.util.RxSchedulersOverrideRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import rx.Single;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchContentPresenterTest {

    @Mock SearchContentMvpView mMockSearchContentMvpView;
    @Mock DataManager mMockDataManager;
    private SearchContentPresenter mSearchContentPresenter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        mSearchContentPresenter = new SearchContentPresenter(mMockDataManager);
        mSearchContentPresenter.attachView(mMockSearchContentMvpView);
    }

    @After
    public void detachView() {
        mSearchContentPresenter.detachView();
    }

    @Test
    public void getCatsSuccessful() {
        List<Cat> cats = TestDataFactory.makeCats(10);
        stubDataManagerGetCats(Single.just(cats));

        mSearchContentPresenter.searchCats(cats);

        verify(mMockSearchContentMvpView).showCats(cats);
        verify(mMockSearchContentMvpView, never()).showCatsError();
    }

    @Test
    public void getTagsFails() {
        List<Cat> cats = TestDataFactory.makeCats(10);
        stubDataManagerGetCats(Single.just(cats));
        stubDataManagerGetCats(Single.<List<Cat>>error(new RuntimeException()));

        mSearchContentPresenter.searchCats(cats);

        verify(mMockSearchContentMvpView).showCatsError();
        verify(mMockSearchContentMvpView, never()).showCats(anyListOf(Cat.class));
    }

    private void stubDataManagerGetCats(Single<List<Cat>> single) {
        when(mMockDataManager.getCats(anyListOf(Cat.class))).thenReturn(single);
    }

}