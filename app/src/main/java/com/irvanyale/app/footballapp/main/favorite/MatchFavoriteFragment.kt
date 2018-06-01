package com.irvanyale.app.footballapp.main.favorite

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import com.irvanyale.app.footballapp.db.database
import android.view.View
import android.view.ViewGroup
import com.irvanyale.app.footballapp.db.MatchFavorite
import com.irvanyale.app.footballapp.main.match.DetailMatchActivity
import com.irvanyale.app.footballapp.main.match.MatchAdapter
import com.irvanyale.app.footballapp.main.match.OnItemClickCallback
import com.irvanyale.app.footballapp.model.Match
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx

class MatchFavoriteFragment : Fragment(), AnkoComponent<Context>, OnItemClickCallback {

    private var matches: MutableList<Match> = mutableListOf()
    private lateinit var adapter: MatchAdapter

    private lateinit var listMatch: RecyclerView
    private lateinit var leagueId: String

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = MatchAdapter(matches, this)
        listMatch.adapter = adapter

        showFavorite()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(ctx))
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui){
        linearLayout {
            lparams(width = matchParent, height = matchParent)

            listMatch = recyclerView {
                padding = dip(10)
                layoutManager = LinearLayoutManager(ctx)
                addItemDecoration(DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL))

            }.lparams(width = matchParent, height = matchParent)
        }
    }

    private fun showFavorite(){
        ctx.database.use {
            val result = select(MatchFavorite.TABLE_MATCH_FAVORITE)
            val favorite = result.parseList(classParser<MatchFavorite>())

            matches.clear()
            for (data in favorite){
                matches.add(Match(data.matchId,
                        data.date,
                        data.homeTeam,
                        data.awayTeam,
                        data.homeScore,
                        data.awayScore))
            }

            adapter.notifyDataSetChanged()
        }
    }

    override fun onItemClicked(match: Match) {
        ctx.startActivity<DetailMatchActivity>(DetailMatchActivity.FOOTBALL_MATCH_ID to match.matchId)
    }

    override fun onResume() {
        super.onResume()

        matches.clear()
        adapter.notifyDataSetChanged()
        showFavorite()
    }
}