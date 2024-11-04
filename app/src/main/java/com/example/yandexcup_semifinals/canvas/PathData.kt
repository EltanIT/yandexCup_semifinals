package com.example.yandexcup_semifinals.canvas

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import com.example.yandexcup_semifinals.ui.theme.White

data class PathData(
    val path: Path = Path(),
    val color: Color = White,
    val drawType: DrawType
)