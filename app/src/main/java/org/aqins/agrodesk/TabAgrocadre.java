package org.aqins.agrodesk;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Axelle on 22/12/2016.
 */

public class TabAgrocadre extends Fragment implements DatePickerDialog.OnDateSetListener{

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static TabAgrocadre newInstance() {
        TabAgrocadre fragment = new TabAgrocadre();
        return fragment;
    }

    TextView selectDate;
    TextView dateToActivate;
    public TabAgrocadre() {
    }

    Button ClickMe;
    TextView tv;


    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_agrocadre, container,
                false);

        sharedPreferences = getActivity().getSharedPreferences("org.aqins.agrodesk", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        final List<String> list=new ArrayList<String>();
        list.add("Bénin");
        list.add("Côte d'Ivoire");
        list.add("Cameroun");
        list.add("Togo");
        list.add("Ghana");

        final Spinner s = (Spinner) rootView.findViewById(R.id.spinner);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(dataAdapter);

                s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                String items = s.getSelectedItem().toString();
                editor.putString("country", items);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        selectDate = (TextView)rootView.findViewById(R.id.SelectDate); // getting the image button in fragment_blank.xml
        dateToActivate = (TextView) rootView.findViewById(R.id.selectedDate); // getting the TextView in fragment_blank.xml
        selectDate.setOnClickListener(new View.OnClickListener() {  // setting listener for user click event
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment(); // creating DialogFragment which creates DatePickerDialog
                newFragment.setTargetFragment(TabAgrocadre.this,0);  // Passing this fragment DatePickerFragment.
                // As i figured out this is the best way to keep the reference to calling activity when using FRAGMENT.
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
        dateToActivate.setOnClickListener(new View.OnClickListener() {  // setting listener for user click event
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment(); // creating DialogFragment which creates DatePickerDialog
                newFragment.setTargetFragment(TabAgrocadre.this,0);  // Passing this fragment DatePickerFragment.
                // As i figured out this is the best way to keep the reference to calling activity when using FRAGMENT.
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        EditText etDesc = (EditText) rootView.findViewById(R.id.descPlace) ;
        EditText etCity = (EditText) rootView.findViewById(R.id.sportuleField);

        etCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editor.putString("meeting_city", s.toString());
                editor.commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editor.putString("meeting_desc", s.toString());
                editor.commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch(checkedId) {
                    case R.id.radioButton7:
                        editor.putString("meeting_place", "yes");
                        editor.commit();
                        break;
                    case R.id.radioButton6:
                        editor.putString("meeting_place", "no");
                        editor.commit();
                        break;
                }
            }
        });


        return rootView;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) { // what should be done when a date is selected
        StringBuilder sb = new StringBuilder().append(dayOfMonth).append("/").append(monthOfYear + 1).append("/").append(year);
        String formattedDate = sb.toString();
        dateToActivate.setText(formattedDate);

        editor.putString("meeting_date", formattedDate);
        editor.commit();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
} // This is the end of our MyFragments Class
