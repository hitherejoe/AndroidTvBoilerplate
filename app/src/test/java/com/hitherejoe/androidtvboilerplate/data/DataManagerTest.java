package com.hitherejoe.androidtvboilerplate.data;

import com.hitherejoe.androidtvboilerplate.data.local.PreferencesHelper;
import com.hitherejoe.androidtvboilerplate.data.model.Cat;
import com.hitherejoe.androidtvboilerplate.data.remote.AndroidTvBoilerplateService;
import com.hitherejoe.androidtvboilerplate.test.common.TestDataFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import rx.observers.TestSubscriber;

@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

    @Mock AndroidTvBoilerplateService mMockAndroidTvBoilerplateService;
    @Mock PreferencesHelper mMockPreferencesHelper;
    private DataManager mDataManager;

    @Before
    public void setUp() {
        mDataManager = new DataManager(mMockPreferencesHelper, mMockAndroidTvBoilerplateService);
    }

    @Test
    public void getCatsCompletesAndEmitsCats() throws Exception {
        List<Cat> mockCats = TestDataFactory.makeCats(10);

        TestSubscriber<List<Cat>> result = new TestSubscriber<>();
        mDataManager.getCats(mockCats).subscribe(result);
        result.assertNoErrors();
        result.assertValue(mockCats);
    }

}