package com.irvanyale.app.footballapp.main.team

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
import android.widget.ImageView
import android.widget.RelativeLayout
import com.irvanyale.app.footballapp.R
import com.irvanyale.app.footballapp.model.Player
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class TeamPlayersFragment : Fragment(), AnkoComponent<Context>, OnItemClickCallback {

    private var players: MutableList<Player> = mutableListOf()
    private lateinit var adapter: TeamPlayersAdapter

    private lateinit var rllyMatchNotAvailable: RelativeLayout
    private lateinit var listPlayer: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        addDummy(players)

        adapter = TeamPlayersAdapter(context, players, this)
        listPlayer.adapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(ctx))
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        linearLayout {
            lparams(width = matchParent, height = matchParent)
            padding = dip(16)

            swipeRefresh = swipeRefreshLayout {
                setColorSchemeResources(R.color.colorAccent,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light)

                rllyMatchNotAvailable = relativeLayout {
                    lparams(width = matchParent, height = matchParent)

                    relativeLayout {
                        lparams(width = matchParent, height = matchParent)
                        visibility = View.GONE

                        imageView {
                            image = ContextCompat.getDrawable(ctx, R.drawable.ic_event_busy)
                            scaleType = ImageView.ScaleType.CENTER_INSIDE
                        }.lparams(width = wrapContent, height = wrapContent){
                            centerHorizontally()
                            topMargin = dip(20)
                        }
                    }

                    listPlayer = recyclerView {
                        visibility = View.VISIBLE
                        padding = dip(10)
                        layoutManager = LinearLayoutManager(ctx)
                        addItemDecoration(DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL))

                    }.lparams(width = matchParent, height = matchParent)
                }
            }
        }
    }

    override fun onItemClicked(player: Player) {
        ctx.startActivity<PlayerDetailActivity>(
                PlayerDetailActivity.FOOTBALL_PLAYER_ID to player.playerId,
                PlayerDetailActivity.FOOTBALL_PLAYER_NAME to player.playerName)
    }

    private fun addDummy(player: MutableList<Player>){
        for (index in 1..10){
            val pl = Player()
            pl.playerId = "1"
            pl.playerName = "C.Ronaldo"
            pl.playerPosition = "Forward"
            pl.playerPic = ""
            player.add(pl)
        }
    }
}