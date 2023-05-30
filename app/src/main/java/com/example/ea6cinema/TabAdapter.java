package com.example.ea6cinema;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {

    private final int numberTabs;

    /*This tab adapter is backing a data of two fragments from the main activity.
      When data is changed then associated views will be updated for */

    public TabAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        numberTabs = behavior;
    }

    /*This method will be called from main activity when tabAdapter.notifyDataSetChanged() is called.*/
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new TodaysFragment();
        }
        else if(position == 1){
            return new TodaysFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return numberTabs;
    }
}
