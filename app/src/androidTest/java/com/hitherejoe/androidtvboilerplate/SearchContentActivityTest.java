package com.hitherejoe.androidtvboilerplate;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hitherejoe.androidtvboilerplate.data.model.Cat;
import com.hitherejoe.androidtvboilerplate.test.common.TestDataFactory;
import com.hitherejoe.androidtvboilerplate.test.common.rules.TestComponentRule;
import com.hitherejoe.androidtvboilerplate.ui.search.SearchContentActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.util.List;

import rx.Single;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.hitherejoe.androidtvboilerplate.util.CustomMatchers.withItemText;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class SearchContentActivityTest {

    public final TestComponentRule component =
            new TestComponentRule(InstrumentationRegistry.getTargetContext());
    public final ActivityTestRule<SearchContentActivity> main =
            new ActivityTestRule<>(SearchContentActivity.class, false, false);

    @Rule
    public final TestRule chain = RuleChain.outerRule(component).around(main);

    @Test
    public void searchResultsDisplayAndAreScrollable() {
        main.launchActivity(null);

        List<Cat> mockCats = TestDataFactory.makeCats(5);
        stubDataManagerGetCats(Single.just(mockCats));

        onView(withId(R.id.lb_search_text_editor))
                .perform(replaceText("cat"));

        for (int i = 0; i < mockCats.size(); i++) {
            checkItemAtPosition(mockCats.get(i));
        }
    }

    private void checkItemAtPosition(Cat cat) {
        onView(withItemText(cat.name, R.id.lb_results_frame)).perform(click());
        onView(withItemText(cat.description, R.id.lb_results_frame)).check(matches(isDisplayed()));
    }

    private void stubDataManagerGetCats(Single<List<Cat>> single) {
        when(component.getMockDataManager().getCats(anyListOf(Cat.class)))
                .thenReturn(single);
    }

}