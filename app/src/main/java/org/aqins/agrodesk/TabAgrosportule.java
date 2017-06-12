package org.aqins.agrodesk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Axelle on 22/12/2016.
 */

public class TabAgrosportule extends Fragment{

    private DatabaseReference mDatabase;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static TabAgrosportule newInstance() {
        TabAgrosportule fragment = new TabAgrosportule();
        return fragment;
    }


    public TabAgrosportule() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Fair");
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPreferences = getActivity().getSharedPreferences("org.aqins.agrodesk", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        View rootView = inflater.inflate(R.layout.tab_agrosportule, container,
                false);

        final EditText et1 = (EditText) rootView.findViewById(R.id.sportuleField);

        final Button btnpay1 = (Button) rootView.findViewById(R.id.button3);
        final Button btnpay2 = (Button) rootView.findViewById(R.id.UssdDial);

        et1.setVisibility(View.INVISIBLE);
        btnpay1.setVisibility(View.VISIBLE);
        btnpay2.setVisibility(View.VISIBLE);

        final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioButton2){
                    et1.setVisibility(View.VISIBLE);
                    btnpay1.setVisibility(View.INVISIBLE);
                    btnpay2.setVisibility(View.INVISIBLE);
                } else {

                    et1.setVisibility(View.INVISIBLE);
                    btnpay1.setVisibility(View.VISIBLE);
                    btnpay2.setVisibility(View.VISIBLE);

                }
            }
        });

        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et1.getVisibility() == View.VISIBLE) {
                    editor.putString("quantity", s.toString());
                    editor.commit();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button btnSubmit = (Button) rootView.findViewById(R.id.kermesseSubmitBtn);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference newPost = mDatabase.push();
                newPost.child("projName").setValue(sharedPreferences.getString("projName", ""));
                newPost.child("quality").setValue(sharedPreferences.getString("quality", ""));
                newPost.child("activity1").setValue(sharedPreferences.getString("activity1", ""));
                newPost.child("activity2").setValue(sharedPreferences.getString("activity2", ""));
                newPost.child("num_volun").setValue(sharedPreferences.getString("num_volun", ""));

                newPost.child("country").setValue(sharedPreferences.getString("country", ""));
                newPost.child("meeting_city").setValue(sharedPreferences.getString("meeting_city", ""));
                newPost.child("meeting_desc").setValue(sharedPreferences.getString("meeting_desc", ""));
                newPost.child("meeting_place").setValue(sharedPreferences.getString("meeting_place", ""));
                newPost.child("meeting_date").setValue(sharedPreferences.getString("meeting_date", ""));

                if(et1.getVisibility() == View.VISIBLE) {
                    newPost.child("isGift").setValue(true);
                    newPost.child("gift_quality").setValue(sharedPreferences.getString("quantity", ""));
                } else {
                    newPost.child("isGift").setValue(false);
                }
            }
        });

        rootView.findViewById(R.id.UssdDial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // Creating alert Dialog with two Buttons
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                // Setting Dialog Title
                alertDialog.setTitle("Do you want to call?");
                // Setting Dialog Message
                alertDialog.setMessage("*400#");
                // Setting Icon to Dialog
                //alertDialog.setIcon(R.drawable.warning);
                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // Write your code here to execute after dialog
                                dialog.cancel();
                            }
                        });
                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // Write your code here to execute after dialog
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse(Uri.parse("tel:" + "*400")+Uri.encode("#")));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(callIntent);

                            }
                        });

                // Showing Alert Message
                alertDialog.show();
            }
        });



        return rootView;
    }







} // This is the end of our MyFragments Class
