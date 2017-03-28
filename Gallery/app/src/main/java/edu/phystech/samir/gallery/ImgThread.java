package edu.phystech.samir.gallery;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;


public class ImgThread extends HandlerThread {

    private Handler handler;
    private Handler twoHandler;

    private ArrayList <String> arrayListImg=new ArrayList<>();
    private ListViewAdapter adapter;


    public ImgThread(Handler twoHandler, ListViewAdapter adapter, int size) {
        super(ImgThread.class.getSimpleName());
        this.twoHandler = twoHandler;
        this.adapter = adapter;
        for(int i=0; i < size; i++) {
            arrayListImg.add(i, null);
        }
    }


    public void ListImgItem(int key, String url) {
        arrayListImg.set(key, url);
        handler.obtainMessage(key, url).sendToTarget();
    }

    public void mHandler() {
        handler = new Handler(getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message MyMessage) {
                HttpURLConnection connection = null;
                String path = (String) MyMessage.obj;
                int key = MyMessage.what;
                try {
                    URL url = new URL(path);
                    connection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    adapter.setImage(key, bitmap);
                    twoHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
                finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                return true;
            }
        });
    }

}
