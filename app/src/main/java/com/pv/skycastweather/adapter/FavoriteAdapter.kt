package com.pv.skycastweather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pv.skycastweather.data.model.FavoriteWeather
import com.pv.skycastweather.databinding.ItemFavoriteWeatherBinding

class FavoriteAdapter(
    private val favorites: MutableList<FavoriteWeather>,
    private val onCityClick: (FavoriteWeather) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(
        val binding: ItemFavoriteWeatherBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteViewHolder {

        val binding = ItemFavoriteWeatherBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FavoriteViewHolder,
        position: Int
    ) {

        val item = favorites[position]

        holder.binding.tvCity.text = item.cityName

        holder.binding.tvTemperature.text = item.temperature

        holder.binding.tvCondition.text = item.condition

        Glide.with(holder.itemView)
            .load(item.iconUrl)
            .into(holder.binding.imgCondition)

        holder.binding.root.setOnClickListener {

            onCityClick(item)

        }

    }

    override fun getItemCount() = favorites.size

    fun updateList(newList: List<FavoriteWeather>) {

        favorites.clear()

        favorites.addAll(newList)

        notifyDataSetChanged()

    }

}