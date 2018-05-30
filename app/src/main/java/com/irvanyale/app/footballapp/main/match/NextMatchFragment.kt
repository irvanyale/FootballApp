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
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Spinner
import com.irvanyale.app.footballapp.R
import com.irvanyale.app.footballapp.model.Match
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class NextMatchFragment : Fragment(), AnkoComponent<Context>, OnItemClickCallback {

    private var matches: MutableList<Match> = mutableListOf()
    private lateinit var adapter: MatchAdapter

    private lateinit var rllyMatchNotAvailable: RelativeLayout
    private lateinit var listMatch: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var spinner: Spinner

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val spinnerItems = resources.getStringArray(R.array.league)
        val spinnerAdapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, spinnerItems)
        spinner.adapter = spinnerAdapter

        adapter = MatchAdapter(matches, this)
        listMatch.adapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(ctx))
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui){
        linearLayout {
            lparams(width = matchParent, height = matchParent)
            padding = dip(16)
            orientation = LinearLayout.VERTICAL

            spinner = spinner {}.lparams(width = matchParent, height = wrapContent){
                bottomMargin = dip(10)
            }

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
                        }.lparams(width = dip(100), height = dip(100)){
                            centerInParent()
                        }
                    }

                    listMatch = recyclerView {
                        id = R.id.rly_match
                        visibility = View.VISIBLE
                        padding = dip(10)
                        layoutManager = LinearLayoutManager(ctx)
                        addItemDecoration(DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL))

                    }.lparams(width = matchParent, height = matchParent)
                }
            }
        }
    }

    inline fun ViewManager.customMatchListLayout(theme: Int = 0, init: MatchListLayout.() -> Unit): MatchListLayout {
        return ankoView({ MatchListLayout(it) }, theme = theme, init = init)
    }

    override fun onItemClicked(match: Match) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}