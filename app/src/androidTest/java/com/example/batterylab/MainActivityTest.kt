package com.example.batterylab

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun timerFinish_reEnablesDurationAndStartButton() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.etDuration)).perform(replaceText("1"))
        onView(withId(R.id.btnStartTimer)).perform(click())

        Thread.sleep(1500)

        onView(withId(R.id.etDuration)).check(matches(isEnabled()))
        onView(withId(R.id.btnStartTimer)).check(matches(isEnabled()))
    }
}
