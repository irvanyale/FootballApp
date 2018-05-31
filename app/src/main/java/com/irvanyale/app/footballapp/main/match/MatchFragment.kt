package com.irvanyale.app.footballapp.main.match

import android.content.Context
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.AppBarLayout.LayoutParams.*
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.*
import com.irvanyale.app.footballapp.R
import com.irvanyale.app.footballapp.R.string.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.viewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Layout
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.*
import org.jetbrains.anko.support.v4.find


class MatchFragment : Fragment(), AnkoComponent<Context> {

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
                    scrollFlags = SCROLL_FLAG_SCROLL or SCROLL_FLAG_ENTER_ALWAYS
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
        val viewPagerAdapter = MatchPagerAdapter(childFragmentManager)
        viewPagerAdapter.addFragment(NextMatchFragment(), getString(title_next))
        viewPagerAdapter.addFragment(PrevMatchFragment(), getString(title_prev))
        viewPager.adapter = viewPagerAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            R.id.action_icon_search -> activity?.startActivity<MatchSearchActivity>()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater?.inflate(R.menu.menu_icon_search, menu)
    }
}
