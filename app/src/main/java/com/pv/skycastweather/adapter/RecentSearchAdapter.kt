package com.pv.skycastweather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pv.skycastweather.data.local.entity.SearchHistoryEntity
import com.pv.skycastweather.databinding.ItemSearchCityBinding

class RecentSearchAdapter(
    private val onCityClick: (SearchHistoryEntity) -> Unit
) : RecyclerView.Adapter<RecentSearchAdapter.RecentSearchViewHolder>() {

    private val recentSearches = mutableListOf<SearchHistoryEntity>()

    inner class RecentSearchViewHolder(
        val binding: ItemSearchCityBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentSearchViewHolder {

        val binding = ItemSearchCityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return RecentSearchViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecentSearchViewHolder,
        position: Int
    ) {

        val city = recentSearches[position]

        holder.binding.tvCityName.text = city.cityName

        holder.binding.tvRegionCountry.text = "Recent Search"

        holder.binding.root.setOnClickListener {
            onCityClick(city)
        }

    }

    override fun getItemCount(): Int {
        return recentSearches.size
    }

    fun updateList(newList: List<SearchHistoryEntity>) {

        recentSearches.clear()

        recentSearches.addAll(newList)

        notifyDataSetChanged()

    }

}