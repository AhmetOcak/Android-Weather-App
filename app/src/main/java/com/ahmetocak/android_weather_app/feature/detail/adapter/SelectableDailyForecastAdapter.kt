package com.ahmetocak.android_weather_app.feature.detail.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahmetocak.android_weather_app.databinding.ItemDailyForecastDetailBinding
import com.ahmetocak.android_weather_app.ui.ItemDailyForecastModel

class SelectableDailyForecastAdapter(private val onDayClickListener: OnDayClickListener) :
    ListAdapter<ItemDailyForecastModel, SelectableDailyForecastAdapter.DailyForecastViewHolder>(
        DiffUtilCallback()
    ), OnDayClickListener {

    private lateinit var selectedDay: String
    private var oldPosition: Int = 0

    class DailyForecastViewHolder(
        val binding: ItemDailyForecastDetailBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(itemDailyForecastModel: ItemDailyForecastModel, isSelectedDay: Boolean) {
            binding.itemModel = itemDailyForecastModel
            binding.mvDailyWeather.strokeColor = if (isSelectedDay) {
                Color.parseColor("#ffdb72")
            } else {
                Color.GRAY
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        return DailyForecastViewHolder(
            ItemDailyForecastDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        val item = getItem(position)

        // Initialize Selected Day
        if (!this::selectedDay.isInitialized) selectedDay = item.weatherDate.day

        holder.binding.mvDailyWeather.apply {
            setOnClickListener {
                this@SelectableDailyForecastAdapter.onDayClickListener.onDayClick(item)
                selectedDay = item.weatherDate.day

                notifyItemChanged(position)
                notifyItemChanged(oldPosition)
                oldPosition = holder.adapterPosition
            }
        }
        holder.bind(item, selectedDay == item.weatherDate.day)
    }

    override fun onDayClick(itemDailyForecastModel: ItemDailyForecastModel) {
        onDayClickListener.onDayClick(itemDailyForecastModel)
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<ItemDailyForecastModel>() {
        override fun areItemsTheSame(
            oldItem: ItemDailyForecastModel,
            newItem: ItemDailyForecastModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ItemDailyForecastModel,
            newItem: ItemDailyForecastModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}