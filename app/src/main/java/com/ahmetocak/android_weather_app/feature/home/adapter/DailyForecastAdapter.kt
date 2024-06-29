package com.ahmetocak.android_weather_app.feature.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahmetocak.android_weather_app.databinding.ItemDailyForecastBinding
import com.ahmetocak.android_weather_app.ui.ItemDailyForecastModel

class DailyForecastAdapter :
    ListAdapter<ItemDailyForecastModel, DailyForecastAdapter.DailyForecastViewHolder>(
        DiffUtilCallback()
    ) {

    class DailyForecastViewHolder(
        private val binding: ItemDailyForecastBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(itemDailyForecastModel: ItemDailyForecastModel) {
            binding.itemDailyForecastModel = itemDailyForecastModel
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        return DailyForecastViewHolder(
            ItemDailyForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<ItemDailyForecastModel>() {
        override fun areItemsTheSame(
            oldItem: ItemDailyForecastModel,
            newItem: ItemDailyForecastModel
        ): Boolean {
            return oldItem.weatherDate == newItem.weatherDate
        }

        override fun areContentsTheSame(
            oldItem: ItemDailyForecastModel,
            newItem: ItemDailyForecastModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}