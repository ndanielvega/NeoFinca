package com.ddannielvega.neofinca;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class FincasAdapter extends FragmentStatePagerAdapter {

    int numOfTabs;

    public FincasAdapter(FragmentManager fm, int numOfTabs){
        super(fm);
        this.numOfTabs = numOfTabs;
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new FincasFragmentTab1();
            case 1:
                return new FincasFragmentTab2();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
