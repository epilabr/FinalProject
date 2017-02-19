package com.example.laptop.finalproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import static java.lang.Double.parseDouble;


/*====this fragment is to display the users favorites=====*/


public class FavoritesFragment extends Fragment {

    SimpleCursorAdapter adapter;
    ArrayList<Place> myFavoritesList;
    ViewPager mViewPager;


    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        DbCommands commands= new DbCommands(getActivity());

        myFavoritesList = (ArrayList)commands.getAllPlaces();  //put the data saved base favorites into ArrayList

        ListView favoritesPlacesListView = (ListView) getView().findViewById(R.id.favorites_fragment_list);

        MyFavoritesAdapter adapter = new MyFavoritesAdapter(getActivity(), R.id.place_name, myFavoritesList); //uses adapter to arrange and display list objects

        favoritesPlacesListView.setAdapter(adapter);

        registerForContextMenu(favoritesPlacesListView);

        adapter.notifyDataSetChanged();





        favoritesPlacesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = myFavoritesList.get(position).name;
                String address = myFavoritesList.get(position).address;
                String lat = myFavoritesList.get(position).latitude;
                String lng = myFavoritesList.get(position).longitude;


                Intent intent = new Intent(getActivity(), MyMapActivity.class);

                intent.putExtra("name",name);
                intent.putExtra("address",address);
                intent.putExtra("lat",lat);
                intent.putExtra("lng",lng);
                startActivity(intent);

            }
        });

    }
}
