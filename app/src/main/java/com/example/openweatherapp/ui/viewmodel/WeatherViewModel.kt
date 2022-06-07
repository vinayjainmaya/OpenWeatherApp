package com.example.openweatherapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openweatherapp.data.local.Location
import com.example.openweatherapp.data.model.WeatherResponse
import com.example.openweatherapp.data.repositories.WeatherRepository
import com.example.openweatherapp.utils.BookMarkState
import com.example.openweatherapp.utils.Event
import com.example.openweatherapp.utils.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject


@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _cityWeather = MutableLiveData<Event<ResponseState>>()
    val cityWeatherResult: LiveData<Event<ResponseState>>
        get() = _cityWeather

    private val _allBookMark = MutableLiveData<Event<BookMarkState>>()
    val allBookMarkResult: LiveData<Event<BookMarkState>>
        get() = _allBookMark

    private val _addRemoveBookmark = MutableLiveData<Event<BookMarkState>>()
    val addRemoveBookmarkResult: LiveData<Event<BookMarkState>>
        get() = _addRemoveBookmark

    private val _isLocationBookmarked = MutableLiveData<Event<BookMarkState>>()
    val isLocationBookmarked: LiveData<Event<BookMarkState>>
        get() = _isLocationBookmarked

    lateinit var cityWeather: WeatherResponse
        private set

    lateinit var bookMarkedLocations: List<String>
        private set

    /**
     * method to get city weather
     *
     * @param city, name of the city
     */
    fun getCityWeather(city: String) {
        _cityWeather.postValue(Event(ResponseState.LOADING))

        viewModelScope.launch {
            try {
                cityWeather = repository.getCityWeather(city)
                _cityWeather.postValue(Event(ResponseState.SUCCESS))
            } catch (e: Exception) {
                _cityWeather.postValue(
                    Event(
                        handleError(e)
                    )
                )
            }
        }
    }

    fun getAllBookMarkedLocation() {
        _allBookMark.postValue(Event(BookMarkState.LOADING))
        viewModelScope.launch {
            try {
                bookMarkedLocations = repository.getAllBookmarkLocation()
                _allBookMark.postValue(
                    Event(
                        if (bookMarkedLocations.isNotEmpty())
                            BookMarkState.BM_PRESENT
                        else BookMarkState.BM_EMPTY
                    )
                )
            } catch (e: Exception) {
                _allBookMark.postValue(
                    Event(
                        BookMarkState.SQL_ERROR
                    )
                )
            }
        }
    }

    private fun addToBookMark(location: Location) {
        _addRemoveBookmark.postValue(Event(BookMarkState.LOADING))
        viewModelScope.launch {
            try {
                repository.addToBookMark(location).also {
                    _addRemoveBookmark.postValue(
                        Event(
                            if (it >= 0) BookMarkState.BM_ADDED else
                                BookMarkState.SQL_ERROR
                        )
                    )
                }
            } catch (e: Exception) {
                _addRemoveBookmark.postValue(
                    Event(
                        BookMarkState.SQL_ERROR
                    )
                )
            }
        }
    }

    private fun removeFromBookMark(location: Location) {
        viewModelScope.launch {
            try {
                repository.removeFromBookMark(location).also {
                    _addRemoveBookmark.postValue(
                        Event(
                            if (it >= 0) BookMarkState.BM_REMOVED else
                                BookMarkState.SQL_ERROR
                        )
                    )
                }
            } catch (e: Exception) {
                _addRemoveBookmark.postValue(
                    Event(
                        BookMarkState.SQL_ERROR
                    )
                )
            }
        }
    }

    fun isLocationBookmarked(id: Int) {
        _isLocationBookmarked.postValue(Event(BookMarkState.LOADING))
        viewModelScope.launch {
            try {
                repository.isBookMarkPresent(id).also {
                    _isLocationBookmarked.postValue(
                        Event(
                            if (it) BookMarkState.BM_PRESENT
                            else BookMarkState.BM_NOT_PRESENT
                        )
                    )
                }
            } catch (e: Exception) {
                _isLocationBookmarked.postValue(
                    Event(
                        BookMarkState.SQL_ERROR
                    )
                )
            }
        }
    }

    fun handleBookmark(location: Location, isLocationBookmarked: Boolean) {
        _addRemoveBookmark.postValue(Event(BookMarkState.LOADING))
        viewModelScope.launch {
            try {
                if (isLocationBookmarked) removeFromBookMark(location)
                else addToBookMark(location)
            } catch (e: Exception) {
                _addRemoveBookmark.postValue(
                    Event(
                        BookMarkState.SQL_ERROR
                    )
                )
            }
        }
    }


    private fun handleError(e: Exception): ResponseState {
        return if (e is UnknownHostException) {
            ResponseState.NO_INTERNET
        } else {
            val json =
                (e as HttpException).response()?.errorBody()?.charStream()?.readText() ?: "{}"
            val response = JSONObject(json)
            val errorCode = response.getString("cod")
            val errorMessage = response.getString("message")
            if (errorCode.contains("404") && errorMessage.contains("city not found"))
                ResponseState.NOT_FOUND
            else ResponseState.API_ERROR
        }

    }
}