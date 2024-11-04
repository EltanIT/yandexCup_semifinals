package com.example.yandexcup_semifinals.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.yandexcup_semifinals.ui.theme.NotActiveColor

@Composable
fun CanvasPlayDialog(
    onClose: () -> Unit,
    speedInML: Long,
    onChangeSpeedPlay: (Int) -> Unit,
    onPlay: () -> Unit
) {

    var count by remember{
        mutableStateOf(speedInML.toString())
    }

    var countIsValid by remember{
        mutableStateOf(true)
    }


    val context = LocalContext.current

    Dialog(
        onDismissRequest = onClose
    ){
        Box(
            Modifier
                .background(
                    NotActiveColor.copy(
                        alpha = 0.15f
                    )
                )
                .fillMaxSize()
                .clickable {
                           onClose()
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp))
                    .padding(horizontal = 46.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Укажите скорость смены кадров при воспроизведении в миллисекундах",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                TextField(
                    value = count,
                    onValueChange = {
                        count = it
                        if (it.isEmpty()){
                            countIsValid = false
                            return@TextField
                        }
                        try {
                            it.toInt()
                            countIsValid = true
                        }catch (e: NumberFormatException){
                            countIsValid = false
                            Toast.makeText(context, "Невозможное значение", Toast.LENGTH_SHORT).show()
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = {
                        Text(
                            text = "Скорость",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 11.sp
                            ),
                        )
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        if (countIsValid){
                            onChangeSpeedPlay(count.toInt())
                            onPlay()
                        }

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onBackground
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 12.dp
                    )
                ) {
                    Text(
                        text = "Пуск",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.background,
                            fontSize = 14.sp
                        ),
                        modifier = Modifier.padding(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

    }
}