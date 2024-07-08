package com.ahmetocak.android_weather_app.feature.home

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.ahmetocak.android_weather_app.core.BaseFragment
import com.ahmetocak.android_weather_app.data.LocationTracker
import com.ahmetocak.android_weather_app.databinding.FragmentHomeScreenBinding
import com.ahmetocak.android_weather_app.feature.home.adapter.DailyForecastAdapter
import com.ahmetocak.android_weather_app.feature.home.adapter.HourlyForecastAdapter
import com.ahmetocak.android_weather_app.ui.ItemDailyForecastModelList
import com.ahmetocak.android_weather_app.ui.PaddingDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeScreenFragment : BaseFragment<FragmentHomeScreenBinding>() {

    @Inject
    lateinit var locationTracker: LocationTracker

    private val viewModel: HomeViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeScreenBinding = FragmentHomeScreenBinding.inflate(inflater, container, false)

    override fun FragmentHomeScreenBinding.onMain() {
        val threeHourlyForecastAdapter = HourlyForecastAdapter()
        includeWeatherCard.rvTodayWeatherData.apply {
            adapter = threeHourlyForecastAdapter
            addItemDecoration(PaddingDecoration(16, 16, 0, 0))
        }

        val dailyForecastAdapter = DailyForecastAdapter()
        rvDailyForecast.apply {
            adapter = dailyForecastAdapter
            addItemDecoration(PaddingDecoration(0, 0, 16, 16))
        }

        tvViewDetails.setOnClickListener {
            with(viewModel.uiState.value) {
                if (currentWeatherInfo != null && dailyForecast.isNotEmpty() && viewModel.threeHourlyForecastData != null) {
                    viewModel.threeHourlyForecastData?.let { forecast ->
                        findNavController().navigate(
                            HomeScreenFragmentDirections.actionHomeScreenFragmentToWeatherDetailScreenFragment(
                                currentWeatherInfo,
                                ItemDailyForecastModelList(dailyForecast),
                                forecast,
                                DateFormat.is24HourFormat(context)
                            )
                        )
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { uiState ->
                    isLoading = with(uiState.dataStatus) {
                        currentWeatherDataStatus == Status.LOADING || weatherForecastDataStatus == Status.LOADING
                    }
                    isError = uiState.isError && isLoading != true
                    includeError.btnTryAgain.setOnClickListener {
                        viewModel.reTry()
                        getWeatherData()
                    }

                    if (uiState.uiEvents.isNotEmpty()) {
                        when (uiState.uiEvents.first()) {
                            is HomeScreenUiEvent.Init -> getWeatherData()
                        }
                        viewModel.consumedUiEvent()
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
        val locationManager = requireActivity().application.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager
        val isGpsAndNetworkEnabled =
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (isGpsAndNetworkEnabled) {
            return
        } else {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}