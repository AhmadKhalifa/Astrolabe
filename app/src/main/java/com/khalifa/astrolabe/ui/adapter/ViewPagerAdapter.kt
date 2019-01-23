package com.khalifa.astrolabe.ui.adapter

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
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

    private val fragmentsIconRes = ArrayList<Int>()

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    fun addFragment(fragment: Fragment, @StringRes titleResId: Int, @DrawableRes iconResId: Int) {
        fragments.add(fragment)
        fragmentsTitleRes.add(titleResId)
        fragmentsIconRes.add(iconResId)
    }

    override fun getPageTitle(position: Int) =
            AstrolabeApplication.getString(fragmentsTitleRes[position])
}