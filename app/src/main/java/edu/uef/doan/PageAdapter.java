package edu.uef.doan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
public class PageAdapter extends FragmentPagerAdapter {
    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AssignmentTab();
            case 1:
                return new AssignmentSubmittedTab();
            default:
                return new AssignmentSubmittedTab();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Đã giao";
            case 1:
                return "Đã nộp";
            default:
                return super.getPageTitle(position);
        }
    }
}
