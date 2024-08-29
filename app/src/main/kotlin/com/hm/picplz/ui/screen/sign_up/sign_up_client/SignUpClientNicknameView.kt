import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hm.picplz.viewmodel.SignUpClientViewModel
import com.hm.picplz.ui.screen.sign_up.SignUpClientState
import com.hm.picplz.ui.screen.sign_up.common.SignUpNicknameView
import com.hm.picplz.ui.screen.sign_up.sign_up_client.SignUpClientIntent
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
        onNicknameFieldChange = { newValue -> viewModel.handleIntent(SignUpClientIntent.SetNickname(newValue)) },
        onClickBackIcon = { viewModel.handleIntent(SignUpClientIntent.NavigateToPrev) },
        onClickBottomButton = { viewModel.handleIntent(SignUpClientIntent.ChangeStep(1)) },
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