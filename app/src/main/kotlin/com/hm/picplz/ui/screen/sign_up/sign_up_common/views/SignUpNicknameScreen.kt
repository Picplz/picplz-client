package com.hm.picplz.ui.screen.sign_up.sign_up_common.views

import CommonFilledTextField
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.MainActivity
import com.hm.picplz.ui.screen.common.CommonButton
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent.Navigate
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent.ResetSelectedUserType
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpScreen
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpSideEffect
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.viewmodel.SignUpViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpNicknameScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = viewModel(),
    mainNavController: NavController,
    signUpNavController: NavController,
) {
    /** 상태바 스타일 설정 **/
    val view = LocalView.current
    val activity = LocalContext.current as? MainActivity

    LaunchedEffect(Unit) {
        activity?.window?.apply {
            statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(this, view).isAppearanceLightStatusBars = true
        }
        viewModel.handleIntent(ResetSelectedUserType)
    }

    val currentState = viewModel.state.collectAsState().value
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MainThemeColor.White
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .pointerInput(kotlin.Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CommonTopBar(
                text = "닉네임 등록하기",
                onClickBack = { viewModel.handleIntent(SignUpCommonIntent.NavigateToPrev) },
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                        })
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("닉네임을\n")
                            append("선택해주세요")
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    CommonFilledTextField(
                        value = currentState.nickname,
                        onValueChange = { newNickname ->
                            viewModel.handleIntent(SignUpCommonIntent.SetNickname(newNickname))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = "닉네임을 입력하세요",
                        isError = false,
                        imeAction = ImeAction.Done,
                        keyboardActions = {
                            focusManager.clearFocus()
                        },
                        label = "닉네임"
                    )
                    Spacer(
                        modifier = Modifier.height(80.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CommonButton(
                    text = "다음",
                    onClick = { viewModel.handleIntent(Navigate("sign-up-profile")) },
                    enabled = currentState.nickname.isNotEmpty(),
                    containerColor = MainThemeColor.Black
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SignUpSideEffect.NavigateToPrev -> {
                    mainNavController.popBackStack()
                }
                is SignUpSideEffect.Navigate -> {
                    signUpNavController.navigate(sideEffect.destination)
                }
                else -> {}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpNicknameScreenPreview() {
    val mainNavController = rememberNavController()
    val signUpNavController = rememberNavController()

    PicplzTheme {
        SignUpNicknameScreen(
            mainNavController = mainNavController,
            signUpNavController = signUpNavController,
        )
    }
}