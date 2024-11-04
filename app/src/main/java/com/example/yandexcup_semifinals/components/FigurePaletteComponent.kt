package com.example.yandexcup_semifinals.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.yandexcup_semifinals.R
import com.example.yandexcup_semifinals.canvas.DrawFigure
import com.example.yandexcup_semifinals.ui.theme.ToastBackground
import com.example.yandexcup_semifinals.ui.theme.ToastBorder

@Composable
fun FigurePaletteComponent(
    modifier: Modifier = Modifier,
    onChangeSelectedFigure: (DrawFigure) -> Unit,
) {

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .border(1.dp, ToastBorder, RoundedCornerShape(4.dp))
            .background(ToastBackground, RoundedCornerShape(4.dp))
            .blur(12.dp)
            .padding(horizontal = 18.dp, vertical = 17.dp),
        horizontalArrangement = Arrangement.spacedBy(17.dp)
    ) {

        figurePalette.forEach { figure ->
            Icon(
                painter = painterResource(id = figure.icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        onChangeSelectedFigure(figure.type)
                    }
            )
        }
    }

}



private val figurePalette = listOf(
    FigureData(
        icon = R.drawable.square,
        type = DrawFigure.SQUARE
    ),
    FigureData(
        icon = R.drawable.star,
        type = DrawFigure.STAR
    ),
    FigureData(
        icon = R.drawable.triangle,
        type = DrawFigure.TRIANGLE
    ),
    FigureData(
        icon = R.drawable.arrow_up,
        type = DrawFigure.ARROW
    ),
)
