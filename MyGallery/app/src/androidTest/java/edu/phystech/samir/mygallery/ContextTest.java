package edu.phystech.samir.mygallery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;

import java.nio.ByteBuffer;
import java.util.Arrays;

@RunWith(AndroidJUnit4.class)
public class ContextTest extends InstrumentationTestCase {


    private static Context context;
    private static String str1 = "Bitmap1";
    private static String str2 = "Bitmap2";
    private Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
    private  ImgLoadServ saveHelp;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = InstrumentationRegistry.getTargetContext();
        assertNotNull(context);
        saveHelp = new ImgLoadServ();
    }

    @Test
    public void testSaveBitmapAndLoadBm() throws Exception {

        saveHelp.saveImgOnInternal(context, bitmap , str1);
        Bitmap res1 = saveHelp.LoadFromInternalStorage(context, str1);

        saveHelp.saveImgOnInternal(context, bitmap , str2);
        Bitmap res2 = saveHelp.LoadFromInternalStorage(context, str2);

        assertNotNull (res1);
        assertNotNull (res2);

        ByteBuffer buffer1 = ByteBuffer.allocate(res1.getHeight() * res1.getRowBytes());
        res1.copyPixelsToBuffer(buffer1);

        ByteBuffer buffer2 = ByteBuffer.allocate(res2.getHeight() * res2.getRowBytes());
        res2.copyPixelsToBuffer(buffer2);

        Boolean flag1  = Arrays.equals(buffer1.array(), buffer2.array());
        assertTrue(flag1);
    }

    @After
    @Override
   public void tearDown() throws Exception {
        super.tearDown();
    }
}
