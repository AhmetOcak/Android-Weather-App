package com.ahmetocak.android_weather_app.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class PaddingDecoration(
    private val left: Int,
    private val right: Int,
    private val top: Int,
    private val bottom: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            left = this@PaddingDecoration.left
            right = this@PaddingDecoration.right
            top = this@PaddingDecoration.top
            bottom = this@PaddingDecoration.bottom
        }
    }
}