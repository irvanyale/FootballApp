package com.irvanyale.app.footballapp.presenter

import com.google.gson.Gson
import com.irvanyale.app.footballapp.main.PlayersView
import com.irvanyale.app.footballapp.model.PlayerDetailResponse
import com.irvanyale.app.footballapp.model.PlayerResponse
import com.irvanyale.app.footballapp.network.ApiRepository
import com.irvanyale.app.footballapp.network.TheSportDBApi
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class PlayerPresenter(private val view: PlayersView,
                     private val apiRepository: ApiRepository,
                     private val gson: Gson) {

    fun getAllPlayers(team: String?) {
        view.showLoading()
        async(UI) {
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getAllPlayers(team)),
                        PlayerResponse::class.java
                )
            }
            view.showPlayerList(data.await().player)
            view.hideLoading()
        }
    }

    fun getDetailPlayer(playerId: String?) {
        view.showLoading()
        async(UI) {
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getDetailPlayer(playerId)),
                        PlayerDetailResponse::class.java
                )
            }
            view.showPlayerList(data.await().players)
            view.hideLoading()
        }
    }
}