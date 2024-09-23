package com.hm.picplz.ui.screen.sign_up.sign_up_common.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hm.picplz.R
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.viewmodel.SignUpCommonViewModel

@Composable
fun NicknameTooltipDialog (
    onDismissRequest: () -> Unit
){
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MainThemeColor.White,
            tonalElevation = 8.dp,
            modifier = Modifier
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
            ){
                Text(
                    modifier = Modifier
                        .padding(10.dp),
                    text = buildAnnotatedString {
                        append("∙ 한글, 영문, 숫자 입력 가능 (2~15자)\n")
                        append("∙ 중복 닉네임은 불가\n")
                        append("∙ 이모티콘, 특수문자 사용이 불가\n")
                        append("∙ 닉네임의 처음과 마지막 부분 공백 사용 불가")
                    },                        style = TextStyle(
                        fontSize = 12.sp
                    ),
                )
                Image(
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { onDismissRequest() },
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "툴팁 종료"
                )
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun TooltipDialogPreview() {
    val viewModel : SignUpCommonViewModel = viewModel()
    PicplzTheme {
        NicknameTooltipDialog (
            onDismissRequest = { viewModel.handleIntent(SignUpCommonIntent.ToggleValidateDialog) }
        )
    }
}