package edu.phystech.samir.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import edu.phystech.samir.profile.fragments.Fragment1;


public class MainActivity extends AppCompatActivity{
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        TextView tvInfo = (TextView) findViewById(R.id.TvDate);
        tvInfo.setText(savedInstanceState.getString("src"));
        super.onRestoreInstanceState(savedInstanceState);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        TextView tvInfo = (TextView) findViewById(R.id.TvDate);
        String src=tvInfo.getText().toString();
        outState.putString("src",src);
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FragmentManager fm = getSupportFragmentManager();

        if (savedInstanceState == null) {
            fm.beginTransaction().replace(R.id.fragment, new Fragment1(), Fragment1.TAG).commit();
        }
    }
}

