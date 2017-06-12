package org.aqins.agrodesk;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Axelle on 28/12/2016.
 */
public class SelectDateFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener dateSetListener; // listener object to get calling fragment listener
    DatePickerDialog myDatePicker;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        dateSetListener = (DatePickerDialog.OnDateSetListener)getTargetFragment(); // getting passed fragment
        myDatePicker = new DatePickerDialog(getActivity(), dateSetListener, year, month, day); // DatePickerDialog gets callBack listener as 2nd parameter
        // Create a new instance of DatePickerDialog and return it
        return myDatePicker;
    }
}
