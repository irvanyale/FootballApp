package com.irvanyale.app.footballapp.utils

import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat

class UtilKtTest {

    @Test
    fun testToSimpleString() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = dateFormat.parse("2018-05-28")
        assertEquals("Monday, 28 May 2018", toSimpleString(date))
    }
}