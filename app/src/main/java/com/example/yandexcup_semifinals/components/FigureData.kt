package com.example.yandexcup_semifinals.components

import androidx.annotation.DrawableRes
import com.example.yandexcup_semifinals.canvas.DrawFigure

data class FigureData(
    @DrawableRes val icon: Int,
    val type: DrawFigure
)
