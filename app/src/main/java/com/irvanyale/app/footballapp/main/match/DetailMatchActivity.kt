package com.irvanyale.app.footballapp.main.match

import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.irvanyale.app.footballapp.R
import com.google.gson.Gson
import com.irvanyale.app.footballapp.R.drawable.ic_add_to_favorites
import com.irvanyale.app.footballapp.R.drawable.ic_added_to_favorites
import com.irvanyale.app.footballapp.R.id.add_to_favorite
import com.irvanyale.app.footballapp.R.menu.detail_menu
import com.irvanyale.app.footballapp.db.database
import com.irvanyale.app.footballapp.main.MainView
import com.irvanyale.app.footballapp.model.DetailTeam
import com.irvanyale.app.footballapp.model.Match
import com.irvanyale.app.footballapp.network.ApiRepository
import com.irvanyale.app.footballapp.presenter.MatchPresenter
import com.irvanyale.app.footballapp.db.MatchFavorite
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import java.text.SimpleDateFormat
import java.util.*

class DetailMatchActivity : AppCompatActivity(), MainView {

    companion object {
        const val FOOTBALL_MATCH_ID: String = "FootballMatchId"
    }

    private lateinit var toolbar: Toolbar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var presenter: MatchPresenter
    private lateinit var match: Match
    private lateinit var matchId: String

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private var isDataReady: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        coordinatorLayout {
            lparams(width = matchParent, height = matchParent)
            fitsSystemWindows = true

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {

                toolbar = themedToolbar {
                    R.style.ThemeOverlay_AppCompat_Light
                    backgroundColor = ContextCompat.getColor(ctx, R.color.colorPrimary)
                }.lparams(width = matchParent, height = wrapContent) {
                    scrollFlags = 0
                }

            }.lparams(width = matchParent, height = wrapContent)

            linearLayout {
                lparams(width = matchParent, height = matchParent)
                padding = dip(16)
                orientation = LinearLayout.VERTICAL

                swipeRefresh = swipeRefreshLayout {
                    setColorSchemeResources(R.color.colorAccent,
                            android.R.color.holo_green_light,
                            android.R.color.holo_orange_light,
                            android.R.color.holo_red_light)

                    scrollView {
                        lparams(width = matchParent, height = wrapContent)
                        linearLayout {
                            orientation = LinearLayout.VERTICAL
                            lparams(width = matchParent, height = matchParent)
                            leftPadding = dip(10)
                            rightPadding = dip(10)
                            topPadding = dip(20)
                            bottomPadding = dip(20)

                            //Date
                            textView {
                                id = R.id.txt_date_match
                                textSize = 18f
                                textColor = Color.MAGENTA

                            }.lparams(width = matchParent, height = wrapContent){
                                bottomMargin = dip(20)
                            }.gravity = Gravity.CENTER_HORIZONTAL

                            //Score
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                weightSum = 1f

                                relativeLayout {
                                    padding = dip(4)

                                    linearLayout {
                                        orientation = LinearLayout.VERTICAL

                                        imageView {
                                            id = R.id.txt_homeTeamLogo
                                        }.lparams(width = dip(60), height = dip(60)){
                                            gravity = Gravity.CENTER
                                        }

                                        textView {
                                            id = R.id.txt_home_team
                                            textSize = 14f
                                            textColor = Color.BLACK

                                        }.lparams(width = wrapContent, height = matchParent){
                                            gravity = Gravity.CENTER
                                        }.gravity = Gravity.CENTER

                                    }.lparams(width = wrapContent, height = matchParent){
                                        alignParentLeft()
                                        leftOf(R.id.txt_home_score)
                                        gravity = Gravity.CENTER
                                    }

                                    textView {
                                        id = R.id.txt_home_score
                                        textSize = dip(20).toFloat()
                                        textColor = Color.BLACK

                                    }.lparams(width = wrapContent, height = matchParent){
                                        alignParentRight()
                                        gravity = Gravity.CENTER_VERTICAL
                                    }.gravity = Gravity.CENTER

                                }.lparams(width = dip(0), height = wrapContent){
                                    weight = 0.45f
                                    gravity = RelativeLayout.CENTER_VERTICAL
                                }

                                textView {
                                    text = ctx.getString(R.string.v)
                                    textSize = 18f
                                    textColor = Color.BLACK
                                    padding = dip(4)

                                }.lparams(width = dip(0), height = matchParent){
                                    weight = 0.1f
                                    gravity = Gravity.CENTER_HORIZONTAL
                                }.gravity = Gravity.CENTER

                                relativeLayout {
                                    padding = dip(4)

                                    linearLayout {
                                        orientation = LinearLayout.VERTICAL

                                        imageView {
                                            id = R.id.txt_awayTeamLogo
                                        }.lparams(width = dip(60), height = dip(60)){
                                            gravity = Gravity.CENTER
                                        }

                                        textView {
                                            id = R.id.txt_away_team
                                            textSize = 14f
                                            textColor = Color.BLACK

                                        }.lparams(width = wrapContent, height = matchParent){
                                            gravity = Gravity.CENTER
                                        }.gravity = Gravity.CENTER

                                    }.lparams(width = wrapContent, height = matchParent){
                                        alignParentRight()
                                        rightOf(R.id.txt_away_score)
                                        gravity = Gravity.CENTER
                                    }

                                    textView {
                                        id = R.id.txt_away_score
                                        textSize = dip(20).toFloat()
                                        textColor = Color.BLACK

                                    }.lparams(width = wrapContent, height = matchParent){
                                        alignParentLeft()
                                        gravity = Gravity.CENTER_HORIZONTAL
                                    }.gravity = Gravity.CENTER

                                }.lparams(width = dip(0), height = wrapContent){
                                    weight = 0.45f
                                    gravity = RelativeLayout.CENTER_VERTICAL
                                }
                            }.lparams(width = matchParent, height = dip(120)){
                                bottomMargin = dip(10)
                            }

                            //Divider
                            view {
                                backgroundColor = ContextCompat.getColor(ctx, android.R.color.black)
                            }.lparams(width = matchParent, height = dip(1))

                            //Goals & Shots
                            relativeLayout {
                                padding = dip(6)
                                linearLayout {
                                    weightSum = 1f

                                    textView {
                                        id = R.id.txt_homeGoalDetails
                                        textSize = 12f
                                        textColor = Color.BLACK
                                    }.lparams(width = dip(0), height = wrapContent){
                                        weight = 0.4f
                                    }.gravity = Gravity.START

                                    textView {
                                        text = ctx.getString(R.string.goals)
                                        textSize = 12f
                                        textColor = Color.BLUE
                                    }.lparams(width = dip(0), height = wrapContent){
                                        weight = 0.2f
                                    }.gravity = Gravity.CENTER_HORIZONTAL

                                    textView {
                                        id = R.id.txt_awayGoalDetails
                                        textSize = 12f
                                        textColor = Color.BLACK
                                    }.lparams(width = dip(0), height = wrapContent){
                                        weight = 0.4f
                                    }.gravity = Gravity.END

                                }.lparams(width = matchParent, height = wrapContent){
                                    bottomMargin = dip(20)
                                    alignParentTop()
                                }

                                linearLayout {
                                    weightSum = 1f

                                    textView {
                                        id = R.id.txt_homeShots
                                        textSize = 12f
                                        textColor = Color.BLACK
                                    }.lparams(width = dip(0), height = wrapContent){
                                        weight = 0.4f
                                    }.gravity = Gravity.START

                                    textView {
                                        text = ctx.getString(R.string.shots)
                                        textSize = 12f
                                        textColor = Color.BLUE
                                    }.lparams(width = dip(0), height = wrapContent){
                                        weight = 0.2f
                                    }.gravity = Gravity.CENTER_HORIZONTAL

                                    textView {
                                        id = R.id.txt_awayShots
                                        textSize = 12f
                                        textColor = Color.BLACK
                                    }.lparams(width = dip(0), height = wrapContent){
                                        weight = 0.4f
                                    }.gravity = Gravity.END

                                }.lparams(width = matchParent, height = wrapContent){
                                    alignParentBottom()
                                }
                            }.lparams(width = matchParent, height = wrapContent)

                            //Divider
                            view {
                                backgroundColor = ContextCompat.getColor(ctx, android.R.color.black)
                            }.lparams(width = matchParent, height = dip(1))

                            //Lineups
                            linearLayout {
                                padding = dip(6)
                                orientation = LinearLayout.VERTICAL

                                textView {
                                    text = ctx.getString(R.string.lineup)
                                    textSize = 14f
                                    textColor = Color.BLACK
                                }.lparams(width = matchParent, height = wrapContent){
                                    bottomMargin = dip(10)
                                }.gravity = Gravity.CENTER_HORIZONTAL

                                //Goal Keeper
                                linearLayout {
                                    weightSum = 1f

                                    textView {
                                        id = R.id.txt_homeLineupGoalkeeper
                                        textSize = 12f
                                        textColor = Color.BLACK
                                    }.lparams(width = dip(0), height = wrapContent){
                                        weight = 0.4f
                                    }.gravity = Gravity.START

                                    textView {
                                        text = ctx.getString(R.string.goalkeeper)
                                        textSize = 12f
                                        textColor = Color.BLUE
                                    }.lparams(width = dip(0), height = wrapContent){
                                        weight = 0.2f
                                    }.gravity = Gravity.CENTER_HORIZONTAL

                                    textView {
                                        id = R.id.txt_awayLineupGoalkeeper
                                        textSize = 12f
                                        textColor = Color.BLACK
                                    }.lparams(width = dip(0), height = wrapContent){
                                        weight = 0.4f
                                    }.gravity = Gravity.END

                                }.lparams(width = matchParent, height = wrapContent){
                                    bottomMargin = dip(10)
                                }

                                //Defender
                                linearLayout {
                                    weightSum = 1f

                                    textView {
                                        id = R.id.txt_homeLineupDefense
                                        textSize = 12f
                                        textColor = Color.BLACK
                                    }.lparams(width = dip(0), height = wrapContent){
                                        weight = 0.4f
                                    }.gravity = Gravity.START

                                    textView {
                                        text = ctx.getString(R.string.defender)
                                        textSize = 12f
                                        textColor = Color.BLUE
                                    }.lparams(width = dip(0), height = wrapContent){
                                        weight = 0.2f
                                    }.gravity = Gravity.CENTER_HORIZONTAL

                                    textView {
                                        id = R.id.txt_awayLineupDefense
                                        textSize = 12f
                                        textColor = Color.BLACK
                                    }.lparams(width = dip(0), height = wrapContent){
                                        weight = 0.4f
                                    }.gravity = Gravity.END

                                }.lparams(width = matchParent, height = wrapContent){
                                    bottomMargin = dip(10)
                                }

                                //Midfielder
                                linearLayout {
                                    weightSum = 1f

                                    textView {
                                        id = R.id.txt_homeLineupMidfield
                                        textSize = 12f
                                        textColor = Color.BLACK
                                    }.lparams(width = dip(0), height = wrapContent){
                                        weight = 0.4f
                                    }.gravity = Gravity.START

                                    textView {
                                        text = ctx.getString(R.string.midfielder)
                                        textSize = 12f
                                        textColor = Color.BLUE
                                    }.lparams(width = dip(0), height = wrapContent){
                                        weight = 0.2f
                                    }.gravity = Gravity.CENTER_HORIZONTAL

                                    textView {
                                        id = R.id.txt_awayLineupMidfield
                                        textSize = 12f
                                        textColor = Color.BLACK
                                    }.lparams(width = dip(0), height = wrapContent){
                                        weight = 0.4f
                                    }.gravity = Gravity.END

                                }.lparams(width = matchParent, height = wrapContent){
                                    bottomMargin = dip(10)
                                }

                                //Forward
                                linearLayout {
                                    weightSum = 1f

                                    textView {
                                        id = R.id.txt_homeLineupForward
                                        textSize = 12f
                                        textColor = Color.BLACK
                                    }.lparams(width = dip(0), height = wrapContent){
                                        weight = 0.4f
                                    }.gravity = Gravity.START

                                    textView {
                                        text = ctx.getString(R.string.forward)
                                        textSize = 12f
                                        textColor = Color.BLUE
                                    }.lparams(width = dip(0), height = wrapContent){
                                        weight = 0.2f
                                    }.gravity = Gravity.CENTER_HORIZONTAL

                                    textView {
                                        id = R.id.txt_awayLineupForward
                                        textSize = 12f
                                        textColor = Color.BLACK
                                    }.lparams(width = dip(0), height = wrapContent){
                                        weight = 0.4f
                                    }.gravity = Gravity.END

                                }.lparams(width = matchParent, height = wrapContent){
                                    bottomMargin = dip(10)
                                }

                                //Substitutes
                                linearLayout {
                                    weightSum = 1f

                                    textView {
                                        id = R.id.txt_homeLineupSubstitutes
                                        textSize = 12f
                                        textColor = Color.BLACK
                                    }.lparams(width = dip(0), height = wrapContent){
                                        weight = 0.4f
                                    }.gravity = Gravity.START

                                    textView {
                                        text = ctx.getString(R.string.substitutes)
                                        textSize = 12f
                                        textColor = Color.BLUE
                                    }.lparams(width = dip(0), height = wrapContent){
                                        weight = 0.2f
                                    }.gravity = Gravity.CENTER_HORIZONTAL

                                    textView {
                                        id = R.id.txt_awayLineupSubstitutes
                                        textSize = 12f
                                        textColor = Color.BLACK
                                    }.lparams(width = dip(0), height = wrapContent){
                                        weight = 0.4f
                                    }.gravity = Gravity.END

                                }.lparams(width = matchParent, height = wrapContent){
                                    bottomMargin = dip(10)
                                }

                            }.lparams(width = matchParent, height = wrapContent)
                        }
                    }
                }.lparams(width = matchParent, height = matchParent){
                    topMargin = dip(20)
                }
            }.lparams(width = matchParent, height = matchParent){
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        }

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Match Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        matchId = intent.getStringExtra(FOOTBALL_MATCH_ID)

        swipeRefresh.onRefresh {
            presenter.getDetailMatches(matchId)
        }

        favoriteState()
        val request = ApiRepository()
        val gson = Gson()

        presenter = MatchPresenter(this, request, gson)
        presenter.getDetailMatches(matchId)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(detail_menu, menu)
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
            add_to_favorite -> {
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

    private fun initData(data: Match?){
        if (data != null){
            var formatedDate = "-"
            if (data.date != null){
                val dates: Date = SimpleDateFormat("yyyy-MM-dd").parse(data.date)
                formatedDate = SimpleDateFormat("EEEE, dd MMMM yyyy").format(dates)
            }

            find<TextView>(R.id.txt_date_match).text = formatedDate
            find<TextView>(R.id.txt_home_team).text = data.homeTeam?: "-"
            find<TextView>(R.id.txt_away_team).text = data.awayTeam?: "-"
            find<TextView>(R.id.txt_home_score).text = data.homeScore?: "-"
            find<TextView>(R.id.txt_away_score).text = data.awayScore?: "-"

            var homeGoals = "-"
            var awayGoals = "-"
            if (data.homeGoalDetails != null){
                homeGoals = data.homeGoalDetails?:"-"
            }
            if (data.awayGoalDetails != null){
                awayGoals = data.awayGoalDetails?: "-"
            }

            find<TextView>(R.id.txt_homeGoalDetails).text = homeGoals.replace(";", "\n")
            find<TextView>(R.id.txt_awayGoalDetails).text = awayGoals.replace(";", "\n")
            find<TextView>(R.id.txt_homeShots).text = data.homeShots?: "-"
            find<TextView>(R.id.txt_awayShots).text = data.awayShots?: "-"

            var homeLineupGoalkeeper = "-"
            var awayLineupGoalkeeper = "-"
            if (data.homeLineupGoalkeeper != null){
                homeLineupGoalkeeper = data.homeLineupGoalkeeper?:"-"
            }
            if (data.awayLineupGoalkeeper != null){
                awayLineupGoalkeeper = data.awayLineupGoalkeeper?: "-"
            }

            find<TextView>(R.id.txt_homeLineupGoalkeeper).text = homeLineupGoalkeeper.replace("; ", "\n")
            find<TextView>(R.id.txt_awayLineupGoalkeeper).text = awayLineupGoalkeeper.replace("; ", "\n")

            var homeLineupDefense = "-"
            var awayLineupDefense = "-"
            if (data.homeLineupDefense != null){
                homeLineupDefense = data.homeLineupDefense?:"-"
            }
            if (data.awayLineupDefense != null){
                awayLineupDefense = data.awayLineupDefense?: "-"
            }

            find<TextView>(R.id.txt_homeLineupDefense).text = homeLineupDefense.replace("; ", "\n")
            find<TextView>(R.id.txt_awayLineupDefense).text = awayLineupDefense.replace("; ", "\n")

            var homeLineupMidfield = "-"
            var awayLineupMidfield = "-"
            if (data.homeLineupMidfield != null){
                homeLineupMidfield = data.homeLineupMidfield?:"-"
            }
            if (data.awayLineupMidfield != null){
                awayLineupMidfield = data.awayLineupMidfield?: "-"
            }

            find<TextView>(R.id.txt_homeLineupMidfield).text = homeLineupMidfield.replace("; ", "\n")
            find<TextView>(R.id.txt_awayLineupMidfield).text = awayLineupMidfield.replace("; ", "\n")

            var homeLineupForward = "-"
            var awayLineupForward = "-"
            if (data.homeLineupForward != null){
                homeLineupForward = data.homeLineupForward?:"-"
            }
            if (data.awayLineupForward != null){
                awayLineupForward = data.awayLineupForward?: "-"
            }

            find<TextView>(R.id.txt_homeLineupForward).text = homeLineupForward.replace("; ", "\n")
            find<TextView>(R.id.txt_awayLineupForward).text = awayLineupForward.replace("; ", "\n")

            var homeLineupSubstitutes = "-"
            var awayLineupSubstitutes = "-"
            if (data.homeLineupSubstitutes != null){
                homeLineupSubstitutes = data.homeLineupSubstitutes?:"-"
            }
            if (data.awayLineupSubstitutes != null){
                awayLineupSubstitutes = data.awayLineupSubstitutes?: "-"
            }

            find<TextView>(R.id.txt_homeLineupSubstitutes).text = homeLineupSubstitutes.replace("; ", "\n")
            find<TextView>(R.id.txt_awayLineupSubstitutes).text = awayLineupSubstitutes.replace("; ", "\n")
        }
    }

    private fun addToFavorite(){
        try {
            database.use {
                insert(MatchFavorite.TABLE_MATCH_FAVORITE,
                        MatchFavorite.MATCH_ID to match.matchId,
                        MatchFavorite.MATCH_DATE to match.date,
                        MatchFavorite.HOME_TEAM to match.homeTeam,
                        MatchFavorite.AWAY_TEAM to match.awayTeam,
                        MatchFavorite.HOME_SCORE to match.homeScore,
                        MatchFavorite.AWAY_SCORE to match.awayScore)
            }
            snackbar(swipeRefresh, "Added to favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(swipeRefresh, e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite(){
        try {
            database.use {
                delete(MatchFavorite.TABLE_MATCH_FAVORITE, "(MATCH_ID = {id})",
                        "id" to matchId)
            }
            snackbar(swipeRefresh, "Removed to favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(swipeRefresh, e.localizedMessage).show()
        }
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_added_to_favorites)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_add_to_favorites)
    }

    private fun favoriteState(){
        database.use {
            val result = select(MatchFavorite.TABLE_MATCH_FAVORITE)
                    .whereArgs("(MATCH_ID = {id})",
                            "id" to matchId)
            val favorite = result.parseList(classParser<MatchFavorite>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }

    override fun showLoading() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefresh.isRefreshing = false
    }

    override fun showMatch(data: List<Match>?) {
        swipeRefresh.isRefreshing = false
        if (data != null && data.isNotEmpty()){

            match = Match(data[0].matchId,
                    data[0].date,
                    data[0].homeTeam,
                    data[0].awayTeam,
                    data[0].homeScore,
                    data[0].awayScore)

            initData(data[0])
            presenter.getHomeTeamLogo(data[0].idHomeTeam)
            presenter.getAwayTeamLogo(data[0].idAwayTeam)
            isDataReady = true
        }
    }

    override fun showHomeTeamLogo(data: List<DetailTeam>?) {
        if (data != null && data.isNotEmpty()){
            if (!data[0].teamLogo.isNullOrEmpty()){
                Picasso.get().load(data[0].teamLogo).fit().into(find<ImageView>(R.id.txt_homeTeamLogo))
            }
        }
    }

    override fun showAwayTeamLogo(data: List<DetailTeam>?) {
        if (data != null && data.isNotEmpty()){
            if (!data[0].teamLogo.isNullOrEmpty()){
                Picasso.get().load(data[0].teamLogo).fit().into(find<ImageView>(R.id.txt_awayTeamLogo))
            }
        }
    }
}