package com.example.laptop.finalproject;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.WindowManager;


import java.util.List;

import static java.lang.Double.parseDouble;


public class MainActivity extends AppCompatActivity {

    ViewPager mViewPager;
    MyPowerConnectionReceiver chargingStateReceiver;   //listens to power connection/detach
    Intent appLocationServiceintent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);  //this line is to make the keyboard not appear when the app starts.

        IsNetworkConnected inc = new IsNetworkConnected(this); //perform an internet connection check with proper message to user if not connected
        inc.isNetworkConnected();                              //

        String unites = getString(R.string.metric);
        try{
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String savedUnites = sharedPreferences.getString("unites",null);
                if (savedUnites == null){

                    editor.putString("unites",unites);
                    editor.apply();
                }else {

                }

        }catch (Exception e){

        }







        if ((getResources().getConfiguration().screenLayout &   //checks to see what is the size of my screen
                Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE||(getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) { //loads "regular" fragments if screen is large or xlarge
                Intent intent = new Intent(this, AppLocationService.class);
                this.startService(intent);
        }else{
            mViewPager = (ViewPager) findViewById(R.id.pagercontainer); //sets the view pager in container. (if screen size is normal or small)
            mViewPager.setAdapter(new MyFragmentPagerAdapter(    //sets the adapter for ViewPager
                    getSupportFragmentManager()));
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Bundle bundle = new Bundle();
            bundle.putBoolean("Agree",true);
            MyDialogFragment myDialogFragment = new MyDialogFragment();
            myDialogFragment.setArguments(bundle);
            myDialogFragment.show(getFragmentManager(), "dialog");

        }else{
            Intent intent = new Intent(this, AppLocationService.class);
            this.startService(intent);


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*=================power connected stuff starts==========================*/
        chargingStateReceiver = new MyPowerConnectionReceiver();
        IntentFilter ifilter = new IntentFilter();
        ifilter.addAction(Intent.ACTION_POWER_CONNECTED);
        ifilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(chargingStateReceiver, ifilter);
        /*=================power connected stuff ends==========================*/


        if ((getResources().getConfiguration().screenLayout &   //checks to see what is the size of my screen
                Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE||(getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) { //loads "regular" fragments if screen is large or xlarge
        }else{
            mViewPager = (ViewPager) findViewById(R.id.pagercontainer); //sets the view pager in container. (if screen size is normal or small)
            mViewPager.setAdapter(new MyFragmentPagerAdapter(    //sets the adapter for ViewPager
                    getSupportFragmentManager()));
        }


        /*if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Log.e("intentServiceStart","intentServiceStart");
            Intent intent = new Intent(this, AppLocationService.class);
            this.startService(intent);

        }*/

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(chargingStateReceiver);  //unregisters the charging receiver.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {  //this is the menu which leads to the shared preference in the app
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.preference_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //when this menu is pressed it starts the activity and the fragment involved
        Intent preferenceIntent = new Intent(this, AppPreferenceActivity.class);
        startActivity(preferenceIntent);
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

         if (grantResults.length == 1 ){
             if (permissions[0].equals("android.permission.ACCESS_FINE_LOCATION")&&grantResults[0]==0){

                 List<Fragment> fragments = getSupportFragmentManager().getFragments();
                 if (fragments != null) {
                     for (Fragment fragment : fragments) {
                         fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                     }
                 }

                 appLocationServiceintent = new Intent(this, AppLocationService.class);
                 this.startService(appLocationServiceintent);

             }else {

                 Bundle bundle = new Bundle();
                 bundle.putBoolean("Agree",false);
                 MyDialogFragment myDialogFragment = new MyDialogFragment();
                 myDialogFragment.setArguments(bundle);
                 myDialogFragment.show(getFragmentManager(), "dialog");

             }
         }

    }


    @Override
    protected void onDestroy() {
        try {
            stopService(appLocationServiceintent);
        }catch (Exception e){

        }
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
