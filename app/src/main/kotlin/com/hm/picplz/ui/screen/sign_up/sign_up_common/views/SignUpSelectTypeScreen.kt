package com.hm.picplz.ui.screen.sign_up.sign_up_common.views

import android.os.Bundle
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.MainActivity
import com.hm.picplz.R
import com.hm.picplz.data.model.UserType
import com.hm.picplz.ui.screen.common.CommonButton
import com.hm.picplz.ui.screen.common.CommonSelectImageButton
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent.*
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpSideEffect
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.viewmodel.SignUpCommonViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpSelectTypeScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpCommonViewModel = viewModel(),
    mainNavController: NavController,
    signUpCommonNavController: NavController,
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
                text = "회원 타입 선택",
                onClickBack = { viewModel.handleIntent(NavigateToPrev) },
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("가입하실 회원 타입을\n")
                            append("선택해주세요.")
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(
                        modifier = Modifier
                            .height(30.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(horizontal = 10.dp)
                        ,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        CommonSelectImageButton(
                            text = "찍사",
                            selectionState =currentState.photographerSelectionState,
                            onClick = { viewModel.handleIntent(ClickUserTypeButton(UserType.Photographer)) },
                            selectedIconResId = R.drawable.user_selected,
                            deSelectedIconResId = R.drawable.user_deselected,
                        )
                        CommonSelectImageButton(
                            text = "고객",
                            selectionState =currentState.userSelectionState,
                            onClick = { viewModel.handleIntent(ClickUserTypeButton(UserType.User)) },
                            selectedIconResId = R.drawable.photographer_selected,
                            deSelectedIconResId = R.drawable.photographer_deselected,
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(170.dp)
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
                    onClick = { viewModel.handleIntent(NavigateToSelected) },
                    enabled = currentState.selectedUserType != null,
                    containerColor = MainThemeColor.Black
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SignUpSideEffect.SelectUserTypeScreenSideEffect.NavigateToSelected -> {
                    val bundle = bundleOf("userInfo" to sideEffect.user)
                    mainNavController.navigate(sideEffect.destination.route, bundle)
                }
                is SignUpSideEffect.NavigateToPrev -> {
                    signUpCommonNavController.popBackStack()
                }
                else -> {}
            }
        }
    }
}

fun NavController.navigate(
    route: String,
    args: Bundle,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    val nodeId = graph.findNode(route)?.id
    if (nodeId != null) {
        navigate(nodeId, args, navOptions, navigatorExtras)
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpSelectTypeScreenPreview() {
    PicplzTheme {
        val mainNavController = rememberNavController()
        val signUpCommonNavController = rememberNavController()

        SignUpSelectTypeScreen(
            mainNavController = mainNavController,
            signUpCommonNavController = signUpCommonNavController
        )
    }
}