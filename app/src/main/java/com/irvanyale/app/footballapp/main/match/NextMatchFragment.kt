package com.irvanyale.app.footballapp.main.match

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
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
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class NextMatchFragment : Fragment(), AnkoComponent<Context>, MainView, OnItemClickCallback {

    private var matches: MutableList<Match> = mutableListOf()
    private lateinit var adapter: MatchAdapter

    private lateinit var rllyMatchNotAvailable: RelativeLayout
    private lateinit var listMatch: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var spinner: Spinner
    private lateinit var presenter: MatchPresenter
    private lateinit var leagueId: String

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()

        val spinnerItems = resources.getStringArray(R.array.league)
        val leagueIds = resources.getIntArray(R.array.leagueId)

        val spinnerAdapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, spinnerItems)
        spinner.adapter = spinnerAdapter

        adapter = MatchAdapter(matches, this)
        listMatch.adapter = adapter

        val request = ApiRepository()
        val gson = Gson()

        presenter = MatchPresenter(this, request, gson, TestContextProvider())

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                leagueId = leagueIds[position].toString()
                presenter.getNextMatches(leagueId)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        swipeRefresh.onRefresh {
            presenter.getNextMatches(leagueId)
        }
    }

    private fun initView(){
        swipeRefresh = find(R.id.match_swipeRefresh)
        spinner = find(R.id.match_spinner)
        listMatch = find(R.id.match_recyclerView)
        rllyMatchNotAvailable = find(R.id.match_relativeLayout)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(ctx))
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui){
        customMatchListLayout {}
    }

    inline fun ViewManager.customMatchListLayout(theme: Int = 0, init: MatchListLayout.() -> Unit): MatchListLayout {
        return ankoView({ MatchListLayout(it) }, theme = theme, init = init)
    }

    override fun onItemClicked(match: Match) {
        ctx.startActivity<DetailMatchActivity>(DetailMatchActivity.FOOTBALL_MATCH_ID to match.matchId)
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