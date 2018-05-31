package com.irvanyale.app.footballapp.presenter

import com.google.gson.Gson
import com.irvanyale.app.footballapp.main.TeamsView
import com.irvanyale.app.footballapp.model.TeamResponse
import com.irvanyale.app.footballapp.network.ApiRepository
import com.irvanyale.app.footballapp.network.TheSportDBApi
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class TeamsPresenter(private val view: TeamsView,
                     private val apiRepository: ApiRepository,
                     private val gson: Gson) {

    fun getTeamList(league: String?) {
        view.showLoading()
        doAsync {
            val data = gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getTeams(league)),
                    TeamResponse::class.java
            )

            uiThread {
                view.hideLoading()
                view.showTeamList(data.teams)
            }
        }
    }

}