package com.irvanyale.app.footballapp.presenter

import android.util.Log
import com.google.gson.Gson
import com.irvanyale.app.footballapp.main.MainView
import com.irvanyale.app.footballapp.model.DetailTeamResponse
import com.irvanyale.app.footballapp.model.MatchResponse
import com.irvanyale.app.footballapp.model.MatchSearchResponse
import com.irvanyale.app.footballapp.network.ApiRepository
import com.irvanyale.app.footballapp.network.TheSportDBApi
import com.irvanyale.app.footballapp.utils.CoroutineContextProvider
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class MatchPresenter(private val view: MainView,
                    private val apiRepository: ApiRepository,
                    private val gson: Gson, private val context: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getPrevMatches(leagueId: String?) {
        view.showLoading()
        async(UI) {
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getPrevMatch(leagueId)),
                        MatchResponse::class.java
                )
            }
            view.showMatch(data.await().matches)
            view.hideLoading()
        }
    }

    fun getNextMatches(leagueId: String?) {
        view.showLoading()
        async(UI) {
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getNextMatch(leagueId)),
                        MatchResponse::class.java
                )
            }
            view.showMatch(data.await().matches)
            view.hideLoading()
        }
    }

    fun getDetailMatches(matchId: String?) {
        view.showLoading()
        async(UI) {
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getDetailMatch(matchId)),
                        MatchResponse::class.java
                )
            }
            view.showMatch(data.await().matches)
            view.hideLoading()
        }
    }

    fun getHomeTeamLogo(teamId: String?) {
        async(context.main) {
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getTeamLogo(teamId)),
                        DetailTeamResponse::class.java
                )
            }
            view.showHomeTeamLogo(data.await().teams)
        }
    }

    fun getAwayTeamLogo(teamId: String?) {
        async(context.main) {
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getTeamLogo(teamId)),
                        DetailTeamResponse::class.java
                )
            }
            view.showAwayTeamLogo(data.await().teams)
        }
    }

    fun getSearchMatch(query: String?) {
        view.showLoading()
        async(UI) {
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getSearchMatch(query)),
                        MatchSearchResponse::class.java
                )
            }
            Log.d("LOL", "DATA "+data.await().matches)
            view.showMatch(data.await().matches)
            view.hideLoading()
        }
    }
}