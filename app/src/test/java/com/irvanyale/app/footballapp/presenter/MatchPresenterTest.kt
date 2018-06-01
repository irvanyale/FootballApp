package com.irvanyale.app.footballapp.presenter

import com.google.gson.Gson
import com.irvanyale.app.footballapp.main.MainView
import com.irvanyale.app.footballapp.model.DetailTeam
import com.irvanyale.app.footballapp.model.DetailTeamResponse
import com.irvanyale.app.footballapp.network.ApiRepository
import com.irvanyale.app.footballapp.network.TheSportDBApi
import com.irvanyale.app.footballapp.utils.TestContextProvider
import org.junit.Test

import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MatchPresenterTest {

    private lateinit var presenter: MatchPresenter

    @Mock
    private
    lateinit var view: MainView

    @Mock
    private
    lateinit var gson: Gson

    @Mock
    private
    lateinit var apiRepository: ApiRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = MatchPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun testGetDetailMatch(){
        val teams: MutableList<DetailTeam> = mutableListOf()
        val response = DetailTeamResponse(teams)
        val teamId = "133604"

        Mockito.`when`(gson.fromJson(apiRepository
                .doRequest(TheSportDBApi.getTeamLogo(teamId)),
                DetailTeamResponse::class.java
        )).thenReturn(response)

        presenter.getHomeTeamLogo(teamId)

        Mockito.verify(view).showHomeTeamLogo(teams)
    }
}