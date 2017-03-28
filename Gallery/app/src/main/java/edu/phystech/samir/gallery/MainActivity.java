package edu.phystech.samir.gallery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends Activity {

    ArrayList<String> arrayList = new ArrayList<>();
    CharSequence text = "No Internet connection";

    ListView listViewImg;
    ListViewAdapter adapter;

    ImgThread thread;

    int duration = Toast.LENGTH_LONG;

    HttpURLConnection connection;
    String downloadJSON = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new JSONTask().execute("http://188.166.49.215/tech/imglist.json");
    }

    class JSONTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            try {

                URL url = new URL(params[0]);

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true);
                connection.connect();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
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
                if (connection != null) {
                    connection.disconnect();
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

                thread = new ImgThread(new Handler(), adapter, arrayList.size());
                thread.start();
                thread.mHandler();

                for (int i = 0; i < arrayList.size(); i++) {
                    thread.ListImgItem(i, arrayList.get(i));
                }
            }
        }
    }
}