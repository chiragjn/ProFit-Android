package com.modprobe.profit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HomeCarouselAdapter extends FragmentPagerAdapter {
	private static int NUM_ITEMS = 3;
		
        public HomeCarouselAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }
        
        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
 
        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return new Page1TotalFitonsToday();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return new Page2TotalFitonsPerCategory();
            case 2: // Fragment # 1 - This will show SecondFragment
                return new Page3TotalStepsTime();
            default:
            	return null;
            }
        }
        
        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
        	return "Page " + position;
        }
        
    }
