package com.hitherejoe.androidtvboilerplate;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hitherejoe.androidtvboilerplate.data.model.Cat;
import com.hitherejoe.androidtvboilerplate.test.common.TestDataFactory;
import com.hitherejoe.androidtvboilerplate.test.common.rules.TestComponentRule;
import com.hitherejoe.androidtvboilerplate.ui.content.ContentActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.util.List;

import rx.Single;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.hitherejoe.androidtvboilerplate.util.CustomMatchers.withItemText;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class ContentActivityTest {

    public final TestComponentRule component =
            new TestComponentRule(InstrumentationRegistry.getTargetContext());
    public final ActivityTestRule<ContentActivity> main =
            new ActivityTestRule<>(ContentActivity.class, false, false);

    @Rule
    public final TestRule chain = RuleChain.outerRule(component).around(main);

    @Test
    public void postsDisplayAndAreBrowseable() {
        List<Cat> mockcats = TestDataFactory.makeCats(5);
        stubDataManagerGetCats(Single.just(mockcats));
        main.launchActivity(null);

        onView(withId(R.id.browse_headers))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        for (int i = 0; i < mockcats.size(); i++) {
            checkItemAtPosition(mockcats.get(i), i);
        }
    }

    private void checkItemAtPosition(Cat cat, int position) {
        if (position > 0) {
            onView(withItemText(cat.name, R.id.browse_container_dock)).perform(click());
        }
        onView(withItemText(cat.name, R.id.browse_container_dock))
                .check(matches(isDisplayed()));
        onView(withItemText(cat.description, R.id.browse_container_dock))
                .check(matches(isDisplayed()));
    }

    private void stubDataManagerGetCats(Single<List<Cat>> single) {
        when(component.getMockDataManager().getCats(anyListOf(Cat.class)))
                .thenReturn(single);
    }

}