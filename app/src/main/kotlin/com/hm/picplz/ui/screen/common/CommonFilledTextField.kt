import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hm.picplz.data.model.NicknameFieldError
import com.hm.picplz.ui.theme.MainThemeColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonFilledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    errors: List<NicknameFieldError> = emptyList(),
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: () -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MainThemeColor.Olive,
        unfocusedBorderColor = MainThemeColor.Gray,
        focusedLabelColor = MainThemeColor.Gray,
        unfocusedLabelColor = MainThemeColor.Gray,
        focusedPlaceholderColor = Color.Gray,
        unfocusedPlaceholderColor = Color.Gray
    )

    val isError = errors.isNotEmpty()

    Column(modifier = modifier.fillMaxWidth()) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MainThemeColor.Gray,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            interactionSource = interactionSource,
            enabled = enabled,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onAny = {
                    keyboardActions()
                }
            )
        ) {
            TextFieldDefaults.DecorationBox(
                value = value,
                visualTransformation = visualTransformation,
                innerTextField = it,
                singleLine = singleLine,
                enabled = enabled,
                interactionSource = interactionSource,
                placeholder = {
                    if (placeholder.isNotEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                },
                supportingText = {
                    if (isError) {
                        Text(
                            text = errors.first().message,
                            fontSize = 12.sp,
                            color = Color.Red,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(18.dp))
                    }
                },
                contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                    start = 4.dp, bottom = 0.dp
                ),
                colors = colors
            )
        }
    }
}