package dev.wilsonjr.newsapp

import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import dev.wilsonjr.newsapp.base.BaseInstrumentedTest
import dev.wilsonjr.newsapp.feature.news.NewsActivity
import dev.wilsonjr.newsapp.feature.sources.SourcesActivity
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView
import android.app.Instrumentation.ActivityResult
import androidx.test.espresso.intent.Intents.intending
import android.content.Intent




@RunWith(AndroidJUnit4::class)
class NewsFluxTest : BaseInstrumentedTest() {

    @get:Rule
    val activityRule = ActivityTestRule(SourcesActivity::class.java)

    @Test
    fun testFlux() {
        Intents.init()

        waitLoading()

        //click on first item
        onView(withChild(withText("ABC News"))).perform(ViewActions.click())

        //check News activity is open
        intended(hasComponent(NewsActivity::class.java.getName()))

        //check title
        onView(allOf(instanceOf(TextView::class.java), withParent(withResourceName("action_bar")))).check(
            matches(
                withText("ABC News")
            )
        )

    }
}