package marwen.project.radioscope.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import marwen.project.radioscope.R
import marwen.project.radioscope.ui.home.fragments.accueil.AccueilScreen
import marwen.project.radioscope.ui.home.fragments.favorite.FavoriteScreen
import marwen.project.radioscope.ui.theme.background2

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    val homeNavController = rememberNavController()
    Scaffold(

        bottomBar = {
            BottomNavigationBar(homeNavController)

        }

    ) {
        Navigation(homeNavController = homeNavController, mainNavController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    //AccueilScreen()
}

@Composable
fun Navigation(homeNavController: NavHostController, mainNavController: NavController) {
    NavHost(homeNavController, startDestination = NavigationItem.Accueil.route) {
        composable(NavigationItem.Accueil.route) {
            AccueilScreen(homeNavController,mainNavController )
        }
        composable(NavigationItem.Favorite.route) {
            FavoriteScreen(homeNavController,mainNavController )
        }
        /*composable(NavigationItem.Shopping.route) {
            ShoppingScreen(homeNavController,mainNavController)
        }
        composable(NavigationItem.MealPlan.route) {
            MealPlannerScreen(homeNavController,mainNavController )
        }
        composable(NavigationItem.Profile.route) {
            ProfileScreen(homeNavController,mainNavController )
        }*/
    }
}


@Composable
fun BottomNavigationBar(homeNavController: NavController) {
    val items = listOf(
        NavigationItem.Accueil,
        NavigationItem.Favorite,
        NavigationItem.Search,
        NavigationItem.Quotes
    )
    BottomNavigation(
        backgroundColor = background2,
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)),
        contentColor = Color.White

    ) {
        val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selectedContentColor = colorResource(id = R.color.accentend),
                unselectedContentColor = Color.White.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    homeNavController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        homeNavController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    BottomNavigationBar(rememberNavController())
}
