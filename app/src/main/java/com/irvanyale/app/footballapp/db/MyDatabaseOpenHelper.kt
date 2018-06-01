package com.irvanyale.app.footballapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class MyDatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "Favorite.db", null, 1) {
    companion object {
        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper {
            if (instance == null) {
                instance = MyDatabaseOpenHelper(ctx.applicationContext)
            }
            return instance as MyDatabaseOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable(MatchFavorite.TABLE_MATCH_FAVORITE, true,
                MatchFavorite.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                MatchFavorite.MATCH_ID to TEXT + UNIQUE,
                MatchFavorite.MATCH_DATE to TEXT,
                MatchFavorite.HOME_TEAM to TEXT,
                MatchFavorite.AWAY_TEAM to TEXT,
                MatchFavorite.HOME_SCORE to TEXT,
                MatchFavorite.AWAY_SCORE to TEXT)

        db.createTable(TeamFavorite.TABLE_TEAM_FAVORITE, true,
                TeamFavorite.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                TeamFavorite.TEAM_ID to TEXT + UNIQUE,
                TeamFavorite.TEAM_NAME to TEXT,
                TeamFavorite.TEAM_BADGE to TEXT,
                TeamFavorite.TEAM_FORMED_YEAR to TEXT,
                TeamFavorite.TEAM_STADIUM to TEXT,
                TeamFavorite.TEAM_DESC to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(MatchFavorite.TABLE_MATCH_FAVORITE, true)
        db.dropTable(TeamFavorite.TABLE_TEAM_FAVORITE, true)
    }
}

// Access property for Context
val Context.database: MyDatabaseOpenHelper
    get() = MyDatabaseOpenHelper.getInstance(applicationContext)