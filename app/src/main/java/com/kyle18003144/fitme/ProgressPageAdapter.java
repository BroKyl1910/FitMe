package com.kyle18003144.fitme;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ProgressPageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ProgressPageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    public ProgressPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ProgressWeightFragment tab1 = new ProgressWeightFragment();
                return tab1;
            case 1:
                ProgressFootstepsFragment tab2 = new ProgressFootstepsFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
