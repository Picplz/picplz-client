import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.hm.picplz.R
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun CommonTextDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    dialogText: String,
    dialogTextSize: Int = 12,
    textPadding: Dp = 10.dp,
    dialogShape: RoundedCornerShape = RoundedCornerShape(16.dp),
    dialogElevation: Dp = 8.dp,
    backgroundColor: Color = MainThemeColor.White,
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Surface(
            shape = dialogShape,
            color = backgroundColor,
            tonalElevation = dialogElevation,
            modifier = modifier
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    modifier = Modifier
                        .padding(textPadding),
                    text = buildAnnotatedString {
                        append(dialogText)
                    },
                    style = TextStyle(
                        fontSize = dialogTextSize.sp
                    ),
                )
                Image(
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { onDismissRequest() },
                    painter = painterResource(id =  R.drawable.close),
                    contentDescription = "Close Dialog"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonTextDialogPreview() {
    CommonTextDialog(
        onDismissRequest = {},
        dialogText = "∙ 한글, 영문, 숫자 입력 가능 (2~15자)\n∙ 중복 닉네임은 불가\n∙ 이모티콘, 특수문자 사용 불가\n∙ 닉네임의 처음과 마지막 부분 공백 사용 불가",
        dialogTextSize = 12,
    )
}
