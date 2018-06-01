package com.irvanyale.app.footballapp.main.team

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.gson.Gson
import com.irvanyale.app.footballapp.R
import com.irvanyale.app.footballapp.main.PlayersView
import com.irvanyale.app.footballapp.model.Player
import com.irvanyale.app.footballapp.network.ApiRepository
import com.irvanyale.app.footballapp.presenter.PlayerPresenter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.notification_template_lines_media.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class PlayerDetailActivity : AppCompatActivity(), PlayersView {

    companion object {
        const val FOOTBALL_PLAYER_ID: String = "FootballPlayerId"
    }

    private var players: MutableList<Player> = mutableListOf()
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var presenter: PlayerPresenter
    private lateinit var imageView: ImageView
    private lateinit var txtvPlayerHeight: TextView
    private lateinit var txtvPlayerWeight: TextView
    private lateinit var txtvPlayerPosition: TextView
    private lateinit var txtvPlayerDesc: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var playerId: String

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
                            padding = dip(10)

                            imageView = imageView().lparams(width = matchParent, height = dip(220)){
                                bottomMargin = dip(16)
                            }

                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                weightSum = 1f

                                linearLayout {
                                    padding = dip(6)
                                    orientation = LinearLayout.VERTICAL

                                    textView {
                                        textSize = sp(8).toFloat()
                                        textColor = Color.BLACK
                                        text = ctx.getString(R.string.height)

                                    }.lparams(width = wrapContent, height = wrapContent){
                                        gravity = Gravity.CENTER_HORIZONTAL
                                    }

                                    txtvPlayerHeight = textView {
                                        textSize = sp(14).toFloat()
                                        textColor = Color.BLACK

                                    }.lparams(width = wrapContent, height = wrapContent){
                                        gravity = Gravity.CENTER_HORIZONTAL
                                    }

                                }.lparams(width = dip(0), height = wrapContent){
                                    weight = 0.5f
                                    gravity = RelativeLayout.CENTER_VERTICAL
                                }

                                linearLayout {
                                    padding = dip(6)
                                    orientation = LinearLayout.VERTICAL

                                    textView {
                                        textSize = sp(8).toFloat()
                                        textColor = Color.BLACK
                                        text = ctx.getString(R.string.weight)

                                    }.lparams(width = wrapContent, height = wrapContent){
                                        gravity = Gravity.CENTER_HORIZONTAL
                                    }

                                    txtvPlayerWeight = textView {
                                        textSize = sp(14).toFloat()
                                        textColor = Color.BLACK

                                    }.lparams(width = wrapContent, height = wrapContent){
                                        gravity = Gravity.CENTER_HORIZONTAL
                                    }

                                }.lparams(width = dip(0), height = wrapContent){
                                    weight = 0.5f
                                    gravity = RelativeLayout.CENTER_VERTICAL
                                }
                            }

                            txtvPlayerPosition = textView {
                                textSize = sp(12).toFloat()
                                textColor = Color.BLACK
                                padding = dip(4)
                                gravity = Gravity.CENTER_HORIZONTAL

                            }.lparams(width = matchParent, height = wrapContent){
                                bottomMargin = dip(8)
                                gravity = Gravity.CENTER
                            }

                            view {
                                backgroundColor = Color.BLACK
                            }.lparams(width = matchParent, height = dip(1)){
                                bottomMargin = dip(12)
                            }

                            txtvPlayerDesc = textView {
                                textSize = sp(6).toFloat()
                            }.lparams(width = matchParent, height = wrapContent){
                                bottomMargin = dip(16)
                            }
                        }
                    }
                }.lparams(width = matchParent, height = matchParent){
                    topMargin = dip(20)
                }
            }.lparams(width = matchParent, height = matchParent){
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        }

        playerId = intent.getStringExtra(FOOTBALL_PLAYER_ID) ?: "-"

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val request = ApiRepository()
        val gson = Gson()

        presenter = PlayerPresenter(this, request, gson)
        presenter.getDetailPlayer(playerId)

        swipeRefresh.onRefresh {
            presenter.getDetailPlayer(playerId)
        }
    }

    private fun initData(data: Player?){
        if (data != null){
            supportActionBar?.title = data.playerName
            Picasso.get().load(data.playerThumb).fit().into(imageView)
            txtvPlayerPosition.text = data.playerPosition
            txtvPlayerDesc.text = data.playerDesc
            txtvPlayerHeight.text = data.playerHeight
            txtvPlayerWeight.text = data.playerWeight
        }
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

    override fun showLoading() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefresh.isRefreshing = false
    }

    override fun showPlayerList(data: List<Player>?) {
        swipeRefresh.isRefreshing = false
        if (data != null && data.isNotEmpty()){
            initData(data[0])
        }
    }
}
