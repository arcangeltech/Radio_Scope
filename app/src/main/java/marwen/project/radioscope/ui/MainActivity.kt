package marwen.project.radioscope.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.NavType
import androidx.navigation.navArgument
import marwen.project.radioscope.data.local.ListenParameters
import marwen.project.radioscope.data.remote.dto.Radio
import marwen.project.radioscope.ui.home.HomeScreen
import marwen.project.radioscope.ui.listen.ListenScreen
import marwen.project.radioscope.ui.splash.SplashScreen
import marwen.project.radioscope.ui.theme.RadioScopeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RadioScopeTheme() {

                MainScreen()

            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splashScreen") {
        composable("splashScreen") {
            SplashScreen(navController= navController)
        }
        composable("HomeScreen") {
            HomeScreen(navController = navController)
        }
        /*composable("LoginScreen") {
            LoginScreen(navController = navController)
        }*/
        /*composable("DetailScreen/{result}",arguments = listOf(navArgument("result") { type = NavType.ParcelableType(Result::class.java) })){

            DetailScreen(navController = navController, it.arguments!!.putParcelable("result",resu)<Result>("result"))

        }*/
        /*composable("DetailScreen") { backStackEntry ->
            val result = navController.previousBackStackEntry?.arguments?.getParcelable<Result>("result")

            DetailScreen(navController = navController,result!!) // Here UserDetailsView is a composable.
        }*/
        composable(
            route = "ListenScreen/{listRadio}",
            arguments = listOf(
                navArgument("listRadio") {
                    type = ListenParameters
                }
            )
        ) { backStackEntry ->
            val listRadio = backStackEntry.arguments?.getParcelable("listRadio") as ListenParameters?
            ListenScreen(mainNavController = navController,listRadio!!) // Here UserDetailsView is a composable.
        }

    }

}

