package com.szollosi.sensormusicplayer

import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

import com.szollosi.sensormusicplayer.fragments.FragmentX
import com.szollosi.sensormusicplayer.fragments.FragmentXYZ
import com.szollosi.sensormusicplayer.fragments.FragmentY
import com.szollosi.sensormusicplayer.fragments.FragmentZ

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to one of the sections/tabs/pages.
 */
class SectionsPagerAdapter internal constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val titles = listOf("Page XYZ", "Page X", "Page Y", "Page Z")

    // Sparse array to keep track of registered fragments in memory
    private val registeredFragments = SparseArray<Fragment>()

    // Register the fragment when the item is instantiated
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        registeredFragments.put(position, fragment)
        return fragment
    }

    // Unregister when the item is inactive
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    // Returns the fragment for the position (if instantiated)
    fun getRegisteredFragment(position: Int): Fragment = registeredFragments.get(position)

    override fun getCount(): Int = MyConstants.NUMBER_OF_AVAILABLE_SCREENS.toInt()

    // getItem is called to instantiate the fragment for the given page.
    override fun getItem(position: Int): Fragment = when (position) {
        0 -> FragmentXYZ.newInstance(0, titles[position])
        1 -> FragmentX.newInstance(1, titles[position])
        2 -> FragmentY.newInstance(2, titles[position])
        3 -> FragmentZ.newInstance(3, titles[position])
        else -> FragmentXYZ.newInstance(0, titles[0])
    }

    // Returns the page title for the top indicator
    override fun getPageTitle(position: Int) = titles[position]
}
