package edu.phystech.samir.mygallery;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class ContextTest {

    private static Context context;

    @BeforeClass
    public static void getContext(){
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void useContext() throws Exception {
        assertEquals("edu.phystech.samir.mygallery",
                context.getPackageName());
    }

    @Test
    public void startMainActivity() throws Exception {
        Context testContext = InstrumentationRegistry.getContext();
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        testContext.startActivity(intent);
    }

}
