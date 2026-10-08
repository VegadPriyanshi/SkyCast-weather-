package com.pv.skycastweather.ui.forecast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pv.skycastweather.R
import com.pv.skycastweather.databinding.ActivityForecastBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pv.skycastweather.adapter.ForecastAdapter
import com.pv.skycastweather.ui.home.WeatherViewModel
import com.pv.skycastweather.utils.WeatherBackgroundManager
import java.text.SimpleDateFormat
import java.util.Locale

class ForecastActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForecastBinding
    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val city = intent.getStringExtra("city") ?: "Rajkot"

        // Initialize ViewBinding first
        binding = ActivityForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        observeWeather()

        viewModel.getCurrentWeather(city)
        // Back button click
        binding.headerLayout.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun observeWeather() {

        viewModel.weather.observe(this) { response ->

            if (response.isSuccessful && response.body() != null) {

                val weather = response.body()!!

                val background = WeatherBackgroundManager.getBackground(
                    weather.current.condition.code,
                    weather.current.is_day
                )

                binding.headerLayout.headerRoot.setBackgroundResource(background)

                // City name

                binding.headerLayout.tvForecastCity.text =
                    weather.location.name

// Date / Last updated
                binding.headerLayout.tvForecastDate.text =
                    "Updated at ${formatLastUpdated(weather.current.last_updated)}"

// Temperature
                binding.headerLayout.tvForecastTemp.text =
                    "${weather.current.temp_c.toInt()}°"

// Weather condition
                binding.headerLayout.tvForecastCondition.text =
                    weather.current.condition.text

// Feels like
                binding.headerLayout.tvForecastFeels.text =
                    "Feels Like ${weather.current.feelslike_c.toInt()}°"

                Glide.with(this)
                    .load("https:${weather.current.condition.icon}")
                    .into(binding.headerLayout.imgForecastWeather)

                // Forecast list
                binding.forecastLayout.rvForecast.layoutManager =
                    LinearLayoutManager(this)

                binding.forecastLayout.rvForecast.adapter =
                    ForecastAdapter(weather.forecast.forecastday)

            }

        }

    }

    private fun formatLastUpdated(lastUpdated: String): String {

        return try {

            val inputFormat = SimpleDateFormat(
                "yyyy-MM-dd HH:mm",
                Locale.getDefault()
            )

            val outputFormat = SimpleDateFormat(
                "hh:mm a",
                Locale.getDefault()
            )

            val date = inputFormat.parse(lastUpdated)

            outputFormat.format(date!!)

        } catch (e: Exception) {

            lastUpdated

        }

    }

    override fun finish() {
        super.finish()

        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }
}