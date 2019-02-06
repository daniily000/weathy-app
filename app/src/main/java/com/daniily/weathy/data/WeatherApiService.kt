package com.daniily.weathy.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("inf/meteo.php")
    fun getWeatherData(@Query("tid") townId: Int): Call<List<WeatherObject>>

}