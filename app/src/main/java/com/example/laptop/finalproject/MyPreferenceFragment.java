package com.example.laptop.finalproject;


import android.os.Bundle;
import android.preference.PreferenceFragment;


/*==this fragment shows the shared preference=====*/
public class MyPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.shared_preferences); //loads the corresponding xml file
    }
}
