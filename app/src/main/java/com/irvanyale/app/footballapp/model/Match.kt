package com.irvanyale.app.project4.model

import com.google.gson.annotations.SerializedName

data class Match (
    @SerializedName("idEvent")
    var matchId: String? = null,

    @SerializedName("dateEvent")
    var date: String? = null,

    @SerializedName("strHomeTeam")
    var homeTeam: String? = null,

    @SerializedName("strAwayTeam")
    var awayTeam: String? = null,

    @SerializedName("intHomeScore")
    var homeScore: String? = null,

    @SerializedName("intAwayScore")
    var awayScore: String? = null,

    @SerializedName("strEvent")
    var match: String? = null,

    @SerializedName("strTime")
    var time: String? = null,

    @SerializedName("strHomeGoalDetails")
    var homeGoalDetails: String? = null,

    @SerializedName("strAwayGoalDetails")
    var awayGoalDetails: String? = null,

    @SerializedName("intHomeShots")
    var homeShots: String? = null,

    @SerializedName("intAwayShots")
    var awayShots: String? = null,

    @SerializedName("strHomeLineupGoalkeeper")
    var homeLineupGoalkeeper: String? = null,

    @SerializedName("strHomeLineupDefense")
    var homeLineupDefense: String? = null,

    @SerializedName("strHomeLineupMidfield")
    var homeLineupMidfield: String? = null,

    @SerializedName("strHomeLineupForward")
    var homeLineupForward: String? = null,

    @SerializedName("strHomeLineupSubstitutes")
    var homeLineupSubstitutes: String? = null,

    @SerializedName("strAwayLineupGoalkeeper")
    var awayLineupGoalkeeper: String? = null,

    @SerializedName("strAwayLineupDefense")
    var awayLineupDefense: String? = null,

    @SerializedName("strAwayLineupMidfield")
    var awayLineupMidfield: String? = null,

    @SerializedName("strAwayLineupForward")
    var awayLineupForward: String? = null,

    @SerializedName("strAwayLineupSubstitutes")
    var awayLineupSubstitutes: String? = null,

    @SerializedName("idHomeTeam")
    var idHomeTeam: String? = null,

    @SerializedName("idAwayTeam")
    var idAwayTeam: String? = null
)

