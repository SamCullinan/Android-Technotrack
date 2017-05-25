package edu.phystech.samir.mygallery;

/**
 * Created by Samir on 25.05.2017.
 */

import android.support.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import java.util.concurrent.TimeUnit;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class Espresso {
    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(MainActivity.class);
    @Test
    public void testServiceConnection() throws InterruptedException {
        onView(withId(R.id.listViewImg)).perform(swipeUp());
        Thread.sleep(2);
        onView(withId(R.id.listViewImg)).perform(swipeDown());
        Thread.sleep(2);
        onView(withId(R.id.listViewImg)).perform(swipeUp());
        Thread.sleep(2);
        onView(withId(R.id.listViewImg)).perform(swipeUp());
        Thread.sleep(2);
        onView(withId(R.id.listViewImg)).perform(swipeDown());
        Thread.sleep(2);
        onView(withId(R.id.listViewImg)).perform(swipeUp());
    }

}

