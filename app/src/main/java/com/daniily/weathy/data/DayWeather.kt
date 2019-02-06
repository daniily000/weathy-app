package com.daniily.weathy.data

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

const val MORNING = 0
const val DAY = 1
const val EVENING = 2
const val NIGHT = 3

const val INSANE_COLD = 0
const val VERY_COLD = 1
const val COLD = 2
const val FINE = 3
const val WARM = 4
const val HOT = 5
const val INSANE_HOT = 6


class DayWeather(
    val morning: WeatherObject,
    val day: WeatherObject,
    val evening: WeatherObject,
    val night: WeatherObject
) {

    private val tempMorning = morning.temperature
        .replace("+", "")
        .replace("&minus;", "")
        .toInt()

    private val tempDay = day.temperature
        .replace("+", "")
        .replace("&minus;", "")
        .toInt()

    private val tempEvening = evening.temperature
        .replace("+", "")
        .replace("&minus;", "")
        .toInt()

    private val tempNight = night.temperature
        .replace("+", "")
        .replace("&minus;", "")
        .toInt()

    private val min = minOf(minOf(tempMorning, tempDay, tempEvening), tempNight)
    private val max = maxOf(maxOf(tempMorning, tempDay, tempEvening), tempNight)

    val date = morning.date
    val dayOfTheWeek = Calendar.getInstance().apply { time = date }.get(Calendar.DAY_OF_WEEK)
    val temperature = "${if (min > 0) "+" else ""}$min .. ${if (max > 0) "+" else ""}$max"
    var tempLevel = when ((min + max) / 2) {

        in Int.MIN_VALUE..-15 -> INSANE_COLD
        in -14..-5 -> VERY_COLD
        in -4..10 -> COLD
        in 11..16 -> FINE
        in 17..25 -> WARM
        in 26..35 -> HOT
        in 36..Int.MAX_VALUE -> INSANE_HOT
        else -> FINE
    }

    override fun toString(): String {
        return "DayWeather(morning=$morning, day=$day, evening=$evening, night=$night, date=$date, dayOfTheWeek=$dayOfTheWeek)"
    }
}

class WeatherObject {

    @SerializedName("date")
    lateinit var date: Date

    @SerializedName("tod")
    var timeOfDay: Int = 0

    @SerializedName("pressure")
    var pressure: Int = 0

    @SerializedName("temp")
    lateinit var temperature: String

    @SerializedName("feel")
    lateinit var temperatureFeelsLike: String

    @SerializedName("humidity")
    var humidity: Int = 0

    @SerializedName("wind")
    lateinit var wind: String

    @SerializedName("cloud")
    lateinit var cloud: String

    @SerializedName("tid")
    var townId: Int = 0

    override fun toString(): String {
        return "DayWeather(date=$date, " +
                "timeOfDay='$timeOfDay', " +
                "pressure=$pressure, " +
                "temperature=$temperature, " +
                "temperatureFeelsLike=$temperatureFeelsLike, " +
                "humidity=$humidity, " +
                "wind='$wind', " +
                "cloud='$cloud', " +
                "townId=$townId)"
    }
}

fun dayWeatherListFrom(objects: List<WeatherObject>): List<DayWeather> {

    val list = ArrayList<DayWeather>()

    for (i in objects.indices) {
        if (objects[i].timeOfDay == MORNING) {
            list.add(
                DayWeather(
                    objects[i],
                    objects[i + 1],
                    objects[i + 2],
                    objects[i + 3]
                )
            )
        }
    }
    return list
}
