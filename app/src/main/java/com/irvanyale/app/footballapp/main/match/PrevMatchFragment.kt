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
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.Spinner
import com.irvanyale.app.footballapp.R
import com.irvanyale.app.footballapp.model.Match
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class PrevMatchFragment : Fragment(), AnkoComponent<Context>, OnItemClickCallback {

    private var matches: MutableList<Match> = mutableListOf()
    private lateinit var adapter: MatchAdapter

    private lateinit var rllyMatchNotAvailable: RelativeLayout
    private lateinit var listMatch: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var spinner: Spinner

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()

        val spinnerItems = resources.getStringArray(R.array.league)
        val spinnerAdapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, spinnerItems)
        spinner.adapter = spinnerAdapter

        adapter = MatchAdapter(matches, this)
        listMatch.adapter = adapter
    }

    private fun initView(){
        spinner = find(R.id.match_spinner)
        listMatch = find(R.id.match_relativeLayout)
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}