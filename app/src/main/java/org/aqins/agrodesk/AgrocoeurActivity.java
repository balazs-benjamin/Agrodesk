package org.aqins.agrodesk;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.Spinner;

public class AgrocoeurActivity extends AppCompatActivity {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agrocoeur);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Saved form data", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_agrocoeur, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private SharedPreferences sharedPreferences;
        private SharedPreferences.Editor editor;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance() {
            PlaceholderFragment fragment = new PlaceholderFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.tab_contexte, container, false);

            EditText etProjName = (EditText) rootView.findViewById(R.id.titlePrjtField);
            EditText etQuality = (EditText) rootView.findViewById(R.id.qltVoltField);
            EditText etNumerofVolunteers = (EditText) rootView.findViewById(R.id.etNumberofVolun);
            EditText etActivityName1 = (EditText) rootView.findViewById(R.id.actVol1);
            EditText etActivityName2 = (EditText) rootView.findViewById(R.id.actVol2);

            sharedPreferences = getActivity().getSharedPreferences("org.aqins.agrodesk", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();

            if(sharedPreferences.contains("projName"))
                etProjName.setText(sharedPreferences.getString("projName", ""));
            if(sharedPreferences.contains("quality"))
                etQuality.setText(sharedPreferences.getString("quality", ""));
            if(sharedPreferences.contains("num_volun"))
                etNumerofVolunteers.setText(sharedPreferences.getString("num_volun", ""));
            if(sharedPreferences.contains("activity1"))
                etActivityName1.setText(sharedPreferences.getString("activity1", ""));
            if(sharedPreferences.contains("activity2"))
                etActivityName2.setText(sharedPreferences.getString("activity2", ""));
            etProjName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    editor.putString("projName", s.toString());
                    editor.commit();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            etQuality.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    editor.putString("quality", s.toString());
                    editor.commit();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            etActivityName1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    editor.putString("activity1", s.toString());
                    editor.commit();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            etActivityName2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    editor.putString("activity2", s.toString());
                    editor.commit();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            etNumerofVolunteers.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    editor.putString("num_volun", s.toString());
                    editor.commit();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            return rootView;


        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
          //  return PlaceholderFragment.newInstance(position + 1);

            switch(position){
                case 0 : return PlaceholderFragment.newInstance();
                case 1 : return  TabAgrocadre.newInstance();
                case 2 : return  TabAgrosportule.newInstance();

            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Contexte";
                case 1:

                    return "Cadre";
                case 2:

                    return "Sportule";
            }
            return null;
        }



    }
}
