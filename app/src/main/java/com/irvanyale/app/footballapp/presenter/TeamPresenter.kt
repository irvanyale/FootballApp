package com.irvanyale.app.footballapp.presenter

import com.google.gson.Gson
import com.irvanyale.app.footballapp.main.TeamsView
import com.irvanyale.app.footballapp.model.TeamResponse
import com.irvanyale.app.footballapp.network.ApiRepository
import com.irvanyale.app.footballapp.network.TheSportDBApi
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class TeamsPresenter(private val view: TeamsView,
                     private val apiRepository: ApiRepository,
                     private val gson: Gson) {

    fun getTeamList(league: String?) {
        view.showLoading()
        async(UI) {
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getTeams(league)),
                        TeamResponse::class.java
                )
            }
            view.showTeamList(data.await().teams)
            view.hideLoading()
        }
    }

    fun getTeamListBySearch(name: String?) {
        view.showLoading()
        async(UI) {
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getTeamsBySearch(name)),
                        TeamResponse::class.java
                )
            }
            view.showTeamList(data.await().teams)
            view.hideLoading()
        }
    }

    fun getTeamDetail(teamId: String?) {
        view.showLoading()
        async(UI) {
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getTeamDetail(teamId)),
                        TeamResponse::class.java
                )
            }
            view.showTeamList(data.await().teams)
            view.hideLoading()
        }
    }
}