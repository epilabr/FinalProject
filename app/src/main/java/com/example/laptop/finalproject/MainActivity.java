package com.example.laptop.finalproject;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;


public class MainActivity extends AppCompatActivity {

    ViewPager mViewPager;
    MyPowerConnectionReceiver chargingStateReceiver;

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
}
