package com.example.fragment_test.ui.shopping_list;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.fragment_test.MainActivity2;
import com.example.fragment_test.R;

import junit.framework.TestCase;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ShoppingListFragmentTest extends TestCase {

    @Rule
    public ActivityScenarioRule<MainActivity2> rule =
            new ActivityScenarioRule<>(MainActivity2.class);

    @Test
    public void addNewItemTest() {

        ActivityScenario<MainActivity2> launch = ActivityScenario.launch(MainActivity2.class);


        onView(withId(R.id.navigation_shopping_list))
                .perform(click());

        onView(withId(R.id.fragment_shopping_list))
                .check(matches(isDisplayed()));


    }

    public static ViewAction doTaskInUIThread(final Runnable r) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void perform(UiController uiController, View view) {
                r.run();
            }
        };
    }
}