package marwen.project.radioscope.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(var route: String, var icon: ImageVector, var title: String) {
    object Accueil : NavigationItem("accueil", Icons.Outlined.Home, "Accueil")
    object Favorite : NavigationItem("favorite", Icons.Outlined.Favorite ,"Favorite")
    object Search : NavigationItem("search", Icons.Outlined.Search, "Search")
    object Quotes : NavigationItem("quotes", Icons.Outlined.Info, "Quotes")
}