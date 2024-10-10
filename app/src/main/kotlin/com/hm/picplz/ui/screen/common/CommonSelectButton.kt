import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun CommonSelectButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(5.dp),
    width: Dp = 167.dp,
    unselectedContainerColor: Color = MainThemeColor.White,
    unselectedContentColor: Color = MainThemeColor.Black,
    selectedContainerColor: Color = MainThemeColor.Black,
    selectedContentColor: Color = MainThemeColor.White,
) {
    Button(
        modifier = modifier
            .width(width)
            .height(60.dp),
        onClick = onClick,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) selectedContainerColor else unselectedContainerColor,
            contentColor = if (isSelected) selectedContentColor else unselectedContentColor
        ),
        border = if (isSelected) null else BorderStroke(
            width = 1.dp,
            color = MainThemeColor.Gray3
        )
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 16.sp * 1.4,
                letterSpacing = 0.sp
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommonSelectButtonPreview() {
    var isSelected by remember { mutableStateOf(false) }

    PicplzTheme {
        CommonSelectButton(
            text = "있어요",
            isSelected = isSelected,
            onClick = {
                isSelected = !isSelected
            }
        )
    }
}
