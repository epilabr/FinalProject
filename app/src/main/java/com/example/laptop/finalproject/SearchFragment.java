package com.example.laptop.finalproject;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/*====this fragments purpose is to display the search results the user preformed and send the search details to the SearchIntentService=====*/
public class SearchFragment extends Fragment {
    ArrayList<Place> jasonPlaces = new ArrayList<>();
    Button searchButton;
    String searchText;
    CheckBox searchCheckBox;   //if checked will preform proximity search
    String ifChecked;
    SeekBar distanceSeekBar;  //user uses seekBar to determine the search radius
    String searchRadius;
    TextView distanceTV;  //shows the search radius in a TextView
    String units;  //meters or feet





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

        searchButton = (Button)getView().findViewById(R.id.search_button);

        searchCheckBox = (CheckBox)getView().findViewById(R.id.search_check_Box);

        distanceSeekBar = (SeekBar)getView().findViewById(R.id.distance_seek_bar);

        distanceTV = (TextView)getView().findViewById(R.id.distance_tv);

        units = "meters";

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchText = ((EditText)getView().findViewById(R.id.search_text)).getText().toString(); //captures the text in search field to "searchText"

                final InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);  //these two lines are to make the keyboard disappear when the search starts(SearchIntentService) starts.


                if (searchCheckBox.isChecked()){  //if the user has checked the checkbox, proximity search will be preformed

                    ifChecked = "1";

                }else {

                    ifChecked = "0";
                    searchRadius = null;
                }


                Intent search_service_intent = new Intent(getActivity(),SearchIntentService.class);
                /*=====extra data to be sent to the search intent service=====*/
                search_service_intent.putExtra("searchText", searchText);
                search_service_intent.putExtra("ifChecked", ifChecked);
                search_service_intent.putExtra("searchRadius", searchRadius);
                search_service_intent.putExtra("units", units);
                /*=====extra data to be sent to the search intent service ends=====*/

                getActivity().startService(search_service_intent);  //starts the search

            }
        });

        distanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { //sets the seekBar ready and listening
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

        IntentFilter filter0 = new IntentFilter("finalProject.STARTED"); // this filter catches the start message from the SearchIntentService

        IntentFilter filter = new IntentFilter("finalProject.FINISHED");// this filter catches the finish message from the SearchIntentService

        FinishedReceiver receiver = new FinishedReceiver(); //used one receiver but two filters

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(  receiver , filter0);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(  receiver , filter);

    }

    class FinishedReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("finalProject.STARTED" )){
                Toast.makeText(getActivity(), "Searching, please stand by...", Toast.LENGTH_SHORT).show(); // shows toast when search starts
            }

            if (intent.getAction().equals("finalProject.FINISHED" )) {
                Toast.makeText(getActivity(), "Search completed!", Toast.LENGTH_SHORT).show(); // shows toast when search ends

                String json = intent.getExtras().getString("json");  // extracts the json from the intent
                 /*============parses the json======================*/
                try {
                    JSONObject mainObject = new JSONObject(json);
                    JSONArray results = mainObject.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++){

                        JSONObject location = results.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
                        String name = results.getJSONObject(i).optString("name");
                        String formatted_address = results.getJSONObject(i).optString("formatted_address");
                        double lati = location.getDouble("lat");
                        double lngi = location.getDouble("lng");
                        String picture = results.getJSONObject(i).getJSONArray("photos").getJSONObject(0).optString("photo_reference");
                        String sLati = "" + lati;
                        String sLng = "" + lngi;
                        /*============end of parsing the json======================*/

                        Place tempPlace = new Place(name, sLati, sLng, formatted_address, picture); //call constructor
                        jasonPlaces.add(tempPlace); //creates an ArrayList of places
                    }


                }
                catch (JSONException e){
                    e.printStackTrace();
                }

                ListView placesList = (ListView) getView().findViewById(R.id.search_fragment_list);
                MyListAdapter adapter = new MyListAdapter(getActivity(), R.id.place_name, jasonPlaces); //uses adapter to arrange list objects
                placesList.setAdapter(adapter);
            }
        }
    }
}
