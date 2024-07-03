package com.ahmetocak.android_weather_app.feature.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.ahmetocak.android_weather_app.databinding.FragmentWeatherDetailScreenBinding
import com.ahmetocak.android_weather_app.feature.detail.adapter.OnDayClickListener
import com.ahmetocak.android_weather_app.feature.detail.adapter.SelectableDailyForecastAdapter
import com.ahmetocak.android_weather_app.feature.home.adapter.HourlyForecastAdapter
import com.ahmetocak.android_weather_app.ui.ItemDailyForecastModel
import com.ahmetocak.android_weather_app.ui.PaddingDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherDetailScreenFragment : Fragment() {

    private var _binding: FragmentWeatherDetailScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WeatherDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherDetailScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mtToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val dailyForecastAdapter = SelectableDailyForecastAdapter(
            onDayClickListener = object : OnDayClickListener {
                override fun onDayClick(itemDailyForecastModel: ItemDailyForecastModel) {
                    viewModel.setWeatherData(itemDailyForecastModel)
                }
            }
        )
        binding.rvDaily.apply {
            adapter = dailyForecastAdapter
            addItemDecoration(PaddingDecoration(16, 16, 0 , 0))

        }

        val hourlyForecastAdapter = HourlyForecastAdapter()
        binding.includeWeatherCard.rvTodayWeatherData.apply {
            adapter = hourlyForecastAdapter
            addItemDecoration(PaddingDecoration(16, 16, 0, 0))
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    with(binding) {
                        if (uiState.currentWeatherInfo != null) {
                            currentWeatherInfo = uiState.currentWeatherInfo
                        }

                        if (uiState.dailyForecast.isNotEmpty()) {
                            dailyForecastAdapter.submitList(uiState.dailyForecast)
                        }

                        if (uiState.weatherDetails != null) {
                            weatherDetails = uiState.weatherDetails
                        }

                        if (uiState.todayThreeHourlyForecast.isNotEmpty()) {
                            hourlyForecastAdapter.submitList(uiState.todayThreeHourlyForecast)
                        }
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