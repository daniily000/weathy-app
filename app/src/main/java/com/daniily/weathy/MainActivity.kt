package com.daniily.weathy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.daniily.weathy.data.WeatherApiService
import com.daniily.weathy.data.WeatherObject
import com.daniily.weathy.data.dayWeatherListFrom
import com.daniily.weathy.fragment.DaysListFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {


    private val retrofit = Retrofit.Builder()
        .baseUrl("http://icomms.ru/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val weatherApiService = retrofit.create(WeatherApiService::class.java)
    private val call = weatherApiService.getWeatherData(24)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val daysListFragment = DaysListFragment()
//        daysListFragment.apply {
//            onDayClickListener = { Log.e("    MainActivity", it.toString()) }
//            onRefreshListener = { fetchData(defaultCallback) }
//        }

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.days_list_fragment_container, daysListFragment)
        fragmentTransaction.commit()

        fetchData(object : Callback<List<WeatherObject>> {

            override fun onFailure(call: Call<List<WeatherObject>>, t: Throwable) =
                defaultFailure.invoke(call, t)
            override fun onResponse(call: Call<List<WeatherObject>>, response: Response<List<WeatherObject>>) {
                val weatherObjectList = response.body()
                if (weatherObjectList != null)
                    daysListFragment.updateWeather(dayWeatherListFrom(weatherObjectList))
            }
        })

    }

    private fun fetchData(callback: Callback<List<WeatherObject>>) {
        call.enqueue(callback)
    }















//
//    private val updateAndMakeColoredWeatherResponse: (Call<List<WeatherObject>>, Response<List<WeatherObject>>) -> Unit = { _, response ->
//        val weatherObjectList = response.body()
//        if (weatherObjectList != null)
//            daysListFragment.updateWeather(dayWeatherListFrom(weatherObjectList).apply {
//                    this[0].tempLevel = INSANE_COLD
//                    this[1].tempLevel = VERY_COLD
//                    this[2].tempLevel = COLD
//                    this[3].tempLevel = FINE
//                    this[4].tempLevel = WARM
//                    this[5].tempLevel = HOT
//                    this[6].tempLevel = INSANE_HOT
//                    // TODO: delete this example
//                }
//            )
//    }
//
    private val defaultFailure: (call: Call<List<WeatherObject>>, t: Throwable) -> Unit = { _, t ->
        t.printStackTrace()
    }
//
//    private val defaultCallback: Callback<List<WeatherObject>> = object : Callback<List<WeatherObject>> {
//
//        override fun onFailure(call: Call<List<WeatherObject>>, t: Throwable) {
//            days_list_swipe.isRefreshing = false
//        }
//
//        override fun onResponse(call: Call<List<WeatherObject>>, response: Response<List<WeatherObject>>) {
//            val weatherObjectList = response.body()
//
//            if (weatherObjectList != null)
//                daysListFragment.updateWeather(dayWeatherListFrom(weatherObjectList))
//
//            days_list_swipe.isRefreshing = false
//        }
//    }
}
