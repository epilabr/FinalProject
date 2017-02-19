package com.example.laptop.finalproject;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.Fragment;
import java.util.Locale;


/*==this fragment shows the shared preference=====*/
public class MyPreferenceFragment extends PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.shared_preferences); //loads the corresponding xml file

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String savedUnites = sharedPreferences.getString("unites",null);
        final Preference distancePreference = findPreference("distancePreference");


        if (savedUnites.equals(getString(R.string.metric))){
            distancePreference.setTitle(R.string.changeToMiles);
        }else{
            distancePreference.setTitle(R.string.changeToKilometers);
        }


        Preference myPref = findPreference("preference");
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                MyPreferenceDialog myPreferenceDialog = new MyPreferenceDialog();
                myPreferenceDialog.show(getFragmentManager(), "dialog");

                return false;
            }
        });



        distancePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String savedUnites = sharedPreferences.getString("unites",null);

                if (savedUnites.equals(getString(R.string.metric))){
                    distancePreference.setTitle(R.string.changeToKilometers);
                    editor.putString("unites",getString(R.string.miles));
                    editor.apply();
                }
                if (savedUnites.equals(getString(R.string.miles))){
                    distancePreference.setTitle(R.string.changeToMiles);
                    editor.putString("unites",getString(R.string.metric));
                    editor.apply();
                }

                return false;
            }
        });

    }

    public static class MyPreferenceDialog extends DialogFragment {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.delete_favorites)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DbCommands dbCommands = new DbCommands(getActivity());
                            dbCommands.deletePlacesDB();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            return builder.create();
        }
    }



}
