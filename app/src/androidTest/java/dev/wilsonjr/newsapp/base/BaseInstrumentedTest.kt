package dev.wilsonjr.newsapp.base

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import dev.wilsonjr.newsapp.R
import junit.framework.TestCase

open class BaseInstrumentedTest : TestCase() {

    internal fun waitLoading() {
        //TODO replace with IdlingResource
        Thread.sleep(100)
        while (isLoadingVisible()) {
            Thread.sleep(200)
        }

    }

    private fun isLoadingVisible(): Boolean {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.custom_loading_imageView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            return true
        } catch (ignored: Throwable) {
        }
        return false
    }

}