package com.example.yandexcup_semifinals.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.yandexcup_semifinals.R
import com.example.yandexcup_semifinals.canvas.DrawFigure
import com.example.yandexcup_semifinals.ui.theme.ToastBackground
import com.example.yandexcup_semifinals.ui.theme.ToastBorder
import kotlin.math.roundToInt

@Composable
fun ChangeWidthSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onChangeWidth: (Float) -> Unit,
) {

    val density = LocalDensity.current

    val minWidth = 0.1f
    val maxWidth = 20f

    var sliderWidth by remember{
        mutableFloatStateOf(0f)
    }

    var valueLocal by remember{
        mutableFloatStateOf(value)
    }
    valueLocal = value


    var sliderPos by remember{
        mutableStateOf(0.dp)
    }




    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .border(1.dp, ToastBorder, RoundedCornerShape(4.dp))
            .background(ToastBackground, RoundedCornerShape(4.dp))
            .blur(20.dp)
            .padding(16.dp)
            .clickable {},
        contentAlignment = Alignment.CenterEnd
    ) {

        Icon(
            painter = painterResource(id = R.drawable.slider),
            contentDescription = "slider",
            modifier = Modifier
                .onGloballyPositioned { pos ->
                    sliderWidth = pos.size.width.toFloat()
                    sliderPos = - with(density){((sliderWidth*value) / maxWidth).toDp() }
                }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        if (change.position.x > 0 || change.position.x < sliderWidth){
                            var value = (maxWidth * (sliderWidth-change.position.x) / sliderWidth) - (maxWidth*dragAmount / sliderWidth)

                            if (value<minWidth){
                                value = minWidth
                            }else if(value>maxWidth){
                                value = maxWidth
                            }

                            onChangeWidth(value)
                            sliderPos = - with(density){((sliderWidth*valueLocal) / maxWidth).toDp() }
                            Log.i("slider", "value: ${valueLocal} change: ${change.position.x} slider: ${sliderPos} dragAmount: ${dragAmount}")
                        }
                    }
                }
        )

        Box(
            modifier = Modifier
                .offset(x = sliderPos)
                .size(20.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onBackground, CircleShape)
        )


    }

}


