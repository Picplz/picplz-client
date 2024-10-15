import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hm.picplz.data.model.ChipMode
import com.hm.picplz.data.model.ChipMode.*
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.screen.common.common_chip.CommonChipIntent.*
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.Pretendard
import com.hm.picplz.viewmodel.common.CommonChipViewModel

@Composable
fun CommonChip(
    viewModel: CommonChipViewModel = viewModel(),
    value: String,
    unselectedBorderColor: Color = MainThemeColor.Gray3,
    selectedBorderColor: Color = MainThemeColor.Black,
    unselectedTextColor: Color = MainThemeColor.Gray4,
    selectedTextColor: Color = MainThemeColor.Black,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    isEditable: Boolean = false,
    initialMode: ChipMode = DEFAULT
) {

    val currentState = viewModel.state.collectAsState().value

    LaunchedEffect(value) {
        viewModel.handleIntent(SetInitialValue(value))
        viewModel.handleIntent(SetChipMode(initialMode))
    }

    when (currentState.chipMode) {
        DEFAULT -> {
            Row(
                modifier = Modifier
                    .clickable {
                        onClick()
                    }
                    .height(40.dp)
                    .border(
                        width = 1.dp,
                        color = if (isSelected) selectedBorderColor else unselectedBorderColor,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .widthIn(min = 20.dp)
            ) {
                Text(
                    text = currentState.value,
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    style = TextStyle(
                        fontFamily = Pretendard,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 14.sp,
                    ),
                    color = if (isSelected) selectedTextColor else unselectedTextColor,
                )
            }
        }
        ADD -> {}
        EDIT -> {
            TextField(
                value = currentState.value,
                onValueChange = { newValue ->
                    viewModel.handleIntent(UpdateValue(newValue))
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.handleIntent(FinishEditing)
                        viewModel.handleIntent(SetChipMode(DEFAULT))
                    }
                ),
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .border(
                        width = 1.dp,
                        color = if (isSelected) selectedBorderColor else unselectedBorderColor,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .widthIn(min = 20.dp)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun CommonChipPreviewTrue() {
    PicplzTheme {
        CommonChip(value = "을지로 감성", isSelected = true, isEditable = true)
    }
}

@Preview(showBackground = true)
@Composable
fun CommonChipPreviewFalse() {
    PicplzTheme {
        CommonChip(value = "키치 감성", isSelected = false)
    }
}
