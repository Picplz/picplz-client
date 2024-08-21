import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isError: Boolean = false,
    supportingText: String? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MainThemeColor.Olive,
        unfocusedBorderColor = MainThemeColor.Gray,
        focusedLabelColor = MainThemeColor.Olive,
        unfocusedLabelColor = MainThemeColor.Gray
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth(),
        singleLine = true,
        interactionSource = interactionSource,
        enabled = true
    ) {
        OutlinedTextFieldDefaults.DecorationBox(
            value = value,
            visualTransformation = VisualTransformation.None,
            innerTextField = it,
            singleLine = true,
            enabled = true,
            interactionSource = interactionSource,
            placeholder = { if (placeholder.isNotEmpty()) Text(placeholder) },
            supportingText = { if (supportingText != null) Text(supportingText) },
            contentPadding = OutlinedTextFieldDefaults.contentPadding(
                start = 16.dp,
                top = 8.dp,
                end = 16.dp,
                bottom = 8.dp
            ),
            colors = colors,
            container = {
                OutlinedTextFieldDefaults.ContainerBox(
                    enabled = true,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = colors,
                    shape = RectangleShape,
                    unfocusedBorderThickness = 1.dp,
                    focusedBorderThickness = 2.dp
                )
            }
        )
    }
}