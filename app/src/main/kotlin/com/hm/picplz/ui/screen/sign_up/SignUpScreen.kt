package com.hm.picplz.ui.screen.sign_up

import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import com.hm.picplz.viewmodel.SignUpViewModel
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
import com.hm.picplz.ui.theme.PicplzTheme
import kotlinx.coroutines.flow.collectLatest
import com.hm.picplz.ui.screen.sign_up.SignUpIntent.*
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = viewModel(),
    navController: NavController,
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
    val selectedUserType = currentState.selectedUserType

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MainThemeColor.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
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
                            append("회원 타입을\n")
                            append("선택해주세요")
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(
                        modifier = Modifier
                            .height(30.dp)
                    )
                    Column(
                        modifier = Modifier,
                    ) {
                        CommonSelectImageButton(
                            text = "고객",
                            isSelected = selectedUserType == UserType.User,
                            onClick = { viewModel.handleIntent(ClickUserTypeButton(UserType.User)) },
                            iconResId = R.drawable.logo
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        CommonSelectImageButton(
                            text = "금손",
                            isSelected = selectedUserType == UserType.Photographer,
                            onClick = { viewModel.handleIntent(ClickUserTypeButton(UserType.Photographer)) },
                            iconResId = R.drawable.logo
                        )
                    }
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
                    enabled = selectedUserType != null,
                    containerColor = MainThemeColor.Black
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SignUpSideEffect.NavigateToSelected -> {
                    val bundle = bundleOf("userInfo" to sideEffect.user)
                    navController.navigate(sideEffect.destination.route, bundle)
                }
                is SignUpSideEffect.NavigateToPrev -> {
                    navController.popBackStack()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    val navController = rememberNavController()
    PicplzTheme {
        SignUpScreen(navController = navController)
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