package com.irvanyale.app.footballapp.main.match

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.gson.Gson
import com.irvanyale.app.footballapp.R
import com.irvanyale.app.footballapp.main.MainView
import com.irvanyale.app.footballapp.model.DetailTeam
import com.irvanyale.app.footballapp.model.Match
import com.irvanyale.app.footballapp.network.ApiRepository
import com.irvanyale.app.footballapp.presenter.MatchPresenter
import com.irvanyale.app.footballapp.utils.TestContextProvider
import com.irvanyale.app.footballapp.utils.gone
import com.irvanyale.app.footballapp.utils.visible
import kotlinx.android.synthetic.main.abc_activity_chooser_view.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.coroutines.onQueryTextListener
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class MatchSearchActivity : AppCompatActivity(), MainView, OnItemClickCallback {

    private var matches: MutableList<Match> = mutableListOf()
    private lateinit var adapter: MatchAdapter

    private lateinit var rllyMatchNotAvailable: RelativeLayout
    private lateinit var listMatch: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var presenter: MatchPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Search Match"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        linearLayout {
            lparams(width = matchParent, height = matchParent)
            padding = dip(16)
            orientation = LinearLayout.VERTICAL

            swipeRefresh = swipeRefreshLayout {
                id = R.id.swipeRefresh
                setColorSchemeResources(R.color.colorAccent,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light)

                relativeLayout {
                    lparams(width = matchParent, height = matchParent)

                    rllyMatchNotAvailable = relativeLayout {
                        visibility = View.GONE

                        imageView {
                            image = ContextCompat.getDrawable(ctx, R.drawable.ic_event_busy)

                        }.lparams(width = wrapContent, height = wrapContent){
                            gravity = Gravity.CENTER
                            centerInParent()
                        }
                    }.lparams(width = matchParent, height = matchParent)

                    listMatch = recyclerView {
                        id = R.id.rly_match
                        visibility = View.VISIBLE
                        padding = dip(10)
                        layoutManager = LinearLayoutManager(ctx)
                        addItemDecoration(DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL))

                    }.lparams(width = matchParent, height = matchParent)
                }
            }.lparams(width = matchParent, height = matchParent)
        }

        adapter = MatchAdapter(matches, this)
        listMatch.adapter = adapter

        val request = ApiRepository()
        val gson = Gson()

        presenter = MatchPresenter(this, request, gson, TestContextProvider())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        if (menuInflater != null && menu != null){
            menuInflater.inflate(R.menu.menu_search, menu)
            val searchItem = menu.findItem(R.id.action_search)
            if (searchItem != null){
                val searchView = searchItem.actionView as SearchView

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        presenter.getSearchMatch(newText)
                        return true
                    }

                })
            }
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onItemClicked(match: Match) {
        startActivity<DetailMatchActivity>(DetailMatchActivity.FOOTBALL_MATCH_ID to match.matchId)
    }

    override fun showLoading() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefresh.isRefreshing = false
    }

    override fun showMatch(data: List<Match>?) {
        swipeRefresh.isRefreshing = false
        matches.clear()
        if (data != null){
            matches.addAll(data)
            listMatch.visible()
            rllyMatchNotAvailable.gone()
        } else {
            listMatch.gone()
            rllyMatchNotAvailable.visible()
        }
        adapter.notifyDataSetChanged()
    }

    override fun showHomeTeamLogo(data: List<DetailTeam>?) {}

    override fun showAwayTeamLogo(data: List<DetailTeam>?) {}
}
