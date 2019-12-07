package com.szollosi.sensormusicplayer;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.szollosi.sensormusicplayer.fragments.FragmentX;
import com.szollosi.sensormusicplayer.fragments.FragmentXYZ;
import com.szollosi.sensormusicplayer.fragments.FragmentY;
import com.szollosi.sensormusicplayer.fragments.FragmentZ;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

  // Sparse array to keep track of registered fragments in memory
  private SparseArray<Fragment> registeredFragments = new SparseArray<>();

  SectionsPagerAdapter(FragmentManager fm) {
    super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
  }

  // Register the fragment when the item is instantiated
  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    Fragment fragment = (Fragment) super.instantiateItem(container, position);
    registeredFragments.put(position, fragment);
    return fragment;
  }

  // Unregister when the item is inactive
  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    registeredFragments.remove(position);
    super.destroyItem(container, position, object);
  }

  // Returns the fragment for the position (if instantiated)
  public Fragment getRegisteredFragment(int position) {
    return registeredFragments.get(position);
  }

  @Override
  public int getCount() {
    return Constants.NUMBER_OF_AVAILABLE_SCREENS;
  }

  @Override
  @NonNull
  public Fragment getItem(int position) {
    // getItem is called to instantiate the fragment for the given page.
    switch (position) {
      case 0: // Fragment # 0 - This will show FirstFragment
        return FragmentXYZ.newInstance(0, "Page XYZ");
      case 1: // Fragment # 0 - This will show FirstFragment different title
        return FragmentX.newInstance(1, "Page # X");
      case 2: // Fragment # 1 - This will show SecondFragment
        return FragmentY.newInstance(2, "Page # Y");
      case 3: // Fragment # 1 - This will show SecondFragment
        return FragmentZ.newInstance(3, "Page # Z");
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
