package com.hm.picplz.ui.screen.login

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.core.ui.R
import com.hm.picplz.navigation.model.Dev
import com.hm.picplz.navigation.model.SignUpIntro
import com.hm.picplz.ui.screen.common.CommonHorizontalPager
import com.hm.picplz.ui.screen.common.KakaoLoginButton
import com.hm.picplz.ui.theme.MainFontFamily
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import kotlinx.coroutines.flow.collectLatest

data class LoginIntroPageData(
    val text: String,
    @DrawableRes val imageRes: Int,
    val isLast: Boolean = false,
)

object LoginIntroPageConfig {
    val PAGES =
        listOf(
            LoginIntroPageData("내 인생샷 찍어줄\n작가님과 위치기반 매칭!", R.drawable.intro1),
            LoginIntroPageData("작가와 고객 모두 걱정 없는\n정찰제, 안전 결제 시스템!", R.drawable.intro2),
            LoginIntroPageData("나의 인생 프사,\n이젠 픽플즈가 함께", R.drawable.intro3, isLast = true),
        )

    const val IMAGE_HEIGHT_FACTOR = 0.6f
}

private const val DEV_ENTRY_TAP_COUNT = 5

@Composable
fun LoginIntroScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginCompleted: () -> Unit = {},
    enableDevEntry: Boolean = false,
) {
    val context = LocalContext.current
    var devEntryTapCount by remember { mutableStateOf(0) }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val imageHeight = screenHeight * LoginIntroPageConfig.IMAGE_HEIGHT_FACTOR
    val onDevEntryTap = {
        if (enableDevEntry) {
            devEntryTapCount += 1
            if (devEntryTapCount >= DEV_ENTRY_TAP_COUNT) {
                devEntryTapCount = 0
                navController.navigate(Dev)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MainThemeColor.White,
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
        ) {
            CommonHorizontalPager(
                items = LoginIntroPageConfig.PAGES,
                pageCount = LoginIntroPageConfig.PAGES.size,
                showIndicator = true,
                itemContent = { page ->
                    LoginIntroPage(
                        page = page,
                        imageHeight = imageHeight,
                        onLoginClick = {
                            viewModel.handleIntent(LoginIntent.StartKakaoLogin(context))
                        },
                        onLogoutClick = {
                            viewModel.handleIntent(LoginIntent.UnlinkKakao)
                        },
                        onDevEntryTap = onDevEntryTap,
                        enableDevEntry = enableDevEntry,
                    )
                },
                isIndicatorPositionAbsolute = true,
                indicatorTopSpacing = imageHeight + 92.dp + 34.dp,
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is LoginSideEffect.LoginFailed -> {
                    Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                }

                LoginSideEffect.LoginSuccess -> {
                    Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()
                    onLoginCompleted()
                }

                is LoginSideEffect.NavigateToSignUp -> {
                    navController.navigate(SignUpIntro(profileImageUri = sideEffect.profileImageUrl))
                }

                LoginSideEffect.UnlinkSuccess -> {
                    Toast.makeText(context, "연결 끊기 성공", Toast.LENGTH_SHORT).show()
                }

                is LoginSideEffect.UnlinkFailed -> {
                    Toast.makeText(context, "연결 끊기 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
fun LoginIntroPage(
    page: LoginIntroPageData,
    imageHeight: Dp,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDevEntryTap: () -> Unit,
    enableDevEntry: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(imageHeight),
        ) {
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = "Background Image",
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop,
            )
        }

        Spacer(modifier = Modifier.height(34.dp))

        Text(
            text = page.text,
            style = MainFontFamily.titleLarge,
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .clickable(
                        enabled = enableDevEntry,
                        indication = null,
                        interactionSource = null,
                        onClick = onDevEntryTap,
                    )
                    .wrapContentHeight(Alignment.CenterVertically),
        )

        if (page.isLast) {
            Spacer(modifier = Modifier.height(66.dp))

            Box(modifier = Modifier.padding(horizontal = 15.dp)) {
                KakaoLoginButton(
                    text = "카카오로 계속하기",
                    onClick = onLoginClick,
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier.padding(horizontal = 15.dp)) {
                KakaoLoginButton(
                    text = "[DEV] 카카오 연동 해제",
                    onClick = onLogoutClick,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginIntroScreenPreview() {
    val navController = rememberNavController()
    PicplzTheme {
        LoginIntroScreen(navController = navController)
    }
}
