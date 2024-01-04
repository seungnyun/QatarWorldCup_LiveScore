package com.example.qatarworldcup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class VPAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> items;
    private ArrayList<String> itext = new ArrayList<String>();
    public VPAdapter(FragmentManager fm) {
        super(fm);
        items = new ArrayList<Fragment>();
        items.add(new Fragment1());
        items.add(new Fragment2());
        items.add(new Fragment3());
        items.add(new Fragment4());
        items.add(new Fragment5());
        items.add(new Fragment6());
        items.add(new Fragment7());
        items.add(new Fragment8());

        itext.add("A조");
        itext.add("B조");
        itext.add("C조");
        itext.add("D조");
        itext.add("E조");
        itext.add("F조");
        itext.add("G조");
        itext.add("H조");


    }

    @Nullable
    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    public CharSequence getPageTitle(int position){
        return itext.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }


}