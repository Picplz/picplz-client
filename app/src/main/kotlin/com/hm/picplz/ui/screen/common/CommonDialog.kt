import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.hm.picplz.R
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun CommonDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    dialogShape: RoundedCornerShape = RoundedCornerShape(5.dp),
    dialogWidth: Dp = 304.dp,
    dialogElevation: Dp = 8.dp,
    backgroundColor: Color = MainThemeColor.White,
    hasQuit: Boolean = true,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Surface(
            modifier = modifier
                .width(dialogWidth),
            shape = dialogShape,
            color = backgroundColor,
            tonalElevation = dialogElevation,
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Row {
                    Box(
                        modifier = Modifier
                            .width(if(hasQuit) (dialogWidth - 36.dp) else (dialogWidth - 18.dp))
                            .padding(
                                start = 18.dp,
                                top = 18.dp,
                                bottom = 18.dp,
                                end = if(hasQuit) 0.dp else 18.dp
                            ),
                    ) {
                        content()
                    }
                    if (hasQuit) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { onDismissRequest() },
                                painter = painterResource(id = R.drawable.close),
                                contentDescription = "Close Dialog"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonDialogPreview() {
    CommonDialog(
        onDismissRequest = {},
    ) {
        Column(
        ) {
            Text(text = "∙ 한글, 영문, 숫자 입력 가능 (2~15자)")
            Text(text = "∙ 중복 닉네임은 불가")
            Text(text = "∙ 이모티콘, 특수문자 사용 불가")
            Text(text = "∙ 닉네임의 처음과 마지막 부분 공백 사용 불가")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonDialogNoQuitPreview() {
    CommonDialog(
        onDismissRequest = {},
        hasQuit = false,
    ) {
        Column(
        ) {
            Text(text = "∙ 한글, 영문, 숫자 입력 가능 (2~15자)")
            Text(text = "∙ 중복 닉네임은 불가")
            Text(text = "∙ 이모티콘, 특수문자 사용 불가")
            Text(text = "∙ 닉네임의 처음과 마지막 부분 공백 사용 불가")
        }
    }
}