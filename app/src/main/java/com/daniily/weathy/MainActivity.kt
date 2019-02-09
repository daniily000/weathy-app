package com.daniily.weathy

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.daniily.weathy.data.DayWeather
import com.daniily.weathy.data.WeatherApiService
import com.daniily.weathy.data.WeatherObject
import com.daniily.weathy.data.dayWeatherListFrom
import com.daniily.weathy.fragment.DayTimesFragment
import com.daniily.weathy.fragment.DaysListFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_days_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://icomms.ru/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val weatherApiService = retrofit.create(WeatherApiService::class.java)

    private val daysListTag = "DaysListFragment"
    private val dayTimesTag = "DayTimesFragment"

    private var wasLaunched = false

    private lateinit var daysListFragment: DaysListFragment
    private lateinit var dayTimesFragment: DayTimesFragment

    private var daysList: List<DayWeather> = Collections.emptyList<DayWeather>()
    private var chosenDayWeather: DayWeather? = null

    private lateinit var fragmentTransaction: FragmentTransaction


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        daysListFragment = DaysListFragment()
        dayTimesFragment = DayTimesFragment()
        fragmentTransaction = supportFragmentManager.beginTransaction()

        daysListFragment.apply {
            onDayClickListener = {
                chosenDayWeather = it
                openDayWeather(it)
            }
            onRefreshListener = {
                Thread {
                    try {
                        val exec = weatherApiService.getWeatherData(24).execute()
                        val body = if (exec.isSuccessful) exec.body() ?: emptyList() else emptyList()
                        if (exec.isSuccessful) {
                            daysList = dayWeatherListFrom(body)
                            runOnUiThread {
                                updateWeather(daysList)
                                this@MainActivity.splash.text = ""
                            }
                        }
                    } catch (e: UnknownHostException) {
                        e.printStackTrace()
                        Snackbar.make(
                            days_list,
                            getString(R.string.server_unreachable),
                            Snackbar.LENGTH_LONG).show()
                    }
                    runOnUiThread { days_list_swipe.isRefreshing = false }

                }.start()
            }
        }

        wasLaunched = savedInstanceState?.getBoolean("wasLaunched") ?: false
        if (!wasLaunched) {
            wasLaunched = true
            fetchData(fetchCallback)
        }

        fragmentTransaction.replace(R.id.day_times_fragment_container, dayTimesFragment, dayTimesTag)
        fragmentTransaction.hide(dayTimesFragment)
        fragmentTransaction.replace(R.id.days_list_fragment_container, daysListFragment, daysListTag)
        fragmentTransaction.commit()
    }

    override fun onResume() {
        super.onResume()
        if (daysList.isEmpty()) splash.text = getString(R.string.refresh_tip)
    }

    override fun onBackPressed() {
        if (dayTimesFragment.isVisible) {
            closeDayWeather()
        } else {
            finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArray("daysList", daysList.toTypedArray())
        outState.putParcelable("chosenDayWeather", chosenDayWeather)
        outState.putBoolean("wasLaunched", wasLaunched)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        chosenDayWeather = savedInstanceState?.getParcelable("chosenDayWeather")
        daysList = try {
            val parcelableArray = savedInstanceState?.getParcelableArray("daysList") ?: emptyArray()
            if (!parcelableArray.isEmpty() && parcelableArray[0] is DayWeather) {
                (parcelableArray as Array<DayWeather>).toList()
            } else {
                emptyList()
            }
        } catch (e: ClassCastException) {
            emptyList()
        }

        if (!daysList.isEmpty()) {
            daysListFragment.updateWeather(daysList)
            val weather = chosenDayWeather
            if (weather != null) {
                openDayWeather(weather)
            }
        }
    }

    private fun fetchData(callback: Callback<List<WeatherObject>>) {
        weatherApiService.getWeatherData(24).enqueue(callback)
    }

    private fun openDayWeather(dayWeather: DayWeather) {
        dayTimesFragment.setDayWeatherData(dayWeather)
        supportFragmentManager.beginTransaction().show(dayTimesFragment).commit()
        main_toolbar.title = SimpleDateFormat("d MMMM, EEEE", Locale.getDefault()).format(dayWeather.date)
    }

    private fun closeDayWeather() {
        supportFragmentManager.beginTransaction().hide(dayTimesFragment).commit()
        chosenDayWeather = null
        main_toolbar.setTitle(R.string.app_name)
    }

            private val fetchCallback = object : Callback<List<WeatherObject>> {

        override fun onFailure(call: Call<List<WeatherObject>>, t: Throwable) {
            Log.w("Retrofit", "failure: $t", t)
            Snackbar.make(
                main_container,
                getString(R.string.server_unreachable),
                Snackbar.LENGTH_LONG).show()
        }

        override fun onResponse(call: Call<List<WeatherObject>>, response: Response<List<WeatherObject>>) {
            val weatherObjectList = response.body()
            if (weatherObjectList != null)
                daysList = dayWeatherListFrom(weatherObjectList)
                daysListFragment.updateWeather(daysList)
            splash.text = ""
        }
    }
}
