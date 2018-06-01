package com.irvanyale.app.footballapp.main

import com.irvanyale.app.footballapp.model.Player

interface PlayersView {
    fun showLoading()
    fun hideLoading()
    fun showPlayerList(data: List<Player>?)
}