package marwen.project.radioscope.ui.home.fragments.favorite

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import marwen.project.radioscope.data.local.ListenParameters
import marwen.project.radioscope.data.remote.dto.Radio
import marwen.project.radioscope.ui.home.fragments.accueil.AccueilScreen
import marwen.project.radioscope.ui.home.fragments.accueil.AccueilViewModel
import marwen.project.radioscope.ui.home.fragments.accueil.commun.RadioItem
import marwen.project.radioscope.ui.home.fragments.favorite.commun.FavoriteItem
import marwen.project.radioscope.ui.theme.background

@Composable
fun FavoriteScreen(homeNavController: NavHostController, mainNavController: NavController, viewModel: FavoriteViewModel = hiltViewModel()) {

    LaunchedEffect(Unit) {
        viewModel.getRadios()
    }
    var state =
        viewModel.state.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp)
            .background(background)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            item {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)) {
                    Text(
                        text = "Favorite radio station",
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                }
            }
            val onRadioClick = { listRadio:List<Radio>, radio: Radio ->
                val productJson = Uri.encode(Gson().toJson(ListenParameters(listRadio,radio)))
                mainNavController.navigate("ListenScreen/$productJson")
            }
            state.listFavorite?.let {
                items(it) { radio ->
                    FavoriteItem(radio,onRadioClick)
                }
            }
        }
        if (state.error.isNotBlank()) {
            Text(
                text = state.error,
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("error")
                    .padding(horizontal = 20.dp)
                    .align(Alignment.Center)
            )
        }

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
@Preview(showBackground = true)
@Composable
fun favPreview() {
    FavoriteScreen(rememberNavController(), rememberNavController())
}
