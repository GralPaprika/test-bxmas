package space.carlosrdgz.test.vepormas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import space.carlosrdgz.test.vepormas.ui.navigation.AppNavHost
import space.carlosrdgz.test.vepormas.ui.theme.TestBXTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestBXTheme {
                TestBXTheme {
                    val navController = rememberNavController()
                    AppNavHost(
                        navController = navController,
                    )
                }
            }
        }
    }
}