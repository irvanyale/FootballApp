package com.irvanyale.app.footballapp.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.ContextCompat
import com.irvanyale.app.footballapp.R
import com.irvanyale.app.footballapp.main.favorite.FavoriteFragment
import com.irvanyale.app.footballapp.main.match.MatchFragment
import com.irvanyale.app.footballapp.main.team.TeamFragment
import org.jetbrains.anko.*
import org.jetbrains.anko.design.bottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        relativeLayout {
            lparams(width = matchParent, height = matchParent)

            frameLayout {
                id = R.id.main_container
            }.lparams(width = matchParent, height = matchParent){
                above(R.id.bottom_nav_view)
            }

            view {
                background = ContextCompat.getDrawable(ctx, R.drawable.shadow)
            }.lparams(width = matchParent, height = dip(1)){
                above(R.id.bottom_nav_view)
            }

            bottomNavigationView = bottomNavigationView {
                id = R.id.bottom_nav_view

                inflateMenu(R.menu.menu_nav_bottom)
                itemBackgroundResource = android.R.color.white

            }.lparams(width = matchParent, height = wrapContent){
                alignParentBottom()
            }
        }

        bottomNavigationView.setOnNavigationItemSelectedListener({item ->

            when(item.itemId){
                R.id.action_matches -> loadMatchFragment(savedInstanceState)
                R.id.action_teams -> loadTeamFragment(savedInstanceState)
                R.id.action_favorites -> loadFavoriteFragment(savedInstanceState)
            }

            true
        })

        bottomNavigationView.selectedItemId = R.id.action_matches
    }

    private fun loadMatchFragment(savedInstanceState: Bundle?){
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, MatchFragment(), MatchFragment::class.simpleName)
                    .commit()
        }
    }

    private fun loadTeamFragment(savedInstanceState: Bundle?){
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, TeamFragment(), TeamFragment::class.simpleName)
                    .commit()
        }
    }

    private fun loadFavoriteFragment(savedInstanceState: Bundle?){
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, FavoriteFragment(), FavoriteFragment::class.simpleName)
                    .commit()
        }
    }
}
