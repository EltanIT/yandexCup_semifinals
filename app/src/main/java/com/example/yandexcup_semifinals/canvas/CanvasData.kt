package com.example.yandexcup_semifinals.canvas

data class CanvasData(
    val paths: List<PathData> = emptyList(),
    val historyPaths: List<PathData> = emptyList(),
)