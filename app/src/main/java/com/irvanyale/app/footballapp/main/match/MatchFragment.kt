package com.irvanyale.app.footballapp.main.match

import android.content.Context
import android.content.res.ColorStateList
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.irvanyale.app.footballapp.R
import com.irvanyale.app.footballapp.R.string.title_next
import com.irvanyale.app.footballapp.R.string.title_prev
import org.jetbrains.anko.*
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.bottomNavigationView
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.viewPager

class MatchFragment :Fragment(), AnkoComponent<Context> {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupViewpager(viewPager)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(ctx))
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui){
        relativeLayout {
            lparams(width = matchParent, height = matchParent)

            tabLayout = tabLayout {
                id = R.id.tabLayout_match
                lparams(width = matchParent, height = wrapContent)
                backgroundColor = ContextCompat.getColor(ctx, R.color.colorPrimary)

                tabTextColors = ContextCompat.getColorStateList(ctx, R.color.tab_color_selector)

                tabMode = TabLayout.MODE_FIXED
            }.lparams(width = matchParent, height = wrapContent){
                alignParentTop()
            }

            viewPager = viewPager {
                id = R.id.viewPager_match
            }.lparams(width = matchParent, height = matchParent){
                below(R.id.tabLayout_match)
            }
        }
    }

    fun setupViewpager(viewPager: ViewPager){
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPagerAdapter.addFragment(NextMatchFragment(), getString(title_next))
        viewPagerAdapter.addFragment(PrevMatchFragment(), getString(title_prev))
        viewPager.adapter = viewPagerAdapter
    }

    class ViewPagerAdapter(fragmentManager: FragmentManager) :
            FragmentPagerAdapter(fragmentManager) {

        private var fragmentList: MutableList<Fragment> = mutableListOf()
        private var titleList: MutableList<String> = mutableListOf()

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
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
}
