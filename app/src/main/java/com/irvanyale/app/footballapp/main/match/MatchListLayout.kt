package com.irvanyale.app.footballapp.main.match

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.irvanyale.app.footballapp.R
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class MatchListLayout(ctx: Context): LinearLayout(ctx) {

    init {
        linearLayout {
            lparams(width = matchParent, height = matchParent)
            padding = dip(16)
            orientation = LinearLayout.VERTICAL

            spinner {
                id = R.id.match_spinner
            }.lparams(width = matchParent, height = wrapContent){
                bottomMargin = dip(10)
            }

            view {
                background = ContextCompat.getDrawable(ctx, R.drawable.shadow)
            }.lparams(width = matchParent, height = dip(1))

            swipeRefreshLayout {
                id = R.id.match_swipeRefresh
                setColorSchemeResources(R.color.colorAccent,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light)

                relativeLayout {
                    lparams(width = matchParent, height = matchParent)

                    relativeLayout {
                        lparams(width = matchParent, height = matchParent)
                        id = R.id.match_relativeLayout
                        visibility = View.GONE

                        imageView {
                            image = ContextCompat.getDrawable(ctx, R.drawable.ic_event_busy)
                            scaleType = ImageView.ScaleType.CENTER_INSIDE
                        }.lparams(width = wrapContent, height = wrapContent){
                            centerHorizontally()
                            topMargin = dip(20)
                        }
                    }

                    recyclerView {
                        id = R.id.match_recyclerView
                        visibility = View.VISIBLE
                        padding = dip(10)
                        layoutManager = LinearLayoutManager(ctx)
                        addItemDecoration(DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL))

                    }.lparams(width = matchParent, height = matchParent)
                }
            }
        }
    }
}