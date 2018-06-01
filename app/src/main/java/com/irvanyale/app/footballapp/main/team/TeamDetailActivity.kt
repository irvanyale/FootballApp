package com.irvanyale.app.footballapp.main.team

import android.database.sqlite.SQLiteConstraintException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.AppBarLayout.LayoutParams.*
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.irvanyale.app.footballapp.R
import com.irvanyale.app.footballapp.db.TeamFavorite
import com.irvanyale.app.footballapp.db.database
import com.irvanyale.app.footballapp.main.PagerAdapter
import com.irvanyale.app.footballapp.main.TeamsView
import com.irvanyale.app.footballapp.model.Team
import com.irvanyale.app.footballapp.network.ApiRepository
import com.irvanyale.app.footballapp.presenter.TeamsPresenter
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.design.*
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import org.jetbrains.anko.support.v4.viewPager

class TeamDetailActivity : AppCompatActivity(), TeamsView {

    companion object {
        const val FOOTBALL_TEAM_ID: String = "FootballTeamId"
    }

    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var toolbar: Toolbar
    private lateinit var teamLogoImgview: ImageView
    private lateinit var teamNameTxtview: TextView
    private lateinit var teamEstTxtview: TextView
    private lateinit var teamStadiumTxtview: TextView
    private lateinit var team: Team
    private lateinit var teamId: String
    private lateinit var teamOverview: String
    private lateinit var teamName: String
    private lateinit var presenter: TeamsPresenter

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private var isDataReady: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        coordinatorLayout = coordinatorLayout {
            lparams(width = matchParent, height = matchParent)
            fitsSystemWindows = true

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {

                collapsingToolbarLayout {

                    toolbar = themedToolbar {
                        R.style.ThemeOverlay_AppCompat_Light
                        backgroundColor = ContextCompat.getColor(ctx, R.color.colorPrimary)
                    }.lparams(width = matchParent, height = dip(300)) {
                        collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX
                    }

                    linearLayout {
                        orientation = LinearLayout.VERTICAL

                        teamLogoImgview = imageView().lparams(width = dip(100), height = dip(100)){
                            gravity = Gravity.CENTER_HORIZONTAL
                            bottomMargin = dip(10)
                        }

                        teamNameTxtview = textView{
                            textSize = sp(10).toFloat()
                            gravity = Gravity.CENTER
                        }.lparams(width = matchParent, height = wrapContent){
                            gravity = Gravity.CENTER
                        }

                        teamEstTxtview = textView{
                            textSize = sp(6).toFloat()
                            gravity = Gravity.CENTER
                        }.lparams(width = matchParent, height = wrapContent){
                            gravity = Gravity.CENTER
                        }

                        teamStadiumTxtview = textView{
                            textSize = sp(8).toFloat()
                            gravity = Gravity.CENTER
                        }.lparams(width = matchParent, height = wrapContent){
                            gravity = Gravity.CENTER
                            bottomMargin = dip(10)
                        }

                    }.lparams(width = matchParent, height = wrapContent){
                        gravity = Gravity.CENTER
                    }

                    tabLayout = tabLayout{
                        lparams(width = matchParent, height = wrapContent)
                        backgroundColor = ContextCompat.getColor(ctx, R.color.colorPrimary)
                        tabTextColors = ContextCompat.getColorStateList(ctx, R.color.tab_color_selector)
                        tabMode = TabLayout.MODE_FIXED
                    }.lparams(width = matchParent, height = wrapContent){
                        gravity = Gravity.BOTTOM
                    }

                }.lparams(width = matchParent, height = matchParent) {
                    scrollFlags = SCROLL_FLAG_SCROLL or SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED
                }

            }.lparams(width = matchParent, height = wrapContent)

            swipeRefresh = swipeRefreshLayout {
                setColorSchemeResources(R.color.colorAccent,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light)

                viewPager = viewPager{
                    id = R.id.viewPager_match
                }

            }.lparams(width = matchParent, height = matchParent){
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        }

        teamId = intent.getStringExtra(FOOTBALL_TEAM_ID) ?: "-"

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        teamOverview = "-"
        teamName = "-"

        val request = ApiRepository()
        val gson = Gson()

        favoriteState()
        presenter = TeamsPresenter(this, request, gson)
        presenter.getTeamDetail(teamId)

        swipeRefresh.onRefresh {
            presenter.getTeamDetail(teamId)
        }
    }

    private fun setupViewpager(viewPager: ViewPager){
        val viewPagerAdapter = PagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(TeamOverviewFragment.newInstance(teamOverview), getString(R.string.title_overview))
        viewPagerAdapter.addFragment(TeamPlayersFragment.newInstance(teamName), getString(R.string.title_player))
        viewPager.adapter = viewPagerAdapter
    }

    private fun initData(data: Team?){
        if (data != null){
            Picasso.get().load(data.teamBadge).fit().into(teamLogoImgview)
            teamNameTxtview.text = data.teamName ?: "-"
            teamEstTxtview.text = data.teamFormedYear ?: "-"
            teamStadiumTxtview.text = data.teamStadium ?: "-"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.add_to_favorite -> {
                if (isDataReady){
                    if (isFavorite) removeFromFavorite() else addToFavorite()

                    isFavorite = !isFavorite
                    setFavorite()
                } else {
                    toast("Please wait ...").show()
                }

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun favoriteState(){
        database.use {
            val result = select(TeamFavorite.TABLE_TEAM_FAVORITE)
                    .whereArgs("(TEAM_ID = {id})",
                            "id" to teamId)
            val favorite = result.parseList(classParser<TeamFavorite>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorites)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites)
    }

    private fun addToFavorite(){
        try {
            database.use {
                insert(TeamFavorite.TABLE_TEAM_FAVORITE,
                        TeamFavorite.TEAM_ID to team.teamId,
                        TeamFavorite.TEAM_NAME to team.teamName,
                        TeamFavorite.TEAM_BADGE to team.teamBadge,
                        TeamFavorite.TEAM_FORMED_YEAR to team.teamFormedYear,
                        TeamFavorite.TEAM_STADIUM to team.teamStadium,
                        TeamFavorite.TEAM_DESC to team.teamDescription)
            }
            snackbar(coordinatorLayout, "Added to favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(coordinatorLayout, e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite(){
        try {
            database.use {
                delete(TeamFavorite.TABLE_TEAM_FAVORITE, "(TEAM_ID = {id})",
                        "id" to teamId)
            }
            snackbar(coordinatorLayout, "Removed to favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(coordinatorLayout, e.localizedMessage).show()
        }
    }

    override fun showLoading() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefresh.isRefreshing = false
    }

    override fun showTeamList(data: List<Team>?) {
        swipeRefresh.isRefreshing = false
        if (data != null && data.isNotEmpty()){

            team = Team(data[0].teamId,
                    data[0].teamName,
                    data[0].teamBadge,
                    data[0].teamFormedYear,
                    data[0].teamStadium,
                    data[0].teamDescription
            )

            teamOverview = data[0].teamDescription ?: "-"
            teamName = data[0].teamName ?: "-"

            initData(data[0])
            setupViewpager(viewPager)
            tabLayout.setupWithViewPager(viewPager)

            isDataReady = true
        }
    }
}