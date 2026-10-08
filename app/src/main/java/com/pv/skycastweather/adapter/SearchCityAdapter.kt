package com.pv.skycastweather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pv.skycastweather.data.model.SearchCity
import com.pv.skycastweather.databinding.ItemSearchCityBinding

class SearchCityAdapter(
    private val cityList: List<SearchCity>,
    private val onCityClick: (SearchCity) -> Unit
) : RecyclerView.Adapter<SearchCityAdapter.CityViewHolder>() {

    inner class CityViewHolder(
        val binding: ItemSearchCityBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CityViewHolder {

        val binding = ItemSearchCityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CityViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CityViewHolder,
        position: Int
    ) {

        val city = cityList[position]

        holder.binding.tvCityName.text = city.name

        holder.binding.tvRegionCountry.text =
            "${city.region}, ${city.country}"

        holder.binding.root.setOnClickListener {

            onCityClick(city)

        }

    }

    override fun getItemCount(): Int {

        return cityList.size

    }

}