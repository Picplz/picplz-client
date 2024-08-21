import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hm.picplz.ui.screen.common.CommonButton
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.viewmodel.SignUpClientViewModel
import com.hm.picplz.ui.screen.sign_up.SignUpClientState
import com.hm.picplz.ui.screen.sign_up_client.SignUpClientIntent
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun SignUpClientNicknameView(
    modifier: Modifier = Modifier,
    currentState: SignUpClientState,
    viewModel: SignUpClientViewModel,
    innerPadding: PaddingValues
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonTopBar(
            text = "닉네임 등록하기",
            onClickBack = { viewModel.handleIntent(SignUpClientIntent.NavigateToPrev) },
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .imePadding()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
            contentAlignment = Alignment.Center
        ) {
            CommonOutlinedTextField(
                value = currentState.nickname,
                onValueChange = { newValue ->
                    viewModel.handleIntent(SignUpClientIntent.SetNickName(newValue))
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "닉네임을 입력하세요",
                isError = false,
                imeAction = ImeAction.Done,
                keyboardActions = {
                    focusManager.clearFocus()
                }
            )
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
                onClick = { viewModel.handleIntent(SignUpClientIntent.ChangeStep(1)) },
                enabled = currentState.nickname.isNotEmpty(),
                containerColor = MainThemeColor.Olive
            )
        }
    }
}