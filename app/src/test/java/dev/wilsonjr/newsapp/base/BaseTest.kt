package dev.wilsonjr.newsapp.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.wilsonjr.faire.base.appComponent
import dev.wilsonjr.newsapp.base.mock.MockedEndpointService
import dev.wilsonjr.newsapp.base.repository.EndpointService
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.mock.declare

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