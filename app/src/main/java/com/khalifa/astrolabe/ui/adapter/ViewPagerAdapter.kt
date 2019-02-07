package com.khalifa.astrolabe.ui.adapter

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.khalifa.astrolabe.AstrolabeApplication

/**
 * @author Ahmad Khalifa
 */

class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

    private val fragments = ArrayList<Fragment>()

    private val fragmentsTitleRes = ArrayList<Int>()

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    fun addFragment(fragment: Fragment,
                    tabLayout: TabLayout?,
                    @StringRes titleResId: Int,
                    @DrawableRes iconResId: Int) {
        fragments.add(fragment)
        fragmentsTitleRes.add(titleResId)
        tabLayout?.getTabAt(count - 1)?.setIcon(iconResId)
    }

    override fun getPageTitle(position: Int) =
            AstrolabeApplication.getString(fragmentsTitleRes[position])
}