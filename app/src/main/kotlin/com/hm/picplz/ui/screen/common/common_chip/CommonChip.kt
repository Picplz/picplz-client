import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import java.util.UUID

@Composable
fun CommonChip(
    initialMode: ChipMode = DEFAULT,
    id: String = UUID.randomUUID().toString(),
    label: String = "",
    viewModel: CommonChipViewModel = viewModel(key = id),
    unselectedBorderColor: Color = MainThemeColor.Gray3,
    selectedBorderColor: Color = MainThemeColor.Black,
    unselectedTextColor: Color = MainThemeColor.Gray4,
    selectedTextColor: Color = MainThemeColor.Black,
    isSelected: Boolean = false,
    onClickDefaultMode: () -> Unit = {},
    isEditable: Boolean = false,
    onAdd: (String) -> Unit = {},
    onUpdate: (String) -> Unit = {},
    isEditing: Boolean = false,
    onEdit: () -> Unit = {},
) {

    val currentState = viewModel.state.collectAsState().value
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(isEditing) {
        if (isEditing) {
            viewModel.handleIntent(SetChipMode(EDIT))
        } else {
            if (currentState.value.isNotEmpty()) {
                if (initialMode == ADD) onAdd(currentState.value)
                else if (initialMode == DEFAULT && label !== currentState.value) onUpdate(currentState.value)
            }
            viewModel.handleIntent(SetChipMode(initialMode))
            viewModel.handleIntent(SetValue(""))
        }
    }

    LaunchedEffect(currentState.chipMode) {
        if (currentState.chipMode == EDIT) {
            focusRequester.requestFocus()
        }
    }

    LaunchedEffect(label) {
        viewModel.handleIntent(SetValue(label))
        viewModel.handleIntent(SetChipMode(initialMode))
    }

    when (currentState.chipMode) {
        DEFAULT -> {
            Row(
                modifier = Modifier
                    .clickable {
                        onClickDefaultMode()
                    }
                    .height(40.dp)
                    .border(
                        width = 1.dp,
                        color = if (isSelected) selectedBorderColor else unselectedBorderColor,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .widthIn(min = 20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight(),
                    verticalAlignment = CenterVertically
                ) {
                    Text(
                        text = label,
                        modifier = Modifier
                            .padding(
                                horizontal = 12.dp,
                            ),
                        style = TextStyle(
                            fontFamily = Pretendard,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp,
                        ),
                        color = if (isSelected) selectedTextColor else unselectedTextColor,
                    )
                }
            }
        }
        ADD -> {
            Row(
                modifier = Modifier
                    .clickable {
                        onEdit()
                    }
                    .height(40.dp)
                    .border(
                        width = 1.dp,
                        color = MainThemeColor.Gray3,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .background(
                        color = MainThemeColor.Gray1
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight(),
                    verticalAlignment = CenterVertically
                ) {
                    Text(
                        text = "+직접 적어주세요",
                        modifier = Modifier
                            .padding(
                                horizontal = 12.dp,
                            ),
                        style = TextStyle(
                            fontFamily = Pretendard,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                        ),
                        color = MainThemeColor.Gray3,
                    )                }

            }
        }
        EDIT -> {
            BasicTextField(
                modifier = Modifier
                    .focusRequester(focusRequester),
                value = currentState.value,
                onValueChange = { newValue ->
                    viewModel.handleIntent(SetValue(newValue))
                },
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = Pretendard,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp,
                ),
                cursorBrush = SolidColor(Color.Black),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (initialMode == ADD) onAdd(currentState.value)
                        else if (initialMode == DEFAULT && label !== currentState.value) onUpdate(currentState.value)
                        onAdd(currentState.value)
                        keyboardController?.hide()
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = if (isSelected) selectedBorderColor else unselectedBorderColor,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .height(40.dp)
                            .padding(
                                horizontal = 12.dp,
                            ),
                        ) {
                        Row(
                            modifier = Modifier
                                .fillMaxHeight(),
                            verticalAlignment = CenterVertically
                        ) {
                            innerTextField()
                        }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonChipPreviewTrue() {
    PicplzTheme {
        CommonChip(label = "을지로 감성", isSelected = true, isEditable = true)
    }
}

@Preview(showBackground = true)
@Composable
fun CommonChipPreviewFalse() {
    PicplzTheme {
        CommonChip(label = "키치 감성", isSelected = false)
    }
}

@Preview(showBackground = true)
@Composable
fun CommonChipPreviewAdd() {
    PicplzTheme {
        CommonChip(initialMode = ADD)
    }
}

@Preview(showBackground = true)
@Composable
fun CommonChipPreviewEdit() {
    PicplzTheme {
        CommonChip(initialMode = EDIT)
    }
}