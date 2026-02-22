package com.wouterdevriendt.trivit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.wouterdevriendt.trivit.ui.navigation.TrivitNavHost
import com.wouterdevriendt.trivit.ui.theme.TrivitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrivitTheme {
                TrivitNavHost()
            }
        }
    }
}
