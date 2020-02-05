package com.example.canteenapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    private int numberOfTabItems;

    PageAdapter(FragmentManager fm, int numberOfTabItems) {
        super(fm);
        this.numberOfTabItems = numberOfTabItems;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CategoryFragment();
            case 1:
                return new OrderFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabItems;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Menu";
            case 1:
                return "Order";
            default:
                return super.getPageTitle(position);
        }
    }
}
