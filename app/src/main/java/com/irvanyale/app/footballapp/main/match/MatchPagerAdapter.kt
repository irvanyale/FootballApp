package com.irvanyale.app.footballapp.main.match

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class MatchPagerAdapter(fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager) {

    private var fragmentList: MutableList<Fragment> = mutableListOf()
    private var titleList: MutableList<String> = mutableListOf()

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        fragment = when (position) {
            0 -> NextMatchFragment()
            1 -> PrevMatchFragment()
            else -> NextMatchFragment()
        }

        return fragment
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        titleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }
}