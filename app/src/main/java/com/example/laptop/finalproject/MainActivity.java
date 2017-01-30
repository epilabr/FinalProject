package com.example.laptop.finalproject;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;


public class MainActivity extends AppCompatActivity {

    ViewPager mViewPager;
    MyPowerConnectionReceiver chargingStateReceiver;   //listens to power connection/detach

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);  //this line is to make the keyboard not appear when the app starts.

        mViewPager = (ViewPager) findViewById(R.id.pagercontainer); //sets the view pager in container.

        mViewPager.setAdapter(new MyFragmentPagerAdapter(    //sets the adapter for ViewPager
                getSupportFragmentManager()));

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
}
