import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hm.picplz.ui.screen.common.CommonButton
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.viewmodel.SignUpClientViewModel
import com.hm.picplz.ui.screen.sign_up.SignUpClientState
import com.hm.picplz.ui.screen.sign_up.common.SignUpNicknameView
import com.hm.picplz.ui.screen.sign_up_client.SignUpClientIntent
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun SignUpClientNicknameView(
    modifier: Modifier = Modifier,
    currentState: SignUpClientState,
    viewModel: SignUpClientViewModel,
    innerPadding: PaddingValues
) {
    SignUpNicknameView(
        modifier = modifier,
        currentNickname = currentState.nickname,
        onNicknameChange = { newValue -> viewModel.handleIntent(SignUpClientIntent.SetNickName(newValue)) },
        onNavigateToPrev = { viewModel.handleIntent(SignUpClientIntent.NavigateToPrev) },
        onNextStep = { viewModel.handleIntent(SignUpClientIntent.ChangeStep(1)) },
        innerPadding = innerPadding
    )
}

@Preview(showBackground = true)
@Composable
fun SignupClientNicknamePreview() {
    val viewModel: SignUpClientViewModel = viewModel()
    val currentState = viewModel.state.collectAsState().value

    PicplzTheme {
        SignUpClientNicknameView(
            currentState = currentState,
            viewModel = viewModel,
            innerPadding = PaddingValues() ,
        )
    }
}