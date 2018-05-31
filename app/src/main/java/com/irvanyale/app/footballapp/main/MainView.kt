package com.irvanyale.app.footballapp.main

import com.irvanyale.app.footballapp.model.Match
import com.irvanyale.app.footballapp.model.DetailTeam

interface MainView {
    fun showLoading()
    fun hideLoading()
    fun showMatch(data: List<Match>?)
    fun showHomeTeamLogo(data: List<DetailTeam>?)
    fun showAwayTeamLogo(data: List<DetailTeam>?)
}