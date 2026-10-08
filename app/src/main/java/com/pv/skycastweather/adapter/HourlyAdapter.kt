package com.pv.skycastweather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pv.skycastweather.data.model.Hour
import com.pv.skycastweather.databinding.ItemHourBinding
import java.text.SimpleDateFormat
import java.util.Locale

class HourlyAdapter(
    private val hourList: List<Hour>
) : RecyclerView.Adapter<HourlyAdapter.HourViewHolder>() {

    inner class HourViewHolder(
        val binding: ItemHourBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HourViewHolder {

        val binding = ItemHourBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return HourViewHolder(binding)
    }

    override fun getItemCount(): Int = hourList.size

    override fun onBindViewHolder(
        holder: HourViewHolder,
        position: Int
    ) {

        val hour = hourList[position]

        holder.binding.tvHour.text = formatHour(hour.time)

        holder.binding.tvHourTemp.text =
            "${hour.temp_c.toInt()}°"

        Glide.with(holder.itemView)
            .load("https:${hour.condition.icon}")
            .into(holder.binding.imgHourWeather)
    }

    private fun formatHour(time: String): String {

        val input =
            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        val output =
            SimpleDateFormat("h a", Locale.getDefault())

        return try {

            output.format(input.parse(time)!!)

        } catch (e: Exception) {

            time

        }

    }
}