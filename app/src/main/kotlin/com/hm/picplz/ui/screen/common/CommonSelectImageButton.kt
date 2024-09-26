package com.hm.picplz.ui.screen.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hm.picplz.R

enum class SelectionState {
    UNSELECTED, SELECTED, DESELECTED
}

@Composable
fun CommonSelectImageButton(
    text: String,
    selectionState: SelectionState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes unSelectedIconResId: Int? = null,
    @DrawableRes deSelectedIconResId: Int? = null,
    @DrawableRes selectedIconResId: Int? = null,
    contentColor: Color = MainThemeColor.Black,
) {
    val iconResId = when (selectionState) {
        SelectionState.UNSELECTED -> unSelectedIconResId
        SelectionState.DESELECTED -> deSelectedIconResId
        SelectionState.SELECTED -> selectedIconResId
    }

    Box(
        modifier = modifier
            .width(if (selectionState == SelectionState.SELECTED) 160.dp else 120.dp)
            .height(if (selectionState == SelectionState.SELECTED) 190.dp else 150.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
        ) {
            iconResId?.let { iconResId ->
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = "User Type Button",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(if (selectionState == SelectionState.SELECTED) 160.dp else 120.dp)
                        .clickable { onClick() }
                )
            }
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonSelectImageButtonPreview() {
    var isSelected by remember { mutableStateOf(false) }

    PicplzTheme {
        CommonSelectImageButton(
            text = "라벨",
            selectionState = SelectionState.UNSELECTED,
            onClick = { isSelected = !isSelected },
            unSelectedIconResId = R.drawable.photographer_unselected,
            deSelectedIconResId = R.drawable.photographer_deselected,
            selectedIconResId = R.drawable.photographer_selected
        )
    }
}