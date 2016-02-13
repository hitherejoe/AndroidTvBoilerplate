package com.hitherejoe.tvboilerplate;

import com.hitherejoe.tvboilerplate.data.DataManager;
import com.hitherejoe.tvboilerplate.data.local.PreferencesHelper;
import com.hitherejoe.tvboilerplate.data.remote.BoilerplateService;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

    @Mock PreferencesHelper mMockPreferencesHelper;
    @Mock BoilerplateService mMockBoilerplateService;
    private DataManager mDataManager;

    @Before
    public void setUp() {
        mDataManager = new DataManager(mMockPreferencesHelper, mMockBoilerplateService);
    }


}
