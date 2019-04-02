package dev.wilsonjr.newsapp.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.After
import org.junit.Before
import org.junit.Rule

open class BaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        TestSuite.init()
    }

    @After
    fun clear(){
        TestSuite.clear()
    }

}