package com.daniily.weathy.data

import com.google.gson.annotations.SerializedName
import java.util.*

class DayWeather {

    @SerializedName("date")
    lateinit var date: Date

    @SerializedName("tod")
    lateinit var timeOfDay: String

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
