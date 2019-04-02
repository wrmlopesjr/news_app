package dev.wilsonjr.newsapp

import android.app.Instrumentation
import android.content.Intent
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import dev.wilsonjr.newsapp.base.BaseInstrumentedTest
import dev.wilsonjr.newsapp.feature.news.NewsActivity
import dev.wilsonjr.newsapp.feature.sources.SourcesActivity
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class NewsFluxTest : BaseInstrumentedTest() {

    @get:Rule
    val activityRule = ActivityTestRule(SourcesActivity::class.java, false, false)

    @Before
    fun setupTest() {
        activityRule.launchActivity(null)
        Intents.init()
    }

    @Test
    fun testFlux() {
        waitLoading()

        //click on first item
        onView(withChild(withText("ABC News"))).perform(ViewActions.click())

        //check News activity is open
        intended(hasComponent(NewsActivity::class.java.name))

        //check title
        onView(allOf(instanceOf(TextView::class.java), withParent(withResourceName("action_bar")))).check(
            matches(
                withText("ABC News")
            )
        )

        //check url redirect
        val expectedIntent = allOf(
            hasAction(Intent.ACTION_VIEW),
            hasData("https://abcnews.go.com/US/wireStory/texas-remove-hemp-controlled-substance-list-62093347")
        )
        intending(expectedIntent).respondWith(Instrumentation.ActivityResult(0, null))

        //click on first item
        onView(withChild(withText("Texas to soon remove hemp from controlled substance list"))).perform(ViewActions.click())

        intended(expectedIntent)

    }

    @After
    fun clearTest() {
        Intents.release()
    }
}