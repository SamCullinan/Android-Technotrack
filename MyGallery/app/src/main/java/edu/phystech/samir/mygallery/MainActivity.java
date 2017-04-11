package edu.phystech.samir.mygallery;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends Activity {

    final static String BROADCAST_ACTION = "BROADCAST_ACTION";
    final int PERMISSION_REQUEST_CODE = 2222;

    ArrayList<String> arrayList = new ArrayList<>();

    CharSequence text = "No Internet connection";

    ListView listViewImg;
    ListViewAdapter adapter;
    ImgLoadServ imgLoadServ;

    long NumberDownload = 0;

    int duration = Toast.LENGTH_LONG;

    boolean mBound = false;
    Intent intent;
    BroadcastReceiver broadcastReceiver;

    private boolean shouldRequestPermission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, ImgLoadServ.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(connection);
            mBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
    }

    private void checkPermission(){
        if(!shouldRequestPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        startPickerActivity(grantResults , requestCode);

        intent = new Intent(this, ImgLoadServ.class);
        broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Bitmap bitmap = intent.getParcelableExtra("bitmap");
                int key = intent.getIntExtra("key", 0);
                adapter.setImage(key, bitmap);
                adapter.notifyDataSetChanged();
            }
        };

        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
        new JSONTask().execute("http://188.166.49.215/tech/imglist.json");
    }


    public void startPickerActivity(int[] grantResults,int requestCode){
        if( (requestCode == PERMISSION_REQUEST_CODE) && (grantResults.length > 0) )
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) shouldRequestPermission = true;
                else shouldRequestPermission = false;
    }


    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBound = true;
            ImgLoadServ.mBinder binder = (ImgLoadServ.mBinder) service;
            imgLoadServ = binder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }

    };



    private class JSONTask extends AsyncTask<String, Void, Boolean> {

        HttpURLConnection urlConnection;
        String downloadJSON = "";
        @Override
        protected Boolean doInBackground(String... params) {
            try {

                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setInstanceFollowRedirects(true);
                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuilder buffer = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    downloadJSON = buffer.toString();
                    JSONArray jsonArray = new JSONArray(downloadJSON);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(jsonArray.getString(i));
                    }
                    return false;
                }
            }
            catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            finally {
                if (urlConnection!= null) {
                    urlConnection.disconnect();
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean status) {

            if (status) {

                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
            }
            else {

                listViewImg = (ListView) findViewById(R.id.listViewImg);
                adapter = new ListViewAdapter(getApplicationContext(), arrayList);
                listViewImg.setAdapter(adapter);

                Bundle Bundle = new Bundle();
                Bundle.putSerializable("ArrayList", arrayList);
                Bundle.putBoolean("ExternalStorage", shouldRequestPermission);
                intent.putExtras(Bundle);
                startService(intent);
/*
* На лекции было сказано,что onScroll-это плохое решение,но после долгих поисков
* для listview  я нашел только этот вариант с OnScrollListenerom на сайте
* http://startandroid.ru/ru/uroki/vse-uroki-spiskom/85-urok-44-sobytija-v-listview.html (можно ли пользоваться этим сайтом?)
* Я пытался сделать что-то похожее на  функции со слайдов лекции,но они не заработали у меня
* Буду благодарен,если расскажете как хорошо реализовать загрузку по мере прокрутки списка на ListView
* */
               listViewImg.setOnScrollListener(new AbsListView.OnScrollListener() {

                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }
                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem,
                                         int visibleItemCount, int totalItemCount) {
                        int lastVisibleItem = visibleItemCount + firstVisibleItem;
                        for (int i = firstVisibleItem; i < lastVisibleItem; i++) {
                            if(i >= NumberDownload) {
                                imgLoadServ.ListImgItem(i, arrayList.get(i));
                                NumberDownload++;
                            }
                        }
                    }
                });
            }
        }

    }
}