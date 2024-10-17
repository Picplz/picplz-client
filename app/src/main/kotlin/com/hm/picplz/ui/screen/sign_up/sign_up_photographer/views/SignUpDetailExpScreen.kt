package com.hm.picplz.ui.screen.sign_up.sign_up_photographer.views

import CommonChip
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.viewmodel.SignUpPhotographerViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.MainActivity
import com.hm.picplz.data.model.ChipMode
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.*
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerSideEffect
import com.hm.picplz.ui.theme.MainThemeColor
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignUpDetailExpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpPhotographerViewModel = viewModel(),
    signUpPhotographerNavController: NavController
){
    val view = LocalView.current
    val activity = LocalContext.current as? MainActivity

    LaunchedEffect(Unit){
        activity?.window?.apply {
            statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(this, view).isAppearanceLightStatusBars = true
        }
    }
    
    val currentState = viewModel.state.collectAsState().value

    Scaffold (
        modifier = Modifier
            .fillMaxSize(),
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
                    .imePadding(),
                ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Spacer(
                        modifier = Modifier
                            .height(80.dp)
                    )
                    Text(
                        text = "해당되는 경험을 골라주세요.",
                        style = MaterialTheme.typography.titleMedium
                    )
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
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        currentState.experienceChipList.map { chip ->
                            CommonChip(
                                id = chip.id,
                                label = chip.label,
                                initialMode = ChipMode.DEFAULT,
                                onClickDefaultMode = {
                                    viewModel.handleIntent(SetPhotographyExperience(chip.id))
                                },
                                isSelected = currentState.selectedPhotographyExperienceId === chip.id
                            )
                        }
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
                    onClick = { viewModel.handleIntent(Navigate("sign-up-photography-vibe"))},
                    enabled = currentState.selectedPhotographyExperienceId != null,
                    containerColor = MainThemeColor.Black
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SignUpPhotographerSideEffect.NavigateToPrev -> {
                    signUpPhotographerNavController.popBackStack()
                }
                is SignUpPhotographerSideEffect.Navigate -> {
                    signUpPhotographerNavController.navigate(sideEffect.destination)
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun SinUpDetailExpScreenPreview() {
    PicplzTheme {
        val signUpPhotographerNavController = rememberNavController()
        SignUpDetailExpScreen(
            signUpPhotographerNavController = signUpPhotographerNavController
        )
    }
}