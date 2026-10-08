package com.pv.skycastweather.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedCityViewModel : ViewModel() {

    private val _selectedCity = MutableLiveData<String>()
    val selectedCity: LiveData<String>
        get() = _selectedCity

    fun selectCity(city: String) {

        _selectedCity.value = city

    }

}