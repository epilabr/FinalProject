package com.example.laptop.finalproject;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;


/*====this fragments purpose is to display the search results the user preformed and send the search details to the SearchIntentService=====*/
public class SearchFragment extends Fragment {
    Button searchButton;
    String searchText;
    CheckBox searchCheckBox;
    String ifChecked;
    SeekBar distanceSeekBar;
    String searchRadius;
    TextView distanceTV;
    String units;





    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        units = "meters";

        searchButton = (Button)getView().findViewById(R.id.search_button);

        searchText = ((EditText)getView().findViewById(R.id.search_text)).getText().toString(); //captures the text in search field to "searchText"

        searchCheckBox = (CheckBox)getView().findViewById(R.id.search_check_Box);

        distanceSeekBar = (SeekBar)getView().findViewById(R.id.distance_seek_bar);

        distanceTV = (TextView)getView().findViewById(R.id.distance_tv);



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);  //these two lines are to make the keyboard disappear when SearchIntentService starts.


                if (searchCheckBox.isChecked()){

                    ifChecked = "1";

                }else {

                    ifChecked = "0";
                    searchRadius = null;
                }



                Intent search_service_intent = new Intent(getActivity(),SearchIntentService.class);
                search_service_intent.putExtra("searchText", searchText);
                search_service_intent.putExtra("ifChecked", ifChecked);
                search_service_intent.putExtra("searchRadius", searchRadius);
                search_service_intent.putExtra("units", units);


                getActivity().startService(search_service_intent);

            }
        });

        distanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distanceTV.setText("Current Search Radius: " + progress + " ("+ units + ")");
                searchRadius = progress + "";
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
