package com.ahmetocak.android_weather_app.feature.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
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
import androidx.navigation.fragment.findNavController
import com.ahmetocak.android_weather_app.data.LocationTracker
import com.ahmetocak.android_weather_app.databinding.FragmentHomeScreenBinding
import com.ahmetocak.android_weather_app.feature.home.adapter.DailyForecastAdapter
import com.ahmetocak.android_weather_app.feature.home.adapter.HourlyForecastAdapter
import com.ahmetocak.android_weather_app.ui.ItemDailyForecastModelList
import com.ahmetocak.android_weather_app.ui.PaddingDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeScreenFragment : Fragment() {

    private var _binding: FragmentHomeScreenBinding? = null
    private val binding: FragmentHomeScreenBinding get() = _binding!!

    @Inject
    lateinit var locationTracker: LocationTracker

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

        val threeHourlyForecastAdapter = HourlyForecastAdapter()
        binding.includeWeatherCard.rvTodayWeatherData.apply {
            adapter = threeHourlyForecastAdapter
            addItemDecoration(PaddingDecoration(16, 16, 0, 0))
        }

        val dailyForecastAdapter = DailyForecastAdapter()
        binding.rvDailyForecast.apply {
            adapter = dailyForecastAdapter
            addItemDecoration(PaddingDecoration(0, 0, 16, 16))
        }

        binding.tvViewDetails.setOnClickListener {
            with(viewModel.uiState.value) {
                if (currentWeatherInfo != null && dailyForecast.isNotEmpty() && viewModel.threeHourlyForecastData != null) {
                    viewModel.threeHourlyForecastData?.let { forecast ->
                        findNavController().navigate(
                            HomeScreenFragmentDirections.actionHomeScreenFragmentToWeatherDetailScreenFragment(
                                currentWeatherInfo,
                                ItemDailyForecastModelList(dailyForecast),
                                forecast
                            )
                        )
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    binding.isLoading = with(uiState.dataStatus) {
                        currentWeatherDataStatus == Status.LOADING || weatherForecastDataStatus == Status.LOADING
                    }
                    if (uiState.uiEvents.isNotEmpty()) {
                        when (uiState.uiEvents.first()) {
                            is HomeScreenUiEvent.Init -> getWeatherData()
                        }
                    }

                    with(uiState) {
                        if (
                            currentWeatherInfo != null &&
                            todayThreeHourlyForecast.isNotEmpty() &&
                            dailyForecast.isNotEmpty()
                        ) {
                            binding.currentWeatherInfo = uiState.currentWeatherInfo
                            threeHourlyForecastAdapter.submitList(uiState.todayThreeHourlyForecast)
                            dailyForecastAdapter.submitList(uiState.dailyForecast)
                        }
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

    private fun getWeatherData() {
        val is24HourFormat = DateFormat.is24HourFormat(context)
        locationTracker.getLocation(
            onFailure = {
                viewModel.getLocationFromCache(
                    is24HourFormat = is24HourFormat,
                    onCacheNull = this@HomeScreenFragment::askUserToTurnOnGps
                )
            },
            onSuccess = { location ->
                if (location != null) {
                    with(location) {
                        viewModel.getCurrentWeatherData(latitude, longitude)
                        viewModel.getThreeHourlyForecast(
                            latitude,
                            longitude,
                            is24HourFormat
                        )
                        viewModel.cacheLocation(latitude, longitude)
                    }
                } else {
                    viewModel.getLocationFromCache(
                        is24HourFormat = is24HourFormat,
                        onCacheNull = this@HomeScreenFragment::askUserToTurnOnGps
                    )
                }
            }
        )
    }

    private fun askUserToTurnOnGps() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        Toast.makeText(
            requireContext(),
            "Please enable GPS and restart the app.",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}