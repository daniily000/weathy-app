package com.daniily.weathy

import com.daniily.weathy.data.WeatherApiService
import com.daniily.weathy.data.dayWeatherListFrom
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun weatherQueryTest() {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://icomms.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherApiService = retrofit.create(WeatherApiService::class.java)
        val call = weatherApiService.getWeatherData(4)
        val execution = call.execute()

        check(execution.isSuccessful)
        val weatherObjectList = execution.body()

        if (execution.isSuccessful && weatherObjectList  != null) {
            println(dayWeatherListFrom(weatherObjectList).toString())
        }


    }
}
