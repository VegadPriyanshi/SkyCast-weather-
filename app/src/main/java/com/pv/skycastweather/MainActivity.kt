package com.pv.skycastweather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.pv.skycastweather.databinding.ActivityMainBinding
import com.pv.skycastweather.ui.fragments.FavoritesFragment
import com.pv.skycastweather.ui.fragments.ForecastFragment
import com.pv.skycastweather.ui.fragments.HomeFragment
import com.pv.skycastweather.ui.fragments.SearchFragment
import com.pv.skycastweather.ui.fragments.SettingsFragment
import com.pv.skycastweather.utils.ThemeManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var homeFragment: HomeFragment
    private lateinit var forecastFragment: ForecastFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var favoritesFragment: FavoritesFragment
    private lateinit var settingsFragment: SettingsFragment

    private lateinit var activeFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {

        ThemeManager.applyTheme(
            ThemeManager.getTheme(this)
        )
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {

            homeFragment = HomeFragment()
            forecastFragment = ForecastFragment()
            searchFragment = SearchFragment()
            favoritesFragment = FavoritesFragment()
            settingsFragment = SettingsFragment()

            activeFragment = homeFragment

            supportFragmentManager.beginTransaction()

                .add(R.id.fragmentContainer, settingsFragment, "SETTINGS")
                .hide(settingsFragment)

                .add(R.id.fragmentContainer, favoritesFragment, "FAVORITES")
                .hide(favoritesFragment)

                .add(R.id.fragmentContainer, searchFragment, "SEARCH")
                .hide(searchFragment)

                .add(R.id.fragmentContainer, forecastFragment, "FORECAST")
                .hide(forecastFragment)

                .add(R.id.fragmentContainer, homeFragment, "HOME")

                .commit()

        } else {

            homeFragment =
                supportFragmentManager.findFragmentByTag("HOME") as HomeFragment

            forecastFragment =
                supportFragmentManager.findFragmentByTag("FORECAST") as ForecastFragment

            searchFragment =
                supportFragmentManager.findFragmentByTag("SEARCH") as SearchFragment

            favoritesFragment =
                supportFragmentManager.findFragmentByTag("FAVORITES") as FavoritesFragment

            settingsFragment =
                supportFragmentManager.findFragmentByTag("SETTINGS") as SettingsFragment

            activeFragment = listOf(
                homeFragment,
                forecastFragment,
                searchFragment,
                favoritesFragment,
                settingsFragment
            ).first { !it.isHidden }
        }
        binding.bottomNavigation.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.nav_home -> {
                    showFragment(homeFragment)
                    true
                }

                R.id.nav_forecast -> {
                    showFragment(forecastFragment)
                    true
                }

                R.id.nav_search -> {
                    showFragment(searchFragment)
                    true
                }

                R.id.nav_favorites -> {
                    showFragment(favoritesFragment)
                    true
                }

                R.id.nav_settings -> {
                    showFragment(settingsFragment)
                    true
                }

                else -> false
            }

        }
    }

    fun openForecastFragment() {

        binding.bottomNavigation.selectedItemId = R.id.nav_forecast

    }

    fun openHomeFragment() {

        binding.bottomNavigation.selectedItemId = R.id.nav_home

    }

    private fun showFragment(fragment: Fragment) {

        // Don't do anything if the selected fragment is already visible
        if (activeFragment == fragment) return

        supportFragmentManager.beginTransaction()
            .hide(activeFragment)
            .show(fragment)
            .commit()

        activeFragment = fragment

    }
}