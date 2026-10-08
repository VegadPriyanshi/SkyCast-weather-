package com.pv.skycastweather.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pv.skycastweather.MainActivity
import com.pv.skycastweather.adapter.FavoriteAdapter
import com.pv.skycastweather.data.model.FavoriteWeather
import com.pv.skycastweather.databinding.FragmentFavoritesBinding
import com.pv.skycastweather.ui.favorites.FavoriteViewModel
import com.pv.skycastweather.ui.home.SharedCityViewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var sharedCityViewModel: SharedCityViewModel

    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoritesBinding.inflate(
            inflater,
            container,
            false
        )

        favoriteViewModel =
            ViewModelProvider(this)[FavoriteViewModel::class.java]

        sharedCityViewModel =
            ViewModelProvider(requireActivity())[SharedCityViewModel::class.java]

        favoriteAdapter = FavoriteAdapter(mutableListOf()) { city ->

            sharedCityViewModel.selectCity(city.cityName)

            (activity as MainActivity).openHomeFragment()

        }

        binding.rvFavorites.layoutManager =
            LinearLayoutManager(requireContext())

        binding.rvFavorites.adapter = favoriteAdapter

        observeFavorites()

        return binding.root

    }

    private fun observeFavorites() {

        favoriteViewModel.favoriteWeatherList.observe(viewLifecycleOwner) { weatherList ->

            favoriteAdapter.updateList(weatherList)

            if (weatherList.isEmpty()) {

                binding.rvFavorites.visibility = View.GONE
                binding.tvEmpty.visibility = View.VISIBLE

            } else {

                binding.rvFavorites.visibility = View.VISIBLE
                binding.tvEmpty.visibility = View.GONE

            }

        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}