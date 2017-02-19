package com.example.laptop.finalproject;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static java.lang.Double.parseDouble;


/*====this fragments purpose is to display the search results the user preformed and send the search details to the SearchIntentService=====*/
public class SearchFragment extends Fragment {


    static ArrayList<Place> jasonPlaces = new ArrayList<>();
    static ArrayList<Place> mySearchList;
    Button searchButton;
    String searchText;
    CheckBox searchCheckBox;   //if checked will preform proximity search
    String ifChecked;
    SeekBar distanceSeekBar;  //user uses seekBar to determine the search radius
    String searchRadius;
    TextView distanceTV;  //shows the search radius in a TextView
    String units;  //meters or feet
    static ViewPager dViewPager;
    String formatted_address;
    ImageView animationImageView;
    RippleBackground rippleBackground;
    MyListAdapter adapter = null;
    String myLocationLat;
    String myLocationLng;
    public static int itemPosition;
    public static ListView listView;
    public static View itemView;
    ListView placesListView;
    static boolean saved;


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


        rippleBackground=(RippleBackground)getActivity().findViewById(R.id.content);
        animationImageView=(ImageView)getActivity().findViewById(R.id.centerImage);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                IsNetworkConnected inc = new IsNetworkConnected(getActivity());
                inc.isNetworkConnected();

                searchText = ((EditText)getView().findViewById(R.id.search_text)).getText().toString(); //captures the text in search field to "searchText"

                final InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);  //these two lines are to make the keyboard disappear when the search starts(SearchIntentService) starts.


                animationImageView.setVisibility(View.VISIBLE);
                rippleBackground.startRippleAnimation();

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
                search_service_intent.putExtra("myLocationLat", myLocationLat);
                search_service_intent.putExtra("myLocationLng", myLocationLng);
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
        public void onReceive(Context context, final Intent intent) {

            if (intent.getAction().equals("finalProject.STARTED" )){
                Toast.makeText(getActivity(), "Searching, please stand by...", Toast.LENGTH_SHORT).show(); // shows toast when search starts


                rippleBackground.startRippleAnimation();
                animationImageView.setVisibility(View.VISIBLE);
            }

            if (intent.getAction().equals("finalProject.FINISHED" )) {
                Toast.makeText(getActivity(), "Search completed!", Toast.LENGTH_SHORT).show(); // shows toast when search ends


                rippleBackground.stopRippleAnimation();
                animationImageView.setVisibility(View.GONE);

                jasonPlaces.clear();

                String json = intent.getExtras().getString("json");  // extracts the json from the intent
                myLocationLat = intent.getExtras().getString("lat");
                myLocationLng = intent.getExtras().getString("lng");


                 /*============parses the json======================*/
                try {
                    JSONObject mainObject = new JSONObject(json);
                    Log.d("tag", mainObject.toString(4));
                    JSONArray results = mainObject.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {

                        JSONObject location = results.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
                        String name = results.getJSONObject(i).optString("name");
                        if (results.getJSONObject(i).has("formatted_address")) {

                            formatted_address = results.getJSONObject(i).optString("formatted_address");

                        } else {

                            formatted_address = results.getJSONObject(i).optString("vicinity");

                        }

                        double lati = location.getDouble("lat");
                        double lngi = location.getDouble("lng");
                        String photoImage;
                        if (results.getJSONObject(i).has("photos")) {

                            String photoReference = results.getJSONObject(i).getJSONArray("photos").getJSONObject(0).optString("photo_reference");
                            photoImage = "https://maps.googleapis.com/maps/api/place/photo?maxheight=110&maxwidth=110&photoreference=" + photoReference + "&key=AIzaSyDmDuBBI1JVwkKp8VtmyIwzhz4Nujl_Xvo";

                        } else {
                            photoImage = "no_picture";
                        }

                        String sLati = "" + lati;
                        String sLng = "" + lngi;
                        /*============end of parsing the json======================*/

                        Place tempPlace = new Place(name, formatted_address, sLati, sLng, photoImage); //call constructor
                        jasonPlaces.add(tempPlace); //creates an ArrayList of places
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (jasonPlaces.size() > 0){
                    DbCommands commands = new DbCommands(getActivity());
                    commands.deleteSearchPlacesDB();
                    placesListView = (ListView) getView().findViewById(R.id.search_fragment_list);
                    adapter = new MyListAdapter(getActivity(), R.id.place_name, jasonPlaces); //uses adapter to arrange list objects
                    placesListView.setAdapter(adapter);
                    saved = false;   //A flag if to load the saved list
                }










                placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        String name = jasonPlaces.get(position).name;
                        String address = jasonPlaces.get(position).address;
                        String lat = jasonPlaces.get(position).latitude;
                        String lng = jasonPlaces.get(position).longitude;


                        Intent mapIntent = new Intent(getActivity(),MyMapActivity.class);
                        mapIntent.putExtra("name", name);
                        mapIntent.putExtra("address", address);
                        mapIntent.putExtra("lat", lat);
                        mapIntent.putExtra("lng", lng);
                        mapIntent.putExtra("myLocationLat", myLocationLat);
                        mapIntent.putExtra("myLocationLng", myLocationLng);

                        startActivity(mapIntent);

                    }
                });


                placesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                        ImplicitIntentDialog newFragment = new ImplicitIntentDialog();
                        newFragment.show(getActivity().getFragmentManager(), "dialog");
                        itemPosition = position;
                        itemView = view;
                        return true;
                    }
                });


            }

        }

    }



    @Override
    public void onResume() {
        super.onResume();




                DbCommands commands = new DbCommands(getActivity());

                mySearchList = (ArrayList) commands.getAllPlacesSearch();  //put the data saved base search into ArrayList

                placesListView  = (ListView) getView().findViewById(R.id.search_fragment_list);

                MyFavoritesAdapter adapter = new MyFavoritesAdapter(getActivity(), R.id.place_name, mySearchList); //uses adapter to arrange and display list objects

                placesListView.setAdapter(adapter);

                registerForContextMenu(placesListView);

                adapter.notifyDataSetChanged();

                saved = true;

        placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String name = mySearchList.get(position).name;
                String address = mySearchList.get(position).address;
                String lat = mySearchList.get(position).latitude;
                String lng = mySearchList.get(position).longitude;


                Intent mapIntent = new Intent(getActivity(),MyMapActivity.class);
                mapIntent.putExtra("name", name);
                mapIntent.putExtra("address", address);
                mapIntent.putExtra("lat", lat);
                mapIntent.putExtra("lng", lng);
                mapIntent.putExtra("myLocationLat", myLocationLat);
                mapIntent.putExtra("myLocationLng", myLocationLng);

                startActivity(mapIntent);

            }
        });


        placesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                ImplicitIntentDialog newFragment = new ImplicitIntentDialog();
                newFragment.show(getActivity().getFragmentManager(), "dialog");
                itemPosition = position;
                itemView = view;
                return true;
            }
        });

    }


    public static class ImplicitIntentDialog extends DialogFragment{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setItems(R.array.dialog_options, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    int position = itemPosition;

                    String lat;
                    String lng;
                    String name;
                    String address;

                    if (saved){
                        lat = mySearchList.get(position).latitude;
                        lng = mySearchList.get(position).longitude;
                        name = mySearchList.get(position).name;
                        address = mySearchList.get(position).address;
                    }else {
                        lat = jasonPlaces.get(position).latitude;
                        lng = jasonPlaces.get(position).longitude;
                        name = jasonPlaces.get(position).name;
                        address = jasonPlaces.get(position).address;
                    }

                    switch (which) {

                        case(0):

                            listView = (ListView)getActivity().findViewById(R.id.search_fragment_list);

                            ImageView imageView;
                            imageView = (ImageView)itemView.findViewById(R.id.place_image);

                            Bitmap bitmapToSave = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bitmapToSave .compress(Bitmap.CompressFormat.PNG, 100, bos);
                            byte[] picToSaveByteArray = bos.toByteArray();

                            Place place = new Place(name, address, lat, lng, picToSaveByteArray);

                            DbCommands commands = new DbCommands(getActivity());

                            commands.addPlaceFavorites(place);

                            Toast.makeText(getActivity(), "Added a new Place!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);

                            break;

                        case (1):

                            String uri = "http://maps.google.com/?q="+ lat +","+ lng+(name)+"   " + name +" "+address;
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            String shareSub = "Here is the location of " + name;
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, uri);
                            startActivity(Intent.createChooser(sharingIntent, "Share via"));
                            break;

                        case (2):

                            Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            startActivity(mapIntent);

                            break;

                        default:
                            break;
                    }
                }
            });
            return builder.create();
        }
    }

}
