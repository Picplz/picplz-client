package com.hm.picplz.ui.screen.sign_up.sign_up_common.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.MainActivity
import com.hm.picplz.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent.NavigateToPrev
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpSideEffect
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.viewmodel.SignUpCommonViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpCompletionScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpCommonViewModel = viewModel(),
    mainNavController: NavController,
) {
    /** 상태바 스타일 설정 **/
    val view = LocalView.current
    val activity = LocalContext.current as? MainActivity

    LaunchedEffect(Unit) {
        activity?.window?.apply {
            statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(this, view).isAppearanceLightStatusBars = true
        }
    }

    val currentState = viewModel.state.collectAsState().value

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MainThemeColor.White
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CommonTopBar(
                text = "가입 완료",
                onClickBack = { viewModel.handleIntent(NavigateToPrev) }
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column (
                    modifier = modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val painter = if (currentState.profileImageUri != null) {
                            rememberAsyncImagePainter(model = currentState.profileImageUri)
                        } else {
                            painterResource(id = R.drawable.default_profile_large)
                        }
                        Box(
                            modifier = Modifier
                                .offset(y = 30.dp)
                        ) {
                            Image(
                                painter = painter,
                                contentDescription = "프로필 이미지",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(194.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray)
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.spicky1),
                            contentDescription = "프로필 이미지",
                            modifier = Modifier
                                .offset(x= (-80).dp, y = (-100).dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.spicky2),
                            contentDescription = "프로필 이미지",
                            modifier = Modifier
                                .offset(x= (-25).dp, y = (-90).dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.spicky3),
                            contentDescription = "프로필 이미지",
                            modifier = Modifier
                                .offset(x= 112.dp, y = 112.dp)
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                    )
                    Text(
                        text = buildAnnotatedString {
                                append("${currentState.nickname}님,\n")
                                append("가입이 완료되었습니다!")
                        },
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "픽플즈와 인생샷을 건져보세요.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(200.dp))
                }

            }
            Box(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                contentAlignment = Alignment.Center
            ) {
                CommonBottomButton(
                    text = "픽플즈 시작하기",
                    onClick = { },
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
                    mainNavController.navigate(sideEffect.destination)
                }
                else -> {}
            }
        }
    }
}

@Preview
@Composable
fun SignUpCompletionScreenPreview() {
    val mainNavController = rememberNavController()

    PicplzTheme {
        SignUpCompletionScreen(
            mainNavController = mainNavController,
        )
    }
}