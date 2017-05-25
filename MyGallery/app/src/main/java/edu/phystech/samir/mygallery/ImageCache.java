package edu.phystech.samir.mygallery;

/**
 * Created by Samir on 10.04.2017.
 */
import android.content.Context;
import android.support.v4.util.LruCache;
import android.widget.ImageView;
import android.graphics.Bitmap;

public class ImageCache {
    private LruCache<String, Bitmap> mMemoryCache;
    private static ImageCache imageCache;

    public ImageCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        final int cacheSize = maxMemory / 8;
         mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }

    public  ImageCache getInstance() {
        if (imageCache == null) {
            imageCache = new  ImageCache();
        }

        return imageCache;
    }

    public synchronized Bitmap getBitmapFromMemCache(String str) {
        return  mMemoryCache.get(str);
    }
    public synchronized void addBitmapToMemoryCache(String str, Bitmap bm) {
         mMemoryCache.put(str, bm);
    }

}