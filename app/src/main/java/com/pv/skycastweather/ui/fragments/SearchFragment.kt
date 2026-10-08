package com.pv.skycastweather.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pv.skycastweather.MainActivity
import com.pv.skycastweather.adapter.PopularCityAdapter
import com.pv.skycastweather.data.model.PopularCity
import com.pv.skycastweather.databinding.FragmentSearchBinding
import androidx.lifecycle.ViewModelProvider
import com.pv.skycastweather.ui.home.SharedCityViewModel
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.pv.skycastweather.adapter.SearchCityAdapter
import com.pv.skycastweather.ui.search.SearchViewModel
import com.pv.skycastweather.adapter.RecentSearchAdapter
import com.pv.skycastweather.ui.search.SearchHistoryViewModel
import com.pv.skycastweather.ui.search.SearchState
import com.pv.skycastweather.utils.LocationHelper

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private lateinit var sharedCityViewModel: SharedCityViewModel
    private lateinit var searchViewModel: SearchViewModel

    private lateinit var searchHistoryViewModel: SearchHistoryViewModel



    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(
            inflater,
            container,
            false
        )
        sharedCityViewModel =
            ViewModelProvider(requireActivity())[SharedCityViewModel::class.java]

        searchViewModel =
            ViewModelProvider(this)[SearchViewModel::class.java]

        searchHistoryViewModel =
            ViewModelProvider(this)[SearchHistoryViewModel::class.java]

        recentSearchAdapter = RecentSearchAdapter { city ->

            sharedCityViewModel.selectCity(city.cityName)

            (activity as MainActivity).openHomeFragment()

        }

        binding.rvRecentSearches.layoutManager =
            LinearLayoutManager(requireContext())

        binding.rvRecentSearches.adapter = recentSearchAdapter

        setupPopularCities()
        observeSearchResults()
        observeSearchState()
        observeSelectedCity()
        observeRecentSearches()
        setupClearAll()                 // Recent Searches
        setupSearchResultsClearAll()    // Search Results
        setupSearch()
        setupCurrentLocation()

        return binding.root

    }

    private fun setupPopularCities() {

        val popularCities = listOf(

            PopularCity("Rajkot"),
            PopularCity("Ahmedabad"),
            PopularCity("Morbi"),
            PopularCity("Mumbai"),
            PopularCity("Delhi"),
            PopularCity("London"),
            PopularCity("Dubai")

        )

        binding.rvPopularCities.layoutManager =
            LinearLayoutManager(requireContext())

        binding.rvPopularCities.adapter =
            PopularCityAdapter(popularCities) { city ->

                sharedCityViewModel.selectCity(city.cityName)

                // Open Home screen
                (activity as MainActivity).openHomeFragment()

            }

    }

    private fun setupSearch() {

        binding.etCity.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

                searchViewModel.searchCities(
                    s.toString()
                )

            }

            override fun afterTextChanged(s: Editable?) {}

        }

        )
        binding.btnSearch.setOnClickListener {

            val city = binding.etCity.text.toString().trim()

            if (city.isEmpty()) {
                binding.etCity.error = "Enter city name"
                return@setOnClickListener
            }

            searchViewModel.validateCity(city)

        }

    }

    private fun setupCurrentLocation() {

        binding.cardCurrentLocation.setOnClickListener {

            if (LocationHelper.hasLocationPermission(requireActivity())) {

                LocationHelper.getCurrentLocation(
                    requireActivity(),
                    onLocationReceived = { latitude, longitude ->

                        sharedCityViewModel.selectCity("$latitude,$longitude")

                        (activity as MainActivity).openHomeFragment()

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
                    "Location permission not granted",
                    Toast.LENGTH_SHORT
                ).show()

            }

        }

    }

    private fun observeSearchResults() {

        searchViewModel.cities.observe(viewLifecycleOwner) { cities ->

            if (cities.isEmpty()) {

                binding.tvSearchResults.visibility = View.GONE
                binding.rvSearchResults.visibility = View.GONE
                binding.tvClearSearchResults.visibility = View.GONE

            } else {

                binding.tvSearchResults.visibility = View.VISIBLE
                binding.rvSearchResults.visibility = View.VISIBLE
                binding.tvClearSearchResults.visibility = View.VISIBLE

            }
            binding.rvSearchResults.layoutManager =
                LinearLayoutManager(requireContext())

            binding.rvSearchResults.adapter =
                SearchCityAdapter(cities) { city ->

                    // Save to Room
                    searchHistoryViewModel.insertSearch(city.name)

                    // Update Home
                    sharedCityViewModel.selectCity(city.name)

                    // Clear search box
                    binding.etCity.text?.clear()

                    // Hide suggestions
                    binding.tvSearchResults.visibility = View.GONE
                    binding.rvSearchResults.visibility = View.GONE

                    // Open Home
                    (activity as MainActivity).openHomeFragment()

                }
        }
    }


    private lateinit var recentSearchAdapter: RecentSearchAdapter

    private fun observeRecentSearches() {

        searchHistoryViewModel.recentSearches.observe(viewLifecycleOwner) { cities ->

            android.util.Log.d("RecentSearch", "Received ${cities.size} cities")

            recentSearchAdapter.updateList(cities)

            if (cities.isEmpty()) {

                binding.rvRecentSearches.visibility = View.GONE
                binding.tvClearAll.visibility = View.GONE

            } else {

                binding.rvRecentSearches.visibility = View.VISIBLE
                binding.tvClearAll.visibility = View.VISIBLE

            }

        }

    }

    private fun setupClearAll() {

        binding.tvClearAll.setOnClickListener {

            android.util.Log.d("ClearAll", "Button Clicked")

            searchHistoryViewModel.clearAll()

        }

    }

    private fun setupSearchResultsClearAll() {

        binding.tvClearSearchResults.setOnClickListener {

            searchViewModel.clearSearchResults()

            binding.etCity.text?.clear()

            binding.tvSearchMessage.visibility = View.GONE

        }

    }

    private fun observeSearchState() {

        searchViewModel.searchState.observe(viewLifecycleOwner) { state ->

            when (state) {

                is SearchState.Idle -> {

                    binding.tvSearchMessage.visibility = View.GONE

                }

                is SearchState.Loading -> {

                    binding.tvSearchMessage.visibility = View.GONE

                }

                is SearchState.Success -> {

                    binding.tvSearchMessage.visibility = View.GONE

                }

                is SearchState.Error -> {

                    binding.tvSearchResults.visibility = View.GONE
                    binding.rvSearchResults.visibility = View.GONE

                    binding.tvSearchMessage.visibility = View.VISIBLE
                    binding.tvSearchMessage.text = state.message

                }
            }
        }
    }


    private fun observeSelectedCity() {

        searchViewModel.selectedCity.observe(viewLifecycleOwner) { city ->

            searchHistoryViewModel.insertSearch(city)

            sharedCityViewModel.selectCity(city)

            binding.etCity.text?.clear()

            binding.tvSearchResults.visibility = View.GONE
            binding.rvSearchResults.visibility = View.GONE
            binding.tvSearchMessage.visibility = View.GONE

            (activity as MainActivity).openHomeFragment()

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null

    }

}