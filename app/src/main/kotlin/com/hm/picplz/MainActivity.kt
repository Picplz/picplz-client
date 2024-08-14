package com.hm.picplz

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.animation.doOnEnd
import com.hm.picplz.ui.theme.PicplzTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hm.picplz.viewmodel.MainActivityUiState
import com.hm.picplz.viewmodel.MainActivityUiState.Loading
import com.hm.picplz.viewmodel.MainActivityUiState.Success
import com.hm.picplz.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var uiState: MainActivityUiState by mutableStateOf(Loading)


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach {
                        Log.d("MainActivity", "ui state 업데이트: $it")
                        uiState = it
                    }
                    .collect()
            }
        }

        splashScreen.apply {
            setKeepOnScreenCondition {
                when (uiState) {
                    Loading -> true
                    is Success -> false
                }
            }
            setOnExitAnimationListener{ screen ->
                val fadeOut = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.ALPHA,
                    1.0f,
                    0.0f
                )
                fadeOut.duration = 1000L

                val animatorSet = AnimatorSet()
                animatorSet.play(fadeOut)
                animatorSet.doOnEnd { screen.remove() }
                animatorSet.start()
            }
        }

        enableEdgeToEdge()
        setContent {
            PicplzTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PicplzTheme {
        Greeting("Android")
    }
}