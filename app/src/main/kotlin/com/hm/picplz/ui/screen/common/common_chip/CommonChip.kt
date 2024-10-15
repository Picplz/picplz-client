import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.screen.common.common_chip.CommonChipIntent.*
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography
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
) {
    val currentState = viewModel.state.collectAsState().value

    LaunchedEffect(value) {
        viewModel.handleIntent(SetInitialValue(value))
    }

    if (currentState.isEditing) {
        TextField(
            value = currentState.value,
            onValueChange = { newValue ->
                viewModel.handleIntent(UpdateValue(newValue))
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.handleIntent(FinishEditing)
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
    } else {
        Row(
            modifier = Modifier
                .clickable {
                    viewModel.handleIntent(StartEditing)
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
                    .padding(
                        horizontal = 12.dp,
                        vertical = 10.dp
                    ),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) selectedTextColor else unselectedTextColor,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonChipPreviewTrue() {
    PicplzTheme {
        CommonChip(value = "안뇽", isSelected = true)
    }
}

@Preview(showBackground = true)
@Composable
fun CommonChipPreviewFalse() {
    PicplzTheme {
        CommonChip(value = "안뇽", isSelected = false)
    }
}
