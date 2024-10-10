package com.hm.picplz.ui.screen.sign_up.sign_up_photographer.views

import CommonSelectButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.hm.picplz.viewmodel.SignUpPhotographerViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.MainActivity
import com.hm.picplz.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.NavigateToPrev
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.SetPhotographyExperience
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerSideEffect
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import kotlinx.coroutines.flow.collectLatest


@Composable
fun SignUpExperience(
    modifier: Modifier = Modifier,
    viewModel: SignUpPhotographerViewModel = viewModel(),
    mainNavController: NavController,
    signUpPhotographerNavController: NavController,
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

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        containerColor = MainThemeColor.White
    ) { innerPadding ->
        Column (
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CommonTopBar(
                text = "경력 선택",
                onClickBack = {viewModel.handleIntent(NavigateToPrev)}
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 15.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Spacer(
                        modifier = Modifier
                            .height(80.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "사진 촬영 경험이 있으신가요?",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Image(
                            painter = painterResource(id = R.drawable.info),
                            contentDescription = "info icon",
                            modifier = Modifier
                                .height(24.dp)
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(15.dp)
                    )
                    Text(
                        text = "픽플즈는 사진 경력이 없는 금손님도 환영해요!",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(
                        modifier = Modifier
                            .height(30.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CommonSelectButton(
                            text = "있어요",
                            isSelected = currentState.hasPhotographyExperience == true,
                            onClick = {viewModel.handleIntent(SetPhotographyExperience(hasExperience = true))},
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp)
                        )
                        CommonSelectButton(
                            text = "없어요",
                            isSelected = currentState.hasPhotographyExperience == false,
                            onClick = {viewModel.handleIntent(SetPhotographyExperience(hasExperience = false))},
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp)
                        )
                    }
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
                    text = "다음",
                    onClick = {},
                    enabled = currentState.hasPhotographyExperience != null,
                    containerColor = MainThemeColor.Black
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SignUpPhotographerSideEffect.NavigateToPrev -> {
                    mainNavController.popBackStack()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpExprienceScreenPreview() {
    PicplzTheme {
        val mainNavController = rememberNavController()
        val signUpPhotographerNavController = rememberNavController()

        SignUpExperience(
            mainNavController = mainNavController,
            signUpPhotographerNavController = signUpPhotographerNavController,
        )
    }
}