package edu.phystech.samir.mygallery;

/**
 * Created by Samir on 09.04.2017.
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;


public class ImgLoadServ extends Service {


    ArrayList<String> arrayList;
    ImageCache LruCache;
    Handler Handler;
    Looper looper;
    IBinder binder = new mBinder();
    mHandler mHandler;

    boolean shouldRequestPermission;


    @Override
    public void onCreate() {
        LruCache = ImageCache.getInstance();
        Handler = new Handler();
        HandlerThread thread = new HandlerThread("ImgThread");
        thread.start();
        looper = thread.getLooper();
        mHandler = new mHandler(looper);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        arrayList = (ArrayList<String>) bundle.getSerializable("ArrayList");
        shouldRequestPermission = bundle.getBoolean("ExternalStorage");
        return Service.START_REDELIVER_INTENT;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void ListImgItem(int key, String url) {
        mHandler.obtainMessage(key, url).sendToTarget();
    }

    public class mBinder extends Binder {
        ImgLoadServ getService() {
            return ImgLoadServ.this;
        }
    }

    private class mHandler extends Handler {
        mHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Bitmap bitmap;

            String path = (String) msg.obj;
            int key = msg.what;

            String[] strings = path.split("/");
            String filename = strings[strings.length - 1];

            bitmap = LoadFromImgCache(filename);

            if(bitmap == null) {
                if(shouldRequestPermission && isExternalStorageReadable())
                    bitmap = LoadFromExternalStorage(filename);
                else
                    bitmap = LoadFromInternalStorage(getApplicationContext(), filename);

                if(bitmap != null)
                    LruCache.addBitmapToMemoryCache(filename, bitmap);
                else {
                    bitmap = LoadFromURL(path);
                    LruCache.addBitmapToMemoryCache(filename, bitmap);

                    if( shouldRequestPermission && isExternalStorageWritable()) {
                        saveImgOnExternal(bitmap, filename);
                    }
                    else
                        saveImgOnInternal(getApplicationContext(), bitmap, filename);
                }
            }

            final Intent intent = new Intent(MainActivity.BROADCAST_ACTION);

            intent.putExtra("bitmap", bitmap);
            intent.putExtra("key", key);

            Handler.post(new Runnable() {
                @Override
                public void run() {
                    sendBroadcast(intent);
                }
            });
            stopSelf(msg.arg1);
        }
    }

    private Bitmap LoadFromURL(String s) {
        Bitmap bitmap = null;
        HttpURLConnection connection = null;
        try {

            URL url = new URL(s);
            connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return bitmap;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
            return false;
        else
            return true;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
            return false;
        else
            return true;
    }

    private void saveImgOnExternal(Bitmap bitmap, String filename) {
            OutputStream output;
            File filepath = Environment.getExternalStorageDirectory();
            File dir = new File(filepath.getAbsolutePath() + "/ImgCollections");
           boolean results = dir.mkdirs();
            File file = new File(dir, filename);
            try {
                output = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 300, output);
                output.flush();
                output.close();
            } catch (Exception e) { e.printStackTrace(); }
    }

    private void saveImgOnInternal(Context context, Bitmap bitmap, String filename) {
        OutputStream output;
        File cacheDir = context.getCacheDir();
        File dir = new File(cacheDir.getAbsolutePath() + "/ImgCollections");
        boolean result = dir.mkdirs();
        File file = new File(dir, filename);
            try {
                output = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 300, output);
                output.flush();
                output.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public Bitmap LoadFromInternalStorage(Context context, String filename) {
        File CacheDir = context.getCacheDir();
        File dir = new File(CacheDir.getAbsolutePath() + "/ImgCollections");
        File file = new File(dir, filename);
        if (file.exists()) {
            try {
                InputStream is = new FileInputStream(file);
                BitmapFactory.Options options = new BitmapFactory.Options();
                return BitmapFactory.decodeStream(is, null, options);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Bitmap LoadFromExternalStorage(String filename) {
            File filepath = Environment.getExternalStorageDirectory();
            File dir = new File(filepath.getAbsolutePath() + "/ImgCollections");
            File file = new File(dir, filename);
            if (file.exists()) {
                try {
                    InputStream is = new FileInputStream(file);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    return BitmapFactory.decodeStream(is, null, options);
                } catch (FileNotFoundException e) { e.printStackTrace(); }
             }
        return null;
    }

    private Bitmap LoadFromImgCache(String filename) {
        return LruCache.getBitmapFromMemCache(filename);
    }
}