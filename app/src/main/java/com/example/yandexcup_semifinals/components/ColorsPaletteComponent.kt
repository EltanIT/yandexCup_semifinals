package com.example.yandexcup_semifinals.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.yandexcup_semifinals.R
import com.example.yandexcup_semifinals.ui.canvasPalette
import com.example.yandexcup_semifinals.ui.mainCanvasColors
import com.example.yandexcup_semifinals.ui.theme.ActiveColor
import com.example.yandexcup_semifinals.ui.theme.ToastBackground
import com.example.yandexcup_semifinals.ui.theme.ToastBorder
import com.example.yandexcup_semifinals.ui.theme.White

@Composable
fun ColorsPaletteComponent(
    modifier: Modifier = Modifier,
    onChangeSelectedColor: (Color) -> Unit,
) {

    var paletteIsOpen by remember{
        mutableStateOf(false)
    }

    Column(
        modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        if (paletteIsOpen){
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .border(1.dp, ToastBorder, RoundedCornerShape(4.dp))
                    .background(ToastBackground, RoundedCornerShape(4.dp))
                    .blur(30.dp)
                    .padding(horizontal = 18.dp, vertical = 17.dp),
                horizontalArrangement = Arrangement.spacedBy(21.dp)
            ) {
                canvasPalette.forEach { palette ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        palette.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .clickable {
                                        onChangeSelectedColor(color)
                                    }
                            )
                        }
                    }
                }
            }
        }



        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .border(1.dp, ToastBorder, RoundedCornerShape(4.dp))
                .background(ToastBackground, RoundedCornerShape(4.dp))
                .blur(12.dp)
                .padding(horizontal = 18.dp, vertical = 17.dp),
            horizontalArrangement = Arrangement.spacedBy(17.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.palette),
                contentDescription = "palette",
                tint = if (paletteIsOpen) ActiveColor else White,
                modifier = Modifier.clickable {
                    paletteIsOpen = !paletteIsOpen
                }
            )
            mainCanvasColors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(color)
                        .clickable {
                            onChangeSelectedColor(color)
                        }
                )
            }
        }


    }

}
