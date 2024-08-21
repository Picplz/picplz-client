package com.hm.picplz.ui.screen.sign_up

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
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
                    .fillMaxWidth(),
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
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
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
                        Button(
                            onClick = {
                                viewModel.handleIntent(ClickUserTypeButton(UserType.User))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp)
                                .height(100.dp)
                                .shadow(
                                    elevation = 5.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    ambientColor = Color.Gray,
                                    spotColor = Color.Gray)
                                .background(
                                    Color.White,
                                    shape = RoundedCornerShape(16.dp)
                                ).border(
                                    width = 2.dp,
                                    color = if (selectedUserType == UserType.User) {
                                        MainThemeColor.Olive
                                    } else {
                                        Color.Transparent
                                    },
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = MainThemeColor.Black
                            ),
                            shape = RoundedCornerShape(16.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.logo),
                                    contentDescription = "",
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.width(20.dp))
                                Text(
                                    text = "고객",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MainThemeColor.Black
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                viewModel.handleIntent(ClickUserTypeButton(UserType.Photographer))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp)
                                .height(100.dp)
                                .shadow(
                                    elevation = 5.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    ambientColor = Color.Gray,
                                    spotColor = Color.Gray)
                                .background(
                                    Color.White,
                                    shape = RoundedCornerShape(16.dp)
                                ).border(
                                    width = 2.dp,
                                    color = if (selectedUserType == UserType.Photographer) {
                                        MainThemeColor.Olive
                                    } else {
                                        Color.Transparent
                                    },
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = MainThemeColor.Black
                            ),
                            shape = RoundedCornerShape(16.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.logo),
                                    contentDescription = "",
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.width(20.dp))
                                Text(
                                    text = "금손",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MainThemeColor.Black
                                    )
                                )
                            }
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
            contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        viewModel.handleIntent(NavigateToSelected)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MainThemeColor.Olive
                    ),
                    shape = RoundedCornerShape(10.dp),
                    enabled = selectedUserType != null
                ) {
                    Text(
                        text = "다음",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium                        )
                    )
                }
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