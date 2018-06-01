package com.irvanyale.app.footballapp.main.team

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import com.irvanyale.app.footballapp.R
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.themedAppBarLayout

class PlayerDetailActivity : AppCompatActivity() {

    companion object {
        const val FOOTBALL_PLAYER_ID: String = "FootballPlayerId"
        const val FOOTBALL_PLAYER_NAME: String = "FootballPlayerName"
    }

    private lateinit var toolbar: Toolbar

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


        }
    }
}
