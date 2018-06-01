package com.irvanyale.app.footballapp.main.favorite

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.irvanyale.app.footballapp.db.TeamFavorite
import com.irvanyale.app.footballapp.db.database
import com.irvanyale.app.footballapp.main.team.TeamDetailActivity
import com.irvanyale.app.footballapp.main.team.TeamsAdapter
import com.irvanyale.app.footballapp.model.Team
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx

class TeamFavoriteFragment : Fragment(), AnkoComponent<Context> {

    private var teams: MutableList<Team> = mutableListOf()
    private lateinit var adapter: TeamsAdapter

    private lateinit var listMatch: RecyclerView

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = TeamsAdapter(teams){
            ctx.startActivity<TeamDetailActivity>(TeamDetailActivity.FOOTBALL_TEAM_ID to "${it.teamId}")
        }
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
            val result = select(TeamFavorite.TABLE_TEAM_FAVORITE)
            val favorite = result.parseList(classParser<TeamFavorite>())

            teams.clear()
            for (data in favorite){
                teams.add(Team(data.teamId,
                        data.teamName,
                        data.teamBadge,
                        data.teamFormedYear,
                        data.teamStadium,
                        data.teamDesc))
            }

            adapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()

        teams.clear()
        adapter.notifyDataSetChanged()
        showFavorite()
    }
}