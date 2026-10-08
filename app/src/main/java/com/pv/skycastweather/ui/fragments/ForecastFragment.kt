package com.pv.skycastweather.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pv.skycastweather.adapter.ForecastAdapter
import com.pv.skycastweather.databinding.FragmentForecastBinding
import com.pv.skycastweather.ui.home.WeatherViewModel
import com.pv.skycastweather.utils.WeatherBackgroundManager
import java.text.SimpleDateFormat
import java.util.Locale
import com.pv.skycastweather.ui.home.SharedCityViewModel

class ForecastFragment : Fragment() {

    private var _binding: FragmentForecastBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: WeatherViewModel
    private lateinit var sharedCityViewModel: SharedCityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentForecastBinding.inflate(
            inflater,
            container,
            false
        )

        viewModel =
            ViewModelProvider(this)[WeatherViewModel::class.java]
        sharedCityViewModel =
            ViewModelProvider(requireActivity())[SharedCityViewModel::class.java]

        observeCachedForecast()

        observeCachedWeather()

        observeWeather()

        observeSelectedCity()

        viewModel.loadCachedWeather()

        viewModel.loadCachedForecastDays()

        return binding.root
    }

    private fun observeWeather() {

        viewModel.weather.observe(viewLifecycleOwner) { response ->

            if (response.isSuccessful && response.body() != null) {

                val weather = response.body()!!

                val background =
                    WeatherBackgroundManager.getBackground(
                        weather.current.condition.code,
                        weather.current.is_day
                    )

                binding.headerLayout.headerRoot
                    .setBackgroundResource(background)

                binding.headerLayout.tvForecastCity.text =
                    weather.location.name

                binding.headerLayout.tvForecastDate.text =
                    "Updated at ${formatLastUpdated(weather.current.last_updated)}"

                binding.headerLayout.tvForecastTemp.text =
                    "${weather.current.temp_c.toInt()}°"

                binding.headerLayout.tvForecastCondition.text =
                    weather.current.condition.text

                binding.headerLayout.tvForecastFeels.text =
                    "Feels Like ${weather.current.feelslike_c.toInt()}°"

                Glide.with(requireContext())
                    .load("https:${weather.current.condition.icon}")
                    .into(binding.headerLayout.imgForecastWeather)

                binding.forecastLayout.rvForecast.layoutManager =
                    LinearLayoutManager(requireContext())

                binding.forecastLayout.rvForecast.adapter =
                    ForecastAdapter(weather.forecast.forecastday)

            }

        }

    }

    private fun observeCachedForecast() {

        viewModel.cachedForecastDays.observe(viewLifecycleOwner) { forecastDays ->

            Log.d(
                "ForecastUI",
                "Forecast received in Fragment = ${forecastDays.size}"
            )

            if (forecastDays.isNotEmpty()) {

                binding.forecastLayout.rvForecast.layoutManager =
                    LinearLayoutManager(requireContext())

                binding.forecastLayout.rvForecast.adapter =
                    ForecastAdapter(forecastDays)

            }

        }

    }

    private fun observeCachedWeather() {

        viewModel.cachedWeather.observe(viewLifecycleOwner) { weather ->

            weather?.let {

                binding.headerLayout.tvForecastCity.text = it.city

                binding.headerLayout.tvForecastTemp.text =
                    "${it.temperature.toInt()}°"

                binding.headerLayout.tvForecastCondition.text =
                    it.condition

                binding.headerLayout.tvForecastFeels.text =
                    "Feels Like ${it.feelsLike.toInt()}°"

                Glide.with(requireContext())
                    .load(it.iconUrl)
                    .into(binding.headerLayout.imgForecastWeather)

            }

        }

    }

    private fun observeSelectedCity() {

        sharedCityViewModel.selectedCity.observe(viewLifecycleOwner) { city ->

            viewModel.getCurrentWeather(city)

        }

    }

    private fun formatLastUpdated(lastUpdated: String): String {

        return try {

            val input = SimpleDateFormat(
                "yyyy-MM-dd HH:mm",
                Locale.getDefault()
            )

            val output = SimpleDateFormat(
                "hh:mm a",
                Locale.getDefault()
            )

            output.format(input.parse(lastUpdated)!!)

        } catch (e: Exception) {

            lastUpdated

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}