package com.example.openweatherapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.openweatherapp.data.local.WeatherDatabase
import com.example.openweatherapp.data.repositories.WeatherRepository
import com.example.openweatherapp.data.service.WeatherInterface
import com.example.openweatherapp.utils.ResponseState
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.spy
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WeatherViewModelTest {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var repository: WeatherRepository

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var service: WeatherInterface

    @Mock
    private lateinit var database: WeatherDatabase


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = spy(WeatherRepository(service, database))
        viewModel = WeatherViewModel(repository)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_getCityWeather() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)
        try {
            viewModel.getCityWeather("amsterdam")
            assertNotNull(viewModel.cityWeatherResult.value?.getContent())
            assertEquals(ResponseState.SUCCESS, viewModel.cityWeatherResult.value?.getContent())
        } finally {
            Dispatchers.resetMain()
        }
    }
}