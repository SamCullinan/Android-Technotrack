package edu.phystech.samir.mygallery;

/**
 * Created by Samir on 25.05.2017.
 */

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ListView;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ActivityTestRule {

//Бредовый немного тест, так как в этом задании практически нет ничего(
//А можно как-то задерживать activity на рабочем столе, при этом,чтобы все процессы работали?
//Хотел сделать тест со скроллингом до i позиции, но активити быстро закрывается,
// практически не успевает скачать картинки

    @Rule
    public  android.support.test.rule.ActivityTestRule<MainActivity> mActivityRule = new
            android.support.test.rule.ActivityTestRule<>(
            MainActivity.class, true, false);

    private Intent getMainActivityLaunchIntent() {
        Intent returnIntent = new Intent(InstrumentationRegistry.getTargetContext(),
                MainActivity.class);
        return returnIntent;
    }

    @Test
    public void testActivity() throws Throwable {
        final MainActivity MainActivity =
                mActivityRule.launchActivity(getMainActivityLaunchIntent());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        try {
            mActivityRule.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListView Lv = (ListView) MainActivity.findViewById(R.id.listViewImg);
                    Assert.assertEquals(Lv.canScrollHorizontally(1),false);
                }
            });
        } catch (InterruptedException e) {
            Assert.assertTrue("Interrupted exception", false);
        }
    }
}