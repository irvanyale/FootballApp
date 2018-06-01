package com.irvanyale.app.footballapp.main

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.irvanyale.app.footballapp.R.id.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    @Rule
    @JvmField var activityRule = ActivityTestRule(HomeActivity::class.java)

    @Test
    fun testAppBehaviour() {
        Espresso.onView(ViewMatchers.withId(bottom_nav_view))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(action_matches)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(action_teams)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(action_favorites)).perform(ViewActions.click())
    }
}