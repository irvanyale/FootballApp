package com.irvanyale.app.footballapp.model

import com.google.gson.annotations.SerializedName

class MatchSearchResponse(@SerializedName("event") val matches: List<Match>?)