package com.ahmetocak.android_weather_app.feature.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahmetocak.android_weather_app.databinding.ItemThreeHourForecastBinding
import com.ahmetocak.android_weather_app.ui.ItemThreeHourForecastModel

class HourlyForecastAdapter :
    ListAdapter<ItemThreeHourForecastModel, HourlyForecastAdapter.HourlyForecastViewHolder>(
        HourlyForecastDiffUtilCallback()
    ) {

    class HourlyForecastViewHolder(
        private val binding: ItemThreeHourForecastBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(itemThreeHourForecastModel: ItemThreeHourForecastModel) {
            binding.itemThreeHourForecastModel = itemThreeHourForecastModel
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastViewHolder {
        return HourlyForecastViewHolder(
            ItemThreeHourForecastBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HourlyForecastViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(itemThreeHourForecastModel = item)
    }

    class HourlyForecastDiffUtilCallback : DiffUtil.ItemCallback<ItemThreeHourForecastModel>() {
        override fun areItemsTheSame(
            oldItem: ItemThreeHourForecastModel,
            newItem: ItemThreeHourForecastModel
        ): Boolean {
            return oldItem.weatherDate == newItem.weatherDate
        }

        override fun areContentsTheSame(
            oldItem: ItemThreeHourForecastModel,
            newItem: ItemThreeHourForecastModel
        ): Boolean {
            return oldItem == newItem
        }

    }
}