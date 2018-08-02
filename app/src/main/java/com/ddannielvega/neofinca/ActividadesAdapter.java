package com.ddannielvega.neofinca;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ActividadesAdapter extends FragmentStatePagerAdapter {

    private int numOfTabs;

    public ActividadesAdapter(FragmentManager fm, int numOfTabs){
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
                 return new ActividadesFragmentTab1();
             case 1:
                 return new ActividadesFragmentTab2();
             default:
                 return null;
         }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}