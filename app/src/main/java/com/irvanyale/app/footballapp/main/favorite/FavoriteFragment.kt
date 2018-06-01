package com.irvanyale.app.footballapp.main.favorite

import android.content.Context
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.irvanyale.app.footballapp.R
import com.irvanyale.app.footballapp.main.PagerAdapter
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.viewPager

class FavoriteFragment: Fragment(), AnkoComponent<Context> {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var toolbar: Toolbar

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        setupViewpager(viewPager)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(ctx))
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui){
        coordinatorLayout {
            lparams(width = matchParent, height = matchParent)
            fitsSystemWindows = true

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {

                toolbar = themedToolbar {
                    R.style.ThemeOverlay_AppCompat_Light
                    backgroundColor = ContextCompat.getColor(ctx, R.color.colorPrimary)
                }.lparams(width = matchParent, height = wrapContent) {
                    scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                }

                tabLayout = tabLayout{
                    id = R.id.tabLayout_match
                    lparams(width = matchParent, height = wrapContent)
                    backgroundColor = ContextCompat.getColor(ctx, R.color.colorPrimary)
                    tabTextColors = ContextCompat.getColorStateList(ctx, R.color.tab_color_selector)
                    tabMode = TabLayout.MODE_FIXED
                }.lparams(width = matchParent, height = wrapContent){
                    scrollFlags = 0
                }

            }.lparams(width = matchParent, height = wrapContent)

            viewPager = viewPager {
                id = R.id.viewPager_match
            }.lparams(width = matchParent, height = matchParent){
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        }
    }

    private fun setupViewpager(viewPager: ViewPager){
        val viewPagerAdapter = PagerAdapter(childFragmentManager)
        viewPagerAdapter.addFragment(MatchFavoriteFragment(), getString(R.string.matches))
        viewPagerAdapter.addFragment(TeamFavoriteFragment(), getString(R.string.teams))
        viewPager.adapter = viewPagerAdapter
    }
}