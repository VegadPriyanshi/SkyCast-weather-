package com.pv.skycastweather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pv.skycastweather.data.model.ForecastDay
import com.pv.skycastweather.databinding.ItemForecastBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ForecastAdapter(
    private val forecastList: List<ForecastDay>
) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    class ForecastViewHolder(
        val binding: ItemForecastBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ForecastViewHolder {

        val binding = ItemForecastBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ForecastViewHolder(binding)
    }

    override fun getItemCount(): Int = forecastList.size

    override fun onBindViewHolder(
        holder: ForecastViewHolder,
        position: Int
    ) {

        val forecast = forecastList[position]

        holder.binding.tvDay.text = formatDay(forecast.date, position)

        holder.binding.tvCondition.text =
            forecast.day.condition.text

        holder.binding.tvTemperature.text =
            "${forecast.day.maxtemp_c.toInt()}° / ${forecast.day.mintemp_c.toInt()}°"

        holder.binding.tvRainChance.text =
            "Rain ${forecast.day.daily_chance_of_rain}%"

        Glide.with(holder.itemView)
            .load("https:${forecast.day.condition.icon}")
            .into(holder.binding.imgForecastIcon)

    }

    private fun formatDay(
        date: String,
        position: Int
    ): String {

        if (position == 0) {
            return "Today"
        }

        val input =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val output =
            SimpleDateFormat("EEE", Locale.getDefault())

        return try {

            output.format(input.parse(date)!!)

        } catch (e: Exception) {

            date

        }
    }
}