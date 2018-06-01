package com.irvanyale.app.footballapp.db

data class MatchFavorite(
        val id: Long?,
        val matchId: String?,
        val date: String?,
        val homeTeam: String?,
        val awayTeam: String?,
        val homeScore: String?,
        val awayScore: String?) {

    companion object {
        const val TABLE_MATCH_FAVORITE: String = "TABLE_MATCH_FAVORITE"
        const val ID: String = "ID_"
        const val MATCH_ID: String = "MATCH_ID"
        const val MATCH_DATE: String = "MATCH_DATE"
        const val HOME_TEAM: String = "HOME_TEAM"
        const val AWAY_TEAM: String = "AWAY_TEAM"
        const val HOME_SCORE: String = "HOME_SCORE"
        const val AWAY_SCORE: String = "AWAY_SCORE"
    }
}