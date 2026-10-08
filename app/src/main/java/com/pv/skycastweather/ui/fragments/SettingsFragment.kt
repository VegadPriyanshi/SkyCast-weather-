package com.pv.skycastweather.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pv.skycastweather.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Appearance
        binding.layoutAppearance.setOnClickListener {

            AppearanceBottomSheet().show(
                parentFragmentManager,
                "AppearanceBottomSheet"
            )

        }

        // Temperature Unit
        binding.layoutTemperature.setOnClickListener {

        }

        // Location
        binding.layoutLocation.setOnClickListener {

        }

        // About
        binding.layoutAbout.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}