package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hm.picplz.R

@Composable
fun CommonTopBar(
    text: String,
    onClickBack: () -> Unit,
    modifier: Modifier = Modifier,
    boxHeight: Dp = 50.dp,
    textStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    ),
    paddingStart: Dp = 0.dp,
    iconSize: Dp = 35.dp,
    spacerWidth: Dp = 50.dp
) {
    Box(
        modifier = Modifier
            .height(boxHeight)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onClickBack,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = paddingStart)
            ) {
                Image(
                    painter = painterResource(R.drawable.arrow_left),
                    contentDescription = "arrow left",
                    modifier = Modifier.size(iconSize)
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    style = textStyle
                )
            }
            Spacer(modifier = Modifier.width(spacerWidth))
        }
    }
}