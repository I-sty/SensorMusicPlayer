package com.kalosis.sensormusicplayer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

  SectionsPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public int getCount() {
    // Show 3 total pages.
    return 3;
  }

  @Override
  public Fragment getItem(int position) {
    // getItem is called to instantiate the fragment for the given page.
    // Return a PlaceholderFragment (defined as a static inner class below).
    return PlaceholderFragment.newInstance(position + 1);
  }
}
