import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.PicplzTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.ui.screen.login.LoginIntent
import com.hm.picplz.ui.screen.login.LoginSideEffect
import com.hm.picplz.ui.screen.login.LoginViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    viewModel.handleIntent(LoginIntent.NavigateToKaKao)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFEB3B),
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(text = "카카오로 계속하기")
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is LoginSideEffect.NavigateToKaKao -> {
                    /**
                     * Todo : 카카오 로그인 관련 로직 추가
                     *  로그인 성공한 경우 -> 로그인 정보를 가지고 메인 페이지로 이동
                     *  로그인 정보가 없는 경우 -> 카카오와 연동된 회원가입 -> 회원가입 정보를 가지고 sign-up 스크린으로 이동
                     */
                    Toast.makeText(context, "카카오 로그인", Toast.LENGTH_LONG).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        navController.navigate("sign-up") {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }, 500)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    PicplzTheme {
        LoginScreen(navController = navController)
    }
}