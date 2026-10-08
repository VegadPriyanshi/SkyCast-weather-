package com.pv.skycastweather.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.pv.skycastweather.databinding.FragmentHomeBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pv.skycastweather.adapter.HourlyAdapter
import com.pv.skycastweather.ui.home.WeatherViewModel
import com.pv.skycastweather.utils.WeatherBackgroundManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.pv.skycastweather.MainActivity
import com.pv.skycastweather.ui.home.SharedCityViewModel
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.pv.skycastweather.ui.favorites.FavoriteViewModel
import kotlinx.coroutines.launch
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.pv.skycastweather.data.mapper.WeatherMapper
import com.pv.skycastweather.utils.LocationHelper
import java.io.File

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding
        get() = _binding!!


    private lateinit var viewModel: WeatherViewModel
    private lateinit var sharedCityViewModel: SharedCityViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )



        viewModel =
            ViewModelProvider(this)[WeatherViewModel::class.java]
        sharedCityViewModel =
            ViewModelProvider(requireActivity())[SharedCityViewModel::class.java]


        favoriteViewModel =
            ViewModelProvider(this)[FavoriteViewModel::class.java]

        observeCachedWeather()

        observeCachedHourlyWeather()

        observeWeather()

        observeSelectedCity()

        viewModel.loadCachedWeather()

        viewModel.loadCachedHourlyWeather()

        binding.forecastNavigation.cardForecastNavigation.setOnClickListener {
            (activity as MainActivity).openForecastFragment()
        }


        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        if (LocationHelper.hasLocationPermission(requireActivity())) {

            LocationHelper.getCurrentLocation(
                requireActivity(),
                onLocationReceived = { latitude, longitude ->

                    sharedCityViewModel.selectCity("$latitude,$longitude")

                },
                onLocationFailed = {

                    Toast.makeText(
                        requireContext(),
                        "Location not found",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            )

        } else {

            locationPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )

        }
    }

    private fun observeCachedWeather() {

        viewModel.cachedWeather.observe(viewLifecycleOwner) { weather ->

            weather?.let {

                updateWeatherUI(it)

            }

        }

    }

    private fun observeCachedHourlyWeather() {

        viewModel.cachedHourlyWeather.observe(viewLifecycleOwner) { hourlyList ->

            if (hourlyList.isNotEmpty()) {

                binding.hourlyLayout.rvHourly.layoutManager =
                    LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )

                binding.hourlyLayout.rvHourly.adapter =
                    HourlyAdapter(hourlyList)

            }

        }

    }

    private fun updateWeatherUI(weather: com.pv.skycastweather.data.model.WeatherUiModel) {
        Log.d("WeatherIcon", "Icon URL = ${weather.iconUrl}")
        Log.d("WeatherIcon", "Local Path = ${weather.iconLocalPath}")

        binding.headerLayout.tvCity.text = weather.city

        binding.headerLayout.tvTemperature.text =
            "${weather.temperature.toInt()}°"

        binding.headerLayout.tvCondition.text =
            weather.condition

        binding.weatherCardsLayout.tvHumidityValue.text =
            "${weather.humidity}%"

        binding.weatherCardsLayout.tvWindValue.text =
            "${weather.windSpeed} km/h"

        binding.weatherCardsLayout.tvPressureValue.text =
            "${weather.pressure.toInt()} mb"

        binding.weatherCardsLayout.tvUvValue.text =
            weather.uvIndex.toString()

        binding.headerLayout.tvFeelsLike.text =
            "Feels Like ${weather.feelsLike.toInt()}°"

        // Use local path if available, fallback to URL
        val imageSource: Any = if (!weather.iconLocalPath.isNullOrEmpty()) {
            val file = File(weather.iconLocalPath)
            if (file.exists()) file else weather.iconUrl
        } else {
            weather.iconUrl
        }

        Glide.with(requireContext())
            .load(imageSource)
            .apply(
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(binding.headerLayout.imgWeather)

    }
    private fun observeWeather() {

        viewModel.weather.observe(viewLifecycleOwner) { response ->

            if (response.isSuccessful && response.body() != null) {

                val weather = response.body()!!

                // Convert API model to UI model
                val uiWeather = WeatherMapper.toUiModel(weather)

                // Update all common UI
                updateWeatherUI(uiWeather)

                // Background
                val background = WeatherBackgroundManager.getBackground(
                    weather.current.condition.code,
                    weather.current.is_day
                )
                binding.headerLayout.root.setBackgroundResource(background)

                // Favorite button
                setupFavoriteButton(weather.location.name)

                // Date
                val today = SimpleDateFormat(
                    "EEEE, dd MMMM",
                    Locale.getDefault()
                ).format(Date())

                binding.headerLayout.tvDate.text = today

                // Hourly Forecast
                val hourlyList = weather.forecast.forecastday[0].hour

                binding.hourlyLayout.rvHourly.layoutManager =
                    LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )

                binding.hourlyLayout.rvHourly.adapter = HourlyAdapter(hourlyList)

            }

        }

    }
    private fun observeSelectedCity() {

        sharedCityViewModel.selectedCity.observe(viewLifecycleOwner) { city ->

            viewModel.getCurrentWeather(city)

        }

    }

    private fun setupFavoriteButton(cityName: String) {

        viewLifecycleOwner.lifecycleScope.launch {

            var isFavorite = favoriteViewModel.isFavorite(cityName)

            binding.headerLayout.imgFavorite.setImageResource(
                if (isFavorite)
                    com.pv.skycastweather.R.drawable.ic_favorite
                else
                    com.pv.skycastweather.R.drawable.ic_favorite_border
            )

            binding.headerLayout.imgFavorite.setOnClickListener {

                if (isFavorite) {

                    favoriteViewModel.delete(cityName)

                } else {

                    favoriteViewModel.insert(cityName)

                }

                isFavorite = !isFavorite

                binding.headerLayout.imgFavorite.setImageResource(
                    if (isFavorite)
                        com.pv.skycastweather.R.drawable.ic_favorite
                    else
                        com.pv.skycastweather.R.drawable.ic_favorite_border
                )
            }
        }
    }

    private val locationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->

            if (isGranted) {

                LocationHelper.getCurrentLocation(
                    requireActivity(),
                    onLocationReceived = { latitude, longitude ->

                        sharedCityViewModel.selectCity("$latitude,$longitude")

                    },
                    onLocationFailed = {

                        Toast.makeText(
                            requireContext(),
                            "Location not found",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                )

            } else {

                Toast.makeText(
                    requireContext(),
                    "Location permission denied",
                    Toast.LENGTH_SHORT
                ).show()

            }

        }

    private fun isInternetAvailable(): Boolean {

        val connectivityManager =
            requireContext().getSystemService(
                ConnectivityManager::class.java
            )

        val network =
            connectivityManager.activeNetwork ?: return false

        val capabilities =
            connectivityManager.getNetworkCapabilities(network)
                ?: return false

        return capabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}