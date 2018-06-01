package com.irvanyale.app.footballapp.main

import com.irvanyale.app.footballapp.model.Team

interface TeamsView {
    fun showLoading()
    fun hideLoading()
    fun showTeamList(data: List<Team>?)
}