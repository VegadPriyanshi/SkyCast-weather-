package com.pv.skycastweather.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pv.skycastweather.databinding.BottomSheetAppearanceBinding
import com.pv.skycastweather.utils.ThemeManager

class AppearanceBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAppearanceBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = BottomSheetAppearanceBinding.inflate(
            inflater,
            container,
            false
        )

        val currentTheme = ThemeManager.getTheme(requireContext())

        when (currentTheme) {
            ThemeManager.SYSTEM -> binding.rbSystem.isChecked = true
            ThemeManager.LIGHT -> binding.rbLight.isChecked = true
            ThemeManager.DARK -> binding.rbDark.isChecked = true
        }

        binding.rbSystem.setOnClickListener {

            ThemeManager.saveTheme(
                requireContext(),
                ThemeManager.SYSTEM
            )

            ThemeManager.applyTheme(
                ThemeManager.SYSTEM
            )

            dismiss()
        }

        binding.rbLight.setOnClickListener {

            ThemeManager.saveTheme(
                requireContext(),
                ThemeManager.LIGHT
            )

            ThemeManager.applyTheme(
                ThemeManager.LIGHT
            )

            dismiss()
        }

        binding.rbDark.setOnClickListener {

            ThemeManager.saveTheme(
                requireContext(),
                ThemeManager.DARK
            )

            ThemeManager.applyTheme(
                ThemeManager.DARK
            )

            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}