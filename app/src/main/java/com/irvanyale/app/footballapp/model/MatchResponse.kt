package com.irvanyale.app.footballapp.model

import com.google.gson.annotations.SerializedName

class MatchResponse(@SerializedName("events") val matches: List<Match>?)