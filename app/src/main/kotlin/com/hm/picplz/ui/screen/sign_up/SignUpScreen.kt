package com.hm.picplz.ui.screen.sign_up

import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.hm.picplz.viewmodel.SignUpViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.MainActivity
import com.hm.picplz.ui.screen.login.LoginSideEffect
import com.hm.picplz.ui.theme.PicplzTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = viewModel(),
    navController: NavController,
) {
    /** 상태바 스타일 설정 **/
    val view = LocalView.current
    val activity = LocalContext.current as? MainActivity

    DisposableEffect(Unit) {
        activity?.window?.apply {
            statusBarColor = Color.Gray.toArgb()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                WindowCompat.getInsetsController(this, view).isAppearanceLightStatusBars = false
            }
        }

        onDispose {
            activity?.window?.apply {
                statusBarColor = Color.White.toArgb()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    WindowCompat.getInsetsController(this, view).isAppearanceLightStatusBars = true
                }
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    IconButton(
                        onClick = {
                            viewModel.handleIntent(SignUpIntent.NavigateToPrev)
                        },
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ){
                        Text("회원 타입 선텍")
                    }
                }
            }
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
                    Text(text = "선택해주세요")
                    Row(
                        modifier = Modifier,
                    ) {
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .padding(end = 8.dp)
                        ) {
                            Text("고객")
                        }
                        Button(
                            onClick = { /*TODO*/ }
                        ) {
                            Text("금손")
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue
                    )

                ) {
                 Text("다음")
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SignUpSideEffect.NavigateToSetting -> {}
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