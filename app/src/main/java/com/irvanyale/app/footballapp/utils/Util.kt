package com.irvanyale.app.footballapp.utils

import android.view.View
import java.text.SimpleDateFormat
import java.util.*

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun toSimpleString(date: Date?): String? = with(date ?: Date()){
    SimpleDateFormat("EEEE, dd MMM yyy", Locale.US).format(this)
}