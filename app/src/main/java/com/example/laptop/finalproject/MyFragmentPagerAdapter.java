package com.example.laptop.finalproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/*=========this class is for the pager adapter==================*/
class MyFragmentPagerAdapter extends FragmentPagerAdapter{

    MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new SearchFragment();
        } else
            return new FavoritesFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
