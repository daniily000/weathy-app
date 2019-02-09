package com.daniily.weathy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.daniily.weathy.R
import com.daniily.weathy.data.*
import kotlinx.android.synthetic.main.fragment_day_times_list.*
import kotlinx.android.synthetic.main.item_day_time.view.*

class DayTimesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_day_times_list, container, false)
    }

    fun setDayWeatherData(data: DayWeather) {
        day_times_list.adapter = DayWeatherAdapter(data)
    }


    class DayWeatherAdapter(private val dayWeather: DayWeather) :
        RecyclerView.Adapter<DayWeatherAdapter.DayWeatherViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayWeatherViewHolder {
            return DayWeatherViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_day_time, parent, false)
            )
        }

        override fun getItemCount() = 4

        override fun onBindViewHolder(holder: DayWeatherViewHolder, position: Int) {

            holder.itemView.apply {

                when (position) {

                    MORNING -> {
                        item_day_time_container.setBackgroundResource(R.color.colorMorningBackground)
                        central_divider.setBackgroundResource(R.color.colorMorningAccent)
                        time_of_the_day.setText(R.string.morning)
                        temperature.text = dayWeather.morning.temperature.replace("&minus;", "-")
                        weather_description.text = dayWeather.morning.cloud
                        feels_like.text = dayWeather.morning.temperatureFeelsLike.replace("&minus;", "-")
                        pressure.text = String.format(
                            context.getString(R.string.pressure_format),
                            dayWeather.morning.pressure)
                        humidity.text = String.format(
                            context.getString(R.string.humidity_format),
                            dayWeather.morning.humidity)
                        wind.text = dayWeather.morning.wind
                    }

                    DAY -> {
                        item_day_time_container.setBackgroundResource(R.color.colorDayBackground)
                        central_divider.setBackgroundResource(R.color.colorDayAccent)
                        time_of_the_day.setText(R.string.day)
                        temperature.text = dayWeather.day.temperature.replace("&minus;", "-")
                        weather_description.text = dayWeather.day.cloud
                        feels_like.text = dayWeather.day.temperatureFeelsLike.replace("&minus;", "-")
                        pressure.text = String.format(
                            context.getString(R.string.pressure_format),
                            dayWeather.day.pressure)
                        humidity.text = String.format(
                            context.getString(R.string.humidity_format),
                            dayWeather.day.humidity)
                        wind.text = dayWeather.day.wind
                    }

                    EVENING -> {
                        item_day_time_container.setBackgroundResource(R.color.colorEveningBackground)
                        central_divider.setBackgroundResource(R.color.colorEveningAccent)
                        time_of_the_day.setText(R.string.evening)
                        temperature.text = dayWeather.evening.temperature.replace("&minus;", "-")
                        weather_description.text = dayWeather.evening.cloud
                        feels_like.text = dayWeather.evening.temperatureFeelsLike.replace("&minus;", "-")
                        pressure.text = String.format(
                            context.getString(R.string.pressure_format),
                            dayWeather.evening.pressure)
                        humidity.text = String.format(
                            context.getString(R.string.humidity_format),
                            dayWeather.evening.humidity)
                        wind.text = dayWeather.evening.wind
                    }

                    NIGHT -> {
                        item_day_time_container.setBackgroundResource(R.color.colorNightBackground)
                        central_divider.setBackgroundResource(R.color.colorNightAccent)
                        time_of_the_day.setText(R.string.night)
                        temperature.text = dayWeather.night.temperature.replace("&minus;", "-")
                        weather_description.text = dayWeather.night.cloud
                        feels_like.text = dayWeather.night.temperatureFeelsLike.replace("&minus;", "-")
                        pressure.text = String.format(
                            context.getString(R.string.pressure_format),
                            dayWeather.night.pressure)
                        humidity.text = String.format(
                            context.getString(R.string.humidity_format),
                            dayWeather.night.humidity)
                        wind.text = dayWeather.night.wind
                    }
                }
            }


        }

        override fun getItemViewType(position: Int) = position

        class DayWeatherViewHolder(v: View) : RecyclerView.ViewHolder(v)
    }

}