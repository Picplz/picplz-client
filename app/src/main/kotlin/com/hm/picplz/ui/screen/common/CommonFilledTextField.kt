import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hm.picplz.data.model.NicknameFieldError
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

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
    enabled: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(5.dp)
) {
    val interactionSource = remember { MutableInteractionSource() }
    val colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
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
                .fillMaxWidth()
                .height(50.dp)
                .clip(shape)
                .background(MainThemeColor.Gray2)
                .border(
                    width = 0.dp,
                    color = Color.Transparent,
                    shape = shape
                ),
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
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MainThemeColor.Black
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
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                },
                colors = colors
            )
        }

        if (isError) {
            Text(
                text = "* ${errors.first().message}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 12.sp * 1.4,
                color = Color(0xFFFF0000),
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(21.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CommonFilledTextFieldPreview() {
    PicplzTheme {
        CommonFilledTextField(value = "value", onValueChange = {})
    }
}

