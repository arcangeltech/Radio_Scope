package marwen.project.radioscope.ui.home.fragments.accueil

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import marwen.project.radioscope.ui.home.fragments.accueil.commun.RadioItem
import marwen.project.radioscope.ui.theme.background

@Composable
fun AccueilScreen(homeNavController: NavHostController, mainNavController: NavController,viewModel: AccueilViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 50.dp)
        .background(background)) {
        LazyVerticalGrid(
            modifier = Modifier.testTag("listRadiosHome"),
            columns=GridCells.Adaptive(minSize = 160.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(8.dp)){

            item(span = {GridItemSpan(maxLineSpan)}) {
                Column {
                    Text(
                        text = "Choose your favorite radio station",
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.fillMaxWidth().height(10.dp))
                    Text(
                        text = "Recent",
                        color = Color.White,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.fillMaxWidth().height(10.dp))

                }
            }
            val onRadioClick = { listRadio:List<Radio>,radio: Radio ->
                val productJson = Uri.encode(Gson().toJson(ListenParameters(listRadio,radio)))
                mainNavController.navigate("ListenScreen/$productJson")
            }

            state.radioHomeDto?.let {
                items(it.recent) { radio ->
                    RadioItem(radio, it.recent, onRadioClick)
                }
            }
            item(span = {GridItemSpan(maxLineSpan)}) {
                Column {
                    Text(
                        text = "Featured",
                        color = Color.White,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.fillMaxWidth().height(10.dp))

                }
            }

            state.radioHomeDto?.let {
                items(it.featured) { radio ->
                    RadioItem(radio, it.featured, onRadioClick)
                }
            }
            item(span = {GridItemSpan(maxLineSpan)}) {
                Column {
                    Text(
                        text = "Random",
                        color = Color.White,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.fillMaxWidth().height(10.dp))

                }
            }

            state.radioHomeDto?.let {
                items(it.random) { radio ->
                    RadioItem(radio, it.random, onRadioClick)
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
fun AccueilScreenPreview() {
    AccueilScreen(rememberNavController(), rememberNavController())
}