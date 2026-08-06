package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.pretendardTypography

private object CommonBottomButtonDefaults {
    val VerticalPadding = 14.dp
    val HorizontalPadding = 20.dp
    val CornerRadius = 5.dp
}

/**
 * @param enabled 버튼의 활성 여부. false면 비활성 색상으로 그려집니다.
 * @param clickableWhenDisabled true면 [enabled]가 false여도 클릭을 받습니다(외형만 비활성).
 *  입력이 부족할 때 버튼을 눌러 안내 토스트를 띄워야 하는 화면에서 사용합니다.
 *  Material3 `Button`은 `enabled = false`면 클릭 자체를 막기 때문에 별도 분기가 필요합니다.
 */
@Composable
fun CommonBottomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    clickableWhenDisabled: Boolean = false,
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors =
            ButtonDefaults.buttonColors(
                // enabled = false여도 클릭을 받아야 하면 Material에는 활성으로 넘기고 색만 비활성으로 그립니다.
                containerColor = if (enabled) MainThemeColor.Black else MainThemeColor.Gray3,
                contentColor = if (enabled) MainThemeColor.White else MainThemeColor.Gray2,
                disabledContainerColor = MainThemeColor.Gray3,
                disabledContentColor = MainThemeColor.Gray2,
            ),
        shape = RoundedCornerShape(CommonBottomButtonDefaults.CornerRadius),
        contentPadding =
            PaddingValues(
                vertical = CommonBottomButtonDefaults.VerticalPadding,
                horizontal = CommonBottomButtonDefaults.HorizontalPadding,
            ),
        enabled = enabled || clickableWhenDisabled,
    ) {
        Text(
            text = text,
            style = pretendardTypography.labelLarge,
        )
    }
}

@Composable
fun CommonBottomOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MainThemeColor.White,
                contentColor = MainThemeColor.Black,
            ),
        shape = RoundedCornerShape(CommonBottomButtonDefaults.CornerRadius),
        border = BorderStroke(1.dp, MainThemeColor.Gray3),
        contentPadding =
            PaddingValues(
                vertical = CommonBottomButtonDefaults.VerticalPadding,
                horizontal = CommonBottomButtonDefaults.HorizontalPadding,
            ),
        enabled = enabled,
    ) {
        Text(
            text = text,
            style = pretendardTypography.labelLarge,
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun CommonBottomButtonEnabledPreview() {
    PicplzTheme {
        CommonBottomButton(
            text = "다음",
            onClick = {},
            enabled = true,
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun CommonBottomButtonDisabledPreview() {
    PicplzTheme {
        CommonBottomButton(
            text = "다음",
            onClick = {},
            enabled = false,
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun CommonBottomButtonDisabledButClickablePreview() {
    PicplzTheme {
        CommonBottomButton(
            text = "리뷰 등록",
            onClick = {},
            enabled = false,
            clickableWhenDisabled = true,
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun CommonBottomOutlinedButtonPreview() {
    PicplzTheme {
        CommonBottomOutlinedButton(
            text = "다음",
            onClick = {},
            enabled = true,
        )
    }
}
