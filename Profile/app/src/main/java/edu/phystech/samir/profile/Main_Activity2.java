package edu.phystech.samir.profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import edu.phystech.samir.profile.fragments.Fragment1;
import edu.phystech.samir.profile.fragments.Fragment2;
import static android.app.PendingIntent.getActivity;
import static android.icu.text.RelativeDateTimeFormatter.Direction.THIS;
import static edu.phystech.samir.profile.R.id.firstname;
import static edu.phystech.samir.profile.R.styleable.View;

public class Main_Activity2 extends AppCompatActivity {
    private Button   back;
    private TextView firstnametxt;
    private TextView lastNametxt;
    private TextView datetxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        back = (Button) findViewById(R.id.back_button);
        firstnametxt = (TextView) findViewById(R.id.firstnametxt);
        lastNametxt = (TextView) findViewById(R.id.lastnametxt);
        datetxt = (TextView) findViewById(R.id.datetxt);

        String txtName =firstnametxt.getText().toString() + " " + getIntent().getStringExtra("name");
        String txtlastname = lastNametxt.getText().toString() + " " + getIntent().getStringExtra("lastname");
        String txtdate = datetxt.getText().toString() + " " + getIntent().getStringExtra("Tv");

        firstnametxt.setText(txtName);
        lastNametxt.setText(txtlastname);
        datetxt.setText(txtdate);

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity2.this,MainActivity.class);
                Main_Activity2.super.finish();
                startActivity(intent);
            }
        });
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
