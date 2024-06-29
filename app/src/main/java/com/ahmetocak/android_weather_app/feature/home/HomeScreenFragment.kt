package com.ahmetocak.android_weather_app.feature.home

import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ahmetocak.android_weather_app.databinding.FragmentHomeScreenBinding
import com.ahmetocak.android_weather_app.feature.home.adapter.HourlyForecastAdapter
import com.ahmetocak.android_weather_app.ui.PaddingDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeScreenFragment : Fragment() {

    private var _binding: FragmentHomeScreenBinding? = null
    private val binding: FragmentHomeScreenBinding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCurrentWeatherData(41.0082, 28.9784)
        viewModel.getThreeHourlyForecast(41.0082, 28.9784, DateFormat.is24HourFormat(context))

        val threeHourlyForecastAdapter = HourlyForecastAdapter()
        binding.rvTodayWeatherData.apply {
            adapter = threeHourlyForecastAdapter
            addItemDecoration(PaddingDecoration(16, 16, 0 ,0))
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    binding.isLoading = with(uiState.dataStatus) {
                        currentWeatherDataStatus == Status.LOADING || weatherForecastDataStatus == Status.LOADING
                    }

                    if (uiState.currentWeatherInfo != null) {
                        binding.currentWeatherInfo = uiState.currentWeatherInfo
                    }

                    if (uiState.todayThreeHourlyForecast.isNotEmpty()) {
                        threeHourlyForecastAdapter.submitList(uiState.todayThreeHourlyForecast)
                    }

                    if (uiState.errorMessage.isNotEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            uiState.errorMessage.first(),
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.consumedErrorMessage()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}