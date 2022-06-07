package com.example.openweatherapp.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4ClassRunner::class)
class LocationDaoTest {

    private lateinit var locationDao: LocationDao
    private lateinit var db: WeatherDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, WeatherDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        locationDao = db.locationDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


    @Test
    @Throws(Exception::class)
    fun insertAndGetLocation() = runBlocking {
        val location = Location(123,
            "Amsterdam", "asdf")
        locationDao.insertLocation(location)
        val allWords = locationDao.isBookMarkPresent(123)
        assertTrue(allWords)
    }

    @Test
    @Throws(Exception::class)
    fun removeLocation() = runBlocking {
        val location = Location(123,
            "Amsterdam", "asdf")
        locationDao.insertLocation(location)
        var allWords = locationDao.isBookMarkPresent(123)
        assertTrue(allWords)

        locationDao.removeFromBookMark(location)
        allWords = locationDao.isBookMarkPresent(123)
        assertFalse(allWords)
    }

    @Test
    @Throws(Exception::class)
    fun getAllBookmark() = runBlocking {
        val location1 = Location(123,
            "Amsterdam", "asdf")
        val location2 = Location(124,
            "Paris", "asdf")

        locationDao.insertLocation(location1)
        locationDao.insertLocation(location2)

        val allBookmarkLocation = locationDao.getAllBookmarkLocation()
        assertTrue(allBookmarkLocation.isNotEmpty())
        assertEquals("Amsterdam", allBookmarkLocation[0])
        assertEquals("Paris", allBookmarkLocation[1])
    }

    @Test
    @Throws(Exception::class)
    fun getAllBookmark_Empty() = runBlocking {
        val allBookmarkLocation = locationDao.getAllBookmarkLocation()
        assertTrue(allBookmarkLocation.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getCityWeatherFromDB_empty() = runBlocking {
        val location = locationDao.getCityWeatherFromDB("amsterdam")
        assertNull(location)
    }

    @Test
    @Throws(Exception::class)
    fun getCityWeatherFromDB() = runBlocking {
        val location1 = Location(123,
        "Amsterdam", "open weather map")

        locationDao.insertLocation(location1)

        val location = locationDao.getCityWeatherFromDB("amsterdam")
        assertNotNull(location)
        assertEquals(123, location.id)
        assertEquals("open weather map", location.data)
    }

}