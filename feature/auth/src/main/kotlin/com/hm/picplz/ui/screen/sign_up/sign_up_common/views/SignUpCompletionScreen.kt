package com.hm.picplz.ui.screen.sign_up.sign_up_common.views

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.common.mockdata.emptyUserData
import com.hm.picplz.common.model.User
import com.hm.picplz.navigation.model.Login
import com.hm.picplz.navigation.model.Main
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonViewModel
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpSideEffect
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.util.SetStatusBarStyle
import kotlinx.coroutines.flow.collectLatest
import com.hm.picplz.core.ui.R as CoreUiR
import com.hm.picplz.feature.auth.R as AuthR

@Composable
fun SignUpCompletionScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpCommonViewModel = hiltViewModel(),
    mainNavController: NavController,
    userInfo: User,
    onSignupCompleted: () -> Unit,
) {
    SetStatusBarStyle()
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MainThemeColor.White,
    ) { innerPadding ->
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier =
                        modifier
                            .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text =
                            stringResource(
                                AuthR.string.sign_up_completion_greeting,
                                userInfo.nickname.orEmpty(),
                            ),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(
                        modifier =
                            Modifier
                                .height(27.dp),
                    )
                    CompletionProfileImage(profileImageUri = userInfo.profileImageUri)
                    Spacer(
                        modifier =
                            Modifier
                                .height(74.dp),
                    )
                    Text(
                        text = stringResource(AuthR.string.sign_up_completion_message),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
            Box(
                modifier =
                    Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                contentAlignment = Alignment.Center,
            ) {
                CommonBottomButton(
                    text =
                        if (state.isSubmitting) {
                            stringResource(AuthR.string.sign_up_completion_logging_in)
                        } else {
                            stringResource(AuthR.string.sign_up_completion_start)
                        },
                    enabled = !state.isSubmitting,
                    onClick = {
                        viewModel.handleIntent(SignUpCommonIntent.CompleteSignup(context))
                    },
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

                is SignUpSideEffect.SignupCompleted -> {
                    onSignupCompleted()
                    mainNavController.navigate(signupCompletionDestination()) {
                        popUpTo(Login) { inclusive = true }
                        launchSingleTop = true
                    }
                }

                is SignUpSideEffect.ShowToast -> {
                    Toast.makeText(context, sideEffect.messageResId, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }
}

fun signupCompletionDestination(): Any = Main

@Composable
private fun CompletionProfileImage(profileImageUri: String?) {
    if (profileImageUri != null) {
        Image(
            painter = rememberAsyncImagePainter(model = profileImageUri),
            contentDescription =
                stringResource(
                    AuthR.string.sign_up_completion_profile_image_content_description,
                ),
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(160.dp)
                    .clip(CircleShape),
        )
    } else {
        Box(
            modifier =
                Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(MainThemeColor.Gray2),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = CoreUiR.drawable.signup_completion_default_profile),
                contentDescription =
                    stringResource(
                        AuthR.string.sign_up_completion_default_profile_image_content_description,
                    ),
                modifier = Modifier.size(width = 102.dp, height = 116.dp),
            )
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
            userInfo = emptyUserData,
            onSignupCompleted = {},
        )
    }
}
