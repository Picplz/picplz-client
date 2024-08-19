package com.hm.picplz

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.animation.doOnEnd
import com.hm.picplz.ui.theme.PicplzTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.navigation.AppNavHost
import com.hm.picplz.viewmodel.MainActivityUiState
import com.hm.picplz.viewmodel.MainActivityUiState.*
import com.hm.picplz.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    private var isBackPressedOnce = false
    private val handler = Handler(Looper.getMainLooper())
    private val resetDoubleBackFlag = Runnable { isBackPressedOnce = false }

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
                    is Loading -> true
                    else -> false
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

        /** 2초 안에 뒤로가기 두번 입력 시 종료 **/
        onBackPressedDispatcher.addCallback(this) {
            if (isBackPressedOnce) {
                finishAffinity()
            } else {
                isBackPressedOnce = true
                Toast.makeText(this@MainActivity, "한 번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show()
                handler.postDelayed(resetDoubleBackFlag, 2000)
            }
        }

        setContent {
            PicplzTheme {
                val navController = rememberNavController()
                AppNavHost(
                    navController = navController,
                    uiState = uiState
                )
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(resetDoubleBackFlag)
    }

}