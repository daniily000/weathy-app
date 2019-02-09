package com.daniily.weathy.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daniily.weathy.R
import com.daniily.weathy.data.*
import kotlinx.android.synthetic.main.fragment_days_list.*
import kotlinx.android.synthetic.main.fragment_days_list.view.*
import kotlinx.android.synthetic.main.item_day_weather.view.*
import java.text.DateFormat
import java.util.*


class DaysListFragment : Fragment() {

    var onDayClickListener: ((DayWeather) -> Unit)? = null
    var onRefreshListener: (() -> Unit) = { Unit }

    private val mDayWeatherList = ArrayList<DayWeather>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_days_list, container, false)
        v.days_list.adapter = DaysListAdapter(mDayWeatherList, onDayClickListener)
        v.days_list.layoutManager = LinearLayoutManager(v.context)
        v.days_list_swipe.setOnRefreshListener {
            days_list_swipe.isRefreshing = false
        }
        v.days_list_swipe.setOnRefreshListener(onRefreshListener)
        return v
    }

    fun updateWeather(dayWeatherList: List<DayWeather>) {
        mDayWeatherList.clear()
        mDayWeatherList.addAll(dayWeatherList)
        days_list.adapter?.notifyDataSetChanged()
    }


    class DaysListAdapter(
        private val dayWeatherList: List<DayWeather>,
        private val onClickListener: ((DayWeather) -> Unit)?
    ) : RecyclerView.Adapter<DaysListAdapter.DaysListViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DaysListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_day_weather, parent, false)
        )

        override fun getItemCount(): Int = dayWeatherList.size

        override fun onBindViewHolder(holder: DaysListViewHolder, position: Int) {
            val day = dayWeatherList[position]
            holder.itemView.apply {
                item_day_weather_day_of_the_week.text = context.getString(
                    when (day.dayOfTheWeek) {
                        1 -> R.string.sunday
                        2 -> R.string.monday
                        3 -> R.string.tuesday
                        4 -> R.string.wednesday
                        5 -> R.string.thursday
                        6 -> R.string.friday
                        7 -> R.string.saturday
                        else -> R.string.no_data
                    }
                )
                item_day_weather_date.text = DateFormat.getDateInstance(DateFormat.DEFAULT).format(day.date)
                item_day_weather_temperature.text = day.temperature
                setOnClickListener {
                    onClickListener?.invoke(dayWeatherList[position])
                }

                if (Calendar.getInstance().time == day.date) {
                    Log.i("DaysListFragment", "${Calendar.getInstance().time} and ${day.date}")
                    item_day_weather_date.typeface = Typeface.DEFAULT_BOLD
                    item_day_weather_temperature.typeface = Typeface.DEFAULT_BOLD

                    item_day_weather_date.setTextColor(context.resources.getColor(R.color.colorAccent))
                    item_day_weather_temperature.setTextColor(context.resources.getColor(R.color.colorAccent))
                }

                setBackgroundColor(
                    context.resources.getColor(
                        when (day.tempLevel) {

                            INSANE_COLD -> R.color.colorInsaneCold
                            VERY_COLD -> R.color.colorVeryCold
                            COLD -> R.color.colorCold
                            FINE -> R.color.colorFine
                            WARM -> R.color.colorWarm
                            HOT -> R.color.colorHot
                            INSANE_HOT -> R.color.colorInsaneHot
                            else -> R.color.colorPrimary
                        }
                    )
                )
            }
        }


        class DaysListViewHolder(v: View) : RecyclerView.ViewHolder(v)
    }


}
