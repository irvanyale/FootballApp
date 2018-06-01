package com.irvanyale.app.footballapp.model

import com.google.gson.annotations.SerializedName

data class Player (
        @SerializedName("idTeam")
        var playerId: String? = null,

        @SerializedName("strTeam")
        var playerName: String? = null,

        @SerializedName("strTeamBadge")
        var playerPosition: String? = null,

        @SerializedName("intFormedYear")
        var playerPic: String? = null
        )