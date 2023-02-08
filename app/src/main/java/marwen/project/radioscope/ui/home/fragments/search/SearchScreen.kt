package marwen.project.radioscope.ui.home.fragments.search

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import marwen.project.radioscope.ui.home.fragments.favorite.FavoriteViewModel
import marwen.project.radioscope.ui.home.fragments.favorite.commun.FavoriteItem
import marwen.project.radioscope.ui.theme.accentend
import marwen.project.radioscope.ui.theme.accentstart
import marwen.project.radioscope.ui.theme.background
import marwen.project.radioscope.ui.theme.background2

@Composable
fun SearchScreen(homeNavController: NavHostController, mainNavController: NavController, viewModel: SearchViewModel = hiltViewModel()){

    Column(modifier= Modifier.fillMaxSize()
        .padding(bottom = 50.dp)
        .background(background)) {
        Spacer(modifier = Modifier.height(10.dp))

        var textState by remember { mutableStateOf("") }
        val maxLength = 110
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .requiredHeight(55.dp)) {
            BasicTextField(
                modifier =Modifier.weight(3f),
                value = textState,
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                onValueChange = { if(it.length< maxLength) textState = it },
                decorationBox = { innerTextField ->
                    Row(
                        Modifier
                            .background(background2, RoundedCornerShape(percent = 30))
                            .padding(7.dp)
                    ) {

                        if (textState.isEmpty()) {
                            Text("Search Radio...", color = Color.White, fontSize = 12.sp)
                        }
                        // <-- Add this
                        innerTextField()
                    }
                },
            )
            Spacer(modifier = Modifier.width(15.dp))
            val context = LocalContext.current
            IconButton(
                onClick = {
                    if (!textState.isNullOrEmpty()){
                        viewModel.searchRadios(textState,"","1")
                    }
                },
                modifier = Modifier
                    .size(30.dp)
                    .background(accentstart, RoundedCornerShape(8.dp))


            )
            {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Search Button",
                    tint = Color.White,
                    //modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(0.dp))
        val radioListState = rememberLazyListState()
        val state by remember { viewModel.state }
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(state=radioListState,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                contentPadding = PaddingValues(8.dp)
            ) {

                val onRadioClick = { listRadio:List<Radio>, radio: Radio ->
                    val productJson = Uri.encode(Gson().toJson(ListenParameters(listRadio,radio)))
                    mainNavController.navigate("ListenScreen/$productJson")
                }
                state.listSearch?.let {
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
}

@Preview(showBackground = true)
@Composable
fun showsearch(){
    SearchScreen(homeNavController = rememberNavController(), mainNavController = rememberNavController())
}