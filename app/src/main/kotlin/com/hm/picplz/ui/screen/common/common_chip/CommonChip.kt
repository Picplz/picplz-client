import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hm.picplz.R
import com.hm.picplz.data.model.ChipMode
import com.hm.picplz.data.model.ChipMode.*
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.screen.common.common_chip.CommonChipIntent.*
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.Pretendard
import com.hm.picplz.viewmodel.common.CommonChipViewModel
import java.util.UUID
import androidx.compose.ui.text.input.TextFieldValue

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
    onEndEdit: () -> Unit = {},
) {

    val currentState = viewModel.state.collectAsState().value
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(isEditing) {
        if (isEditing) {
            viewModel.handleIntent(SetChipMode(EDIT))
            viewModel.handleIntent(
                SetValue(
                    TextFieldValue(
                        text = currentState.value.text,
                        selection = TextRange(currentState.value.text.length)
                    )
                )
            )
        } else {
            if (currentState.value.text.isNotEmpty()) {
                if (initialMode == ADD) {
                    onAdd(currentState.value.text)
                }
                else if (initialMode == DEFAULT && label != currentState.value.text) {
                    onUpdate(currentState.value.text)
                }
            }
            if (initialMode == ADD) {
                viewModel.handleIntent(SetValue(TextFieldValue("")))
            } else if (initialMode == DEFAULT) {
                viewModel.handleIntent(SetValue(TextFieldValue(label)))
            }
            viewModel.handleIntent(SetChipMode(initialMode))
        }
    }

    LaunchedEffect(currentState.chipMode) {
        if (currentState.chipMode == EDIT) {
            focusRequester.requestFocus()
        } else if (currentState.chipMode == ADD) {
            viewModel.handleIntent(SetTextFieldWidth(96.dp))
        }
    }

    LaunchedEffect(label) {
        viewModel.handleIntent(SetValue(TextFieldValue(label)))
        viewModel.handleIntent(SetChipMode(initialMode))
    }

    val density = LocalDensity.current

    LaunchedEffect(currentState.value.text) {
        if (currentState.calculatedWidth > currentState.textFieldWidth) {
            viewModel.handleIntent(SetTextFieldWidth(currentState.calculatedWidth))
        }
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
                        .fillMaxHeight()
                        .padding(horizontal = 12.dp)
                        .onGloballyPositioned { layoutCoordinates ->
                            val widthInPx = layoutCoordinates.size.width
                            val widthInDp = with(density) { widthInPx.toDp() }
                            viewModel.handleIntent(SetCalculatedWidth(widthInDp))
                        },
                    verticalAlignment = CenterVertically
                ) {
                    Text(
                        text = label,
                        style = TextStyle(
                            fontFamily = Pretendard,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp,
                        ),
                        color = if (isSelected) selectedTextColor else unselectedTextColor,
                    )
                    if (isEditable) {
                        Spacer(modifier = Modifier.width(3.dp))
                        IconButton(
                            onClick = { onEdit() },
                            modifier = Modifier.size(12.dp)
                        ) {
                            Image(
                                painter = painterResource(if (isSelected) R.drawable.edit else R.drawable.edit_grey4),
                                contentDescription = "edit",
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }
        }
        ADD -> {
            Row(
                modifier = Modifier
                    .clickable { onEdit() }
                    .height(40.dp)
                    .border(
                        width = 1.dp,
                        color = MainThemeColor.Gray3,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .background(color = MainThemeColor.Gray1)
            ) {
                Row(
                    modifier = Modifier.fillMaxHeight(),
                    verticalAlignment = CenterVertically
                ) {
                    Text(
                        text = "+직접 적어주세요",
                        modifier = Modifier.padding(horizontal = 12.dp),
                        style = TextStyle(
                            fontFamily = Pretendard,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                        ),
                        color = MainThemeColor.Gray3,
                    )
                }
            }
        }
        EDIT -> {
            BasicTextField(
                modifier = Modifier
                    .focusRequester(focusRequester),
                value = currentState.value,
                onValueChange = { newValue ->
                    val textWidthInDp = with(density) {
                        (newValue.text.length * 20).toDp()
                    }

                    if (textWidthInDp > currentState.textFieldWidth) {
                        viewModel.handleIntent(SetTextFieldWidth(textWidthInDp))
                    }

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
                        if (initialMode == ADD) onAdd(currentState.value.text)
                        else if (initialMode == DEFAULT && label !== currentState.value.text) onUpdate(currentState.value.text)
                        onEndEdit()
                        if (initialMode == ADD) {
                            viewModel.handleIntent(SetValue(TextFieldValue("")))
                        } else if (initialMode == DEFAULT) {
                            viewModel.handleIntent(SetValue(TextFieldValue(label)))
                        }
                        viewModel.handleIntent(SetChipMode(initialMode))
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
                            .width(currentState.textFieldWidth + 24.dp)
                            .padding(horizontal = 12.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxHeight(),
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
        CommonChip(label = "을지로 감성", isSelected = true, isEditable = false)
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

@Preview(showBackground = true)
@Composable
fun CommonChipPreviewFalseEditable() {
    PicplzTheme {
        CommonChip(label = "공주 감성", isSelected = false, isEditable = true)
    }
}
