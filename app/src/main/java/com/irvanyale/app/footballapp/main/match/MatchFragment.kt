package com.irvanyale.app.footballapp.main.match

import android.content.Context
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.AppBarLayout.LayoutParams.*
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.*
import com.irvanyale.app.footballapp.R
import com.irvanyale.app.footballapp.R.string.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.viewPager

class MatchFragment : Fragment(), AnkoComponent<Context> {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)

        setupViewpager(viewPager)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(ctx))
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui){
        coordinatorLayout {
            lparams(width = matchParent, height = wrapContent)

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                toolbar().lparams(width = matchParent, height = wrapContent) {
                    scrollFlags = SCROLL_FLAG_SCROLL or SCROLL_FLAG_ENTER_ALWAYS or SCROLL_FLAG_SNAP
                }
                tabLayout = tabLayout{
                    id = R.id.tabLayout_match
                    lparams(width = matchParent, height = wrapContent)
                    backgroundColor = ContextCompat.getColor(ctx, R.color.colorPrimary)

                    tabTextColors = ContextCompat.getColorStateList(ctx, R.color.tab_color_selector)

                    tabMode = TabLayout.MODE_FIXED
                }.lparams(width = matchParent, height = wrapContent) {
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
