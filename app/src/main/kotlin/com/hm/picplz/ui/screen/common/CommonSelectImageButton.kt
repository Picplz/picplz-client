package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.hm.picplz.R


@Composable
fun CommonSelectImageButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @androidx.annotation.DrawableRes iconResId: Int? = null,
    selectedColor: Color = MainThemeColor.Olive,
    unselectedColor: Color = Color.Transparent,
    backgroundColor: Color = Color.White,
    contentColor: Color = MainThemeColor.Black,
    elevation: Dp = 5.dp,
    shape: RoundedCornerShape = RoundedCornerShape(16.dp)
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = Color.Gray,
                spotColor = Color.Gray
            )
            .background(
                color = backgroundColor,
                shape = shape
            )
            .border(
                width = 2.dp,
                color = if (isSelected) selectedColor else unselectedColor,
                shape = shape
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = shape,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (iconResId != null) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(20.dp))
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
            isSelected = false,
            onClick = { isSelected = !isSelected },
            iconResId = R.drawable.logo
        )
    }
}