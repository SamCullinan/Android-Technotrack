package edu.phystech.samir.profile.fragments;

/**
 * Created by Samir on 08.03.2017.
 *
 *
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TextView;
import android.os.Bundle;
import java.util.Calendar;
import edu.phystech.samir.profile.R;
import static edu.phystech.samir.profile.R.layout.fragment1;

public class Fragment2 extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	public static final String TAG = Fragment.class.getSimpleName();

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
		int Year = c.get(Calendar.YEAR);
		int Month = c.get(Calendar.MONTH);
		int Day = c.get(Calendar.DAY_OF_MONTH);
		return new DatePickerDialog(getActivity(),this, Year, Month, Day);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
		TextView tv = (TextView) getActivity().findViewById(R.id.TvDate);
		String Calen=dayOfMonth+"-"+month+"-"+ year;
		tv.setText(Calen);
	}
}