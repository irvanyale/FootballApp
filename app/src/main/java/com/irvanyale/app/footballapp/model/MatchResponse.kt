package com.irvanyale.app.project4.model

import com.google.gson.annotations.SerializedName

class MatchResponse(@SerializedName("events") val matches: List<Match>?)