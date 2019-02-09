package com.daniily.weathy.data

import android.os.Parcel
import android.os.Parcelable
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

val INSANE_COLD_RANGE = Int.MIN_VALUE..-2
val VERY_COLD_RANGE = -1..1
val COLD_RANGE = 2..3
val FINE_RANGE = 4..5
val WARM_RANGE = 6..6
val HOT_RANGE = 7..8
val INSANE_HOT_RANGE = 9..Int.MAX_VALUE

class DayWeather(
    val morning: WeatherObject,
    val day: WeatherObject,
    val evening: WeatherObject,
    val night: WeatherObject
) : Parcelable {

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

        in INSANE_COLD_RANGE -> INSANE_COLD
        in VERY_COLD_RANGE -> VERY_COLD
        in COLD_RANGE -> COLD
        in FINE_RANGE -> FINE
        in WARM_RANGE -> WARM
        in HOT_RANGE -> HOT
        in INSANE_HOT_RANGE -> INSANE_HOT
        else -> FINE
    }

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(WeatherObject::class.java.classLoader),
        parcel.readParcelable(WeatherObject::class.java.classLoader),
        parcel.readParcelable(WeatherObject::class.java.classLoader),
        parcel.readParcelable(WeatherObject::class.java.classLoader)
    ) {
        tempLevel = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(morning, flags)
        parcel.writeParcelable(day, flags)
        parcel.writeParcelable(evening, flags)
        parcel.writeParcelable(night, flags)
        parcel.writeInt(tempLevel)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DayWeather> {
        override fun createFromParcel(parcel: Parcel): DayWeather {
            return DayWeather(parcel)
        }

        override fun newArray(size: Int): Array<DayWeather?> {
            return arrayOfNulls(size)
        }
    }

}

class WeatherObject() : Parcelable {

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

    constructor(parcel: Parcel) : this() {
        timeOfDay = parcel.readInt()
        pressure = parcel.readInt()
        temperature = parcel.readString() ?: ""
        temperatureFeelsLike = parcel.readString() ?: ""
        humidity = parcel.readInt()
        wind = parcel.readString() ?: ""
        cloud = parcel.readString() ?: ""
        townId = parcel.readInt()
    }

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(timeOfDay)
        parcel.writeInt(pressure)
        parcel.writeString(temperature)
        parcel.writeString(temperatureFeelsLike)
        parcel.writeInt(humidity)
        parcel.writeString(wind)
        parcel.writeString(cloud)
        parcel.writeInt(townId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WeatherObject> {
        override fun createFromParcel(parcel: Parcel): WeatherObject {
            return WeatherObject(parcel)
        }

        override fun newArray(size: Int): Array<WeatherObject?> {
            return arrayOfNulls(size)
        }
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
