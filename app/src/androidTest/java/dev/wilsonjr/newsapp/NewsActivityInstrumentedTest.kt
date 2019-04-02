package dev.wilsonjr.newsapp

import android.content.Intent
import android.widget.TextView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import dev.wilsonjr.newsapp.api.model.ArticlesResponse
import dev.wilsonjr.newsapp.api.model.Source
import dev.wilsonjr.newsapp.api.model.SourceResponse
import dev.wilsonjr.newsapp.api.model.enums.Category
import dev.wilsonjr.newsapp.api.model.enums.Country
import dev.wilsonjr.newsapp.base.BaseInstrumentedTest
import dev.wilsonjr.newsapp.base.FileUtils
import dev.wilsonjr.newsapp.base.TestSuite
import dev.wilsonjr.newsapp.base.mock.endpoint.ResponseHandler
import dev.wilsonjr.newsapp.feature.news.NEWS_ACTIVITY_SOURCE
import dev.wilsonjr.newsapp.feature.news.NewsActivity
import dev.wilsonjr.newsapp.feature.sources.SourcesActivity
import dev.wilsonjr.newsapp.utils.JsonUtils
import okhttp3.Request
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.RuntimeException

@RunWith(AndroidJUnit4::class)
class NewsActivityInstrumentedTest : BaseInstrumentedTest() {

    val emptyResponse = ArticlesResponse(ArrayList(), "ok", 0)
    val baseSource =
        Source("cat", "BR", "Test Brazil Description", "1234", "PT", "Test Brazil", "http://www.google.com.br")

    @get:Rule
    val activityRule = ActivityTestRule(NewsActivity::class.java, false, false)


    private fun launchActivity(source: Source) {
        val intent = Intent()
        intent.putExtra(NEWS_ACTIVITY_SOURCE, source)
        activityRule.launchActivity(intent)
        Intents.init()
    }

    @Test
    fun testNews() {

        launchActivity(baseSource)

        waitLoading()

        onView(
            CoreMatchers.allOf(
                CoreMatchers.instanceOf(TextView::class.java),
                ViewMatchers.withParent(ViewMatchers.withResourceName("action_bar"))
            )
        ).check(matches(ViewMatchers.withText("Test Brazil")))

        onView(withId(R.id.news_list)).check(matches(isDisplayed()))
        onView(withId(R.id.error_state)).check(matches(not(isDisplayed())))
        onView(withId(R.id.empty_state)).check(matches(not(isDisplayed())))
        onView(ViewMatchers.withChild(ViewMatchers.withText("Texas to soon remove hemp from controlled substance list"))).check(matches(isDisplayed()))

    }

    @Test
    fun testEmptyState() {

        TestSuite.mock(TestConstants.newsURL).body(JsonUtils.toJson(emptyResponse)).apply()

        launchActivity(baseSource)

        waitLoading()

        onView(withId(R.id.empty_state)).check(matches(isDisplayed()))
        onView(withId(R.id.error_state)).check(matches(not(isDisplayed())))
        onView(withId(R.id.news_list)).check(matches(not(isDisplayed())))

    }

    @Test
    fun testErrorStateWithRetry() {

        TestSuite.mock(TestConstants.newsURL).throwConnectionError().apply()

        launchActivity(baseSource)

        waitLoading()

        onView(withId(R.id.error_state)).check(matches(isDisplayed()))
        onView(withId(R.id.empty_state)).check(matches(not(isDisplayed())))
        onView(withId(R.id.news_list)).check(matches(not(isDisplayed())))

        TestSuite.clearEndpointMocks()

        onView(withId(R.id.error_state_retry)).perform(ViewActions.click())

        waitLoading()

        onView(withId(R.id.news_list)).check(matches(isDisplayed()))
        onView(withId(R.id.error_state)).check(matches(not(isDisplayed())))
        onView(withId(R.id.empty_state)).check(matches(not(isDisplayed())))

    }

    @After
    fun clearTest() {
        Intents.release()
    }

}