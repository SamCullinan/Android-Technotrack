package edu.phystech.samir.mygallery;

import android.text.StaticLayout;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Created by Samir on 03.05.2017.
 */

public class JUnitTest {

    private static ImageCache imageCache;

    private final String str = "str";

    @BeforeClass
    public static void initForTest(){
        imageCache = new ImageCache();
    }

    @Test
    public void TestImageGetInstance(){
        assertNotEquals(null,
                imageCache.getInstance());
    }

    @Test
    public void TestImageGet(){
        assertEquals(null,
                imageCache.getBitmapFromMemCache(str));
    }

    @Test
    public void TestImageGetClass() {
         assertEquals(ImageCache.class,imageCache.getClass());
    }
}
