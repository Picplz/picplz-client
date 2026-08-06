package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hm.picplz.core.ui.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

private object CommonSuccessModalDefaults {
    val Width = 304.dp
    val Radius = RoundedCornerShape(5.dp)
    val IconSize = 64.dp
    val VerticalPadding = 28.dp
    val HorizontalPadding = 20.dp
    val IconMessageGap = 20.dp
    val ScrimColor = Color(0x66000000)
}

/**
 * 체크 아이콘 + 안내 문구만 있는 완료 모달. 버튼이 없어 스스로 닫히지 않으므로,
 * 호출부에서 일정 시간 뒤 닫거나 다음 화면으로 넘겨야 합니다.
 *
 * 취소/확인 2버튼이 필요하면 [CommonButtonModal]을 쓰세요.
 *
 * @param dismissible false면 바깥 탭·뒤로가기로 닫히지 않습니다.
 */
@Composable
fun CommonSuccessModal(
    message: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    dismissible: Boolean = true,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties =
            DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = dismissible,
                dismissOnClickOutside = dismissible,
            ),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(CommonSuccessModalDefaults.ScrimColor)
                    .clickable(enabled = dismissible, onClick = onDismissRequest),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                modifier =
                    modifier
                        .width(CommonSuccessModalDefaults.Width)
                        .clickable(enabled = false, onClick = {}),
                shape = CommonSuccessModalDefaults.Radius,
                color = MainThemeColor.White,
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = CommonSuccessModalDefaults.VerticalPadding,
                                horizontal = CommonSuccessModalDefaults.HorizontalPadding,
                            ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(R.drawable.check_circle),
                        contentDescription = null,
                        modifier = Modifier.size(CommonSuccessModalDefaults.IconSize),
                    )

                    Box(modifier = Modifier.height(CommonSuccessModalDefaults.IconMessageGap))

                    Text(
                        text = message,
                        style = MainThemeFont.TitleSmall,
                        color = MainThemeColor.Black,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun CommonSuccessModalPreview() {
    PicplzTheme {
        CommonSuccessModal(
            message = "거래 완료 되었습니다",
            onDismissRequest = {},
        )
    }
}
