package com.example.yandexcup_semifinals.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.yandexcup_semifinals.R
import com.example.yandexcup_semifinals.ui.theme.ActiveColor
import com.example.yandexcup_semifinals.ui.theme.NotActiveColor

@Composable
fun BottomPanel(
    modifier: Modifier = Modifier,
    selectedColor: Color,
    selectedAction: BottomPanelActionsType,
    isLoading: Boolean,
    isPlaying: Boolean,
    onChangeSelectedItem: (BottomPanelActionsType) -> Unit
) {


    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        items(bottomPanelActions){ item ->
            when(item.type){
                BottomPanelActionsType.COLOR -> {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .border(if (selectedColor == MaterialTheme.colorScheme.background) 1.dp else 0.dp, NotActiveColor, CircleShape)
                            .background(selectedColor, CircleShape)
                            .clickable(!isLoading && !isPlaying) {
                                onChangeSelectedItem(BottomPanelActionsType.COLOR)
                            }
                    )
                }
                else -> {
                    Icon(
                        painter = painterResource(id = item.icon?:R.drawable.square),
                        contentDescription = item.contentDes,
                        tint = if (selectedAction == item.type) ActiveColor else MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable(!isLoading && !isPlaying) {
                            onChangeSelectedItem(item.type)
                        }
                    )
                }
            }

        }
    }
}



private val bottomPanelActions = listOf(
    BottomPanelAction(
        BottomPanelActionsType.DUBLICATE_CANVAS,
        R.drawable.duplicate,
        "DUBLICATE_CANVAS"
    ),
    BottomPanelAction(
        BottomPanelActionsType.CANVAS_GENERATOR,
        R.drawable.idea,
        "CANVAS_GENERATOR"
    ),
    BottomPanelAction(
        BottomPanelActionsType.PENCIL,
        R.drawable.pencil_icon,
        "PENCIL"
    ),
    BottomPanelAction(
        BottomPanelActionsType.BRUSH,
        R.drawable.brush_icon,
        "BRUSH"
    ),
    BottomPanelAction(
        BottomPanelActionsType.ERASE,
        R.drawable.erase_icon,
        "ERASE"
    ),
    BottomPanelAction(
        BottomPanelActionsType.FIGURE,
        R.drawable.instruments_icon,
        "FIGURE"
    ),
    BottomPanelAction(
        BottomPanelActionsType.COLOR,
        null,
        "COLOR"
    ),
    BottomPanelAction(
        BottomPanelActionsType.CHANGE_WIDTH,
        R.drawable.width,
        "PENCIL_WIDTH"
    ),
)