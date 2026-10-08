package com.pv.skycastweather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pv.skycastweather.data.model.PopularCity
import com.pv.skycastweather.databinding.ItemPopularCityBinding

class PopularCityAdapter(

    private val cityList: List<PopularCity>,
    private val onCityClick: (PopularCity) -> Unit

) : RecyclerView.Adapter<PopularCityAdapter.PopularCityViewHolder>() {

    inner class PopularCityViewHolder(
        private val binding: ItemPopularCityBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(city: PopularCity) {

            binding.tvCityName.text = city.cityName

            binding.root.setOnClickListener {

                onCityClick(city)

            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PopularCityViewHolder {

        val binding = ItemPopularCityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PopularCityViewHolder(binding)

    }

    override fun onBindViewHolder(
        holder: PopularCityViewHolder,
        position: Int
    ) {

        holder.bind(cityList[position])

    }

    override fun getItemCount(): Int = cityList.size
}