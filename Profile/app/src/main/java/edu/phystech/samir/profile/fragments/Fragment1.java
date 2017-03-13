package edu.phystech.samir.profile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.phystech.samir.profile.Main_Activity2;
import edu.phystech.samir.profile.R;

public class Fragment1 extends Fragment {
    public static final String TAG = Fragment1.class.getSimpleName();

    private EditText firstname;
    private EditText lastName;
    private TextView Tv;
    private Button Bt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRetainInstance(true);
    }

    TextWatcher TWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        public void afterTextChanged(Editable s) {
            checkFieldsForEmptyValues();
        }
    };

    public void  checkFieldsForEmptyValues(){
        if( lastName.getText().length()!=0
                && firstname.getText().length()!=0
                && Tv.getText().length()!=0
                && Tv.getText().length()!=4 ) Bt.setEnabled(true);
        else Bt.setEnabled(false);
    }

    @Nullable
    @Override
     public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final LinearLayout l = (LinearLayout) inflater.inflate(R.layout.fragment1, container, false);
        firstname = (EditText) l.findViewById(R.id.firstname);
        lastName = (EditText) l.findViewById(R.id.lastname);
        Tv = (TextView) l.findViewById(R.id.TvDate);
        Bt = (Button) l.findViewById(R.id.BtSave);
        Bt.setEnabled(false);

        firstname.addTextChangedListener(TWatcher);
        lastName.addTextChangedListener(TWatcher);
        Tv.addTextChangedListener(TWatcher);

        Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new Fragment2();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Bt.setEnabled(false);
            Intent intent = new Intent(getActivity(), Main_Activity2.class);
            intent.putExtra("name", firstname.getText().toString());
            intent.putExtra("lastname", lastName.getText().toString());
            intent.putExtra("Tv", Tv.getText().toString());
            getActivity().finish();
            startActivity(intent);
                }
            });

        return l;
    }
}
