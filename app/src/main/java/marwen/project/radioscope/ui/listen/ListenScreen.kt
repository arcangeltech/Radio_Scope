package marwen.project.radioscope.ui.listen

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import marwen.project.radioscope.data.local.ListenParameters
import marwen.project.radioscope.data.remote.dto.Radio
import marwen.project.radioscope.services.AudioService
import marwen.project.radioscope.services.PlayerStatus
import marwen.project.radioscope.ui.theme.accentend
import marwen.project.radioscope.ui.theme.accentstart
import marwen.project.radioscope.ui.theme.background
import marwen.project.radioscope.ui.theme.header
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
inline fun ListenScreen(mainNavController: NavController,  listenParameters:ListenParameters, viewModel: ListenViewModel = hiltViewModel()
) {

    //check favorite
    val isFavorite= remember {
        viewModel.isFavorite
    }

    //player service
    var audioService: AudioService? = null
    var state by remember {
        mutableStateOf(audioService?.playerStatusLiveData?.value)
    }

    /*LaunchedEffect(Unit) {
        launch {
            snapshotFlow { state }.collect()
        }

    }*/
    var command = remember {mutableStateOf("")}
    var playlist = remember {
        mutableStateOf(listenParameters)
    }
    val coroutineScope = rememberCoroutineScope()

    val connection = remember {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as AudioService.AudioServiceBinder
                audioService = binder.service
                state = audioService?.playerStatusLiveData?.value
                // Pass player updates to interested observers.
                audioService?.playerStatusLiveData?.value.let { if (state!!.isCancelled){
                    audioService?.pause()
                    audioService = null
                }

                }

            }


            override fun onServiceDisconnected(name: ComponentName?) {
                audioService = null
            }
        }
    }

        AudioService.newIntent(LocalContext.current, radio = playlist.value.radio, command = command.value).also { intent ->
            // This service will get converted to foreground service using the PlayerNotificationManager notification Id.
            LocalContext.current.startService(intent)
            LocalContext.current.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }


    //*******//
    Column(modifier = Modifier
        .fillMaxSize()
        .background(background), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .clickable { },
                tint = header,
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "back button"
            )
            Text(
                "Playing",
                color = header,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
            if(isFavorite.value){
                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { viewModel.deleteFavorite(radio = listenParameters.radio) },
                    tint = header,
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "favorite"
                )
            }else{
                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { viewModel.addFavorite(radio = listenParameters.radio) },
                    tint = header,
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "favorite"
                )
            }

        }
        var pagerState =  remember {
            PagerState(playlist.value.listRadio.indexOf(playlist.value.radio))
        }
        HorizontalPager(
            state = pagerState,
            count = playlist.value.listRadio.size,
            contentPadding = PaddingValues(100.dp)
        ) { page ->
            Card(elevation = 4.dp,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .graphicsLayer {
                        // Calculate the absolute offset for the current page from the
                        // scroll position. We use the absolute value which allows us to mirror
                        // any effects for both directions
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                        // We animate the scaleX + scaleY, between 85% and 100%
                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }

                        // We animate the alpha, between 50% and 100%
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
            ) {
                Image(
                    painter = rememberAsyncImagePainter(playlist.value.listRadio.get(page).radio_image),
                    contentDescription = "avatar",
                    contentScale = ContentScale.FillBounds,            // crop the image if it's not a square
                    modifier = Modifier
                        .requiredHeight(300.dp)
                        .requiredWidth(200.dp)
                        .clickable {
                            command.value = "play"
                            audioService?.exoPlayer?.play()
                            playlist.value =
                                ListenParameters(
                                    playlist.value.listRadio,
                                    playlist.value.listRadio.get(page)
                                )
                            state = PlayerStatus(
                                isPlaying = true,
                                radioId = playlist.value.listRadio.get(page).radio_id.toString()
                            )
                        }
                )
            }
        }
        Text(
            playlist.value.radio.radio_name,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(horizontal = 10.dp),
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis

        )
        Text(
            "genre: " + playlist.value.radio.genre + " ," + playlist.value.radio.country_name,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            color = accentend,
            fontSize = 15.sp,
            modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp),
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis

        )
        Spacer(modifier = Modifier.height(50.dp))
        state?.let {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.width(60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                command.value = "play"
                                audioService?.exoPlayer?.play()
                                val index = playlist.value.listRadio.indexOf(playlist.value.radio)
                                if (index > 0) {
                                    coroutineScope.launch {
                                        pagerState.scrollToPage(index - 1)
                                    }
                                    playlist.value =
                                        ListenParameters(
                                            playlist.value.listRadio,
                                            playlist.value.listRadio.get(index - 1)
                                        )
                                    state = PlayerStatus(
                                        isPlaying = true,
                                        radioId = playlist.value.listRadio.get(index - 1).radio_id.toString()
                                    )
                                }
                            },
                        tint = header,
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "prev"
                    )
                    Text(
                        "prev",
                        modifier = Modifier.fillMaxWidth(),
                        color = header,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Icon(modifier = Modifier
                    .size(50.dp)
                    .drawBehind {
                        drawCircle(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    accentstart,
                                    accentend
                                )
                            ), radius = this.size.maxDimension
                        )
                    }
                    .clickable {
                        if (state!!.isPlaying) {
                            command.value = "pause"
                            audioService?.exoPlayer?.stop()
                            state = PlayerStatus(isPaused = true, radioId = state!!.radioId)
                        } else {
                            command.value = "play"
                            audioService?.exoPlayer?.play()
                            state = PlayerStatus(isPlaying = true, radioId = state!!.radioId)
                        }
                    },
                    tint = Color.White,
                    imageVector = if (state!!.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = "play"
                )
                Column(
                    modifier = Modifier.width(60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                command.value = "play"
                                audioService?.exoPlayer?.play()
                                val index = playlist.value.listRadio.indexOf(playlist.value.radio)
                                if (index < playlist.value.listRadio.size - 1) {
                                    coroutineScope.launch {
                                        pagerState.scrollToPage(index + 1)
                                    }

                                    playlist.value =
                                        ListenParameters(
                                            playlist.value.listRadio,
                                            playlist.value.listRadio.get(index + 1)
                                        )
                                    state = PlayerStatus(
                                        isPlaying = true,
                                        radioId = playlist.value.listRadio.get(index + 1).radio_id.toString()
                                    )
                                }
                            },
                        tint = header,
                        imageVector = Icons.Filled.KeyboardArrowRight,
                        contentDescription = "next"
                    )
                    Text(
                        "next",
                        modifier = Modifier.fillMaxWidth(),
                        color = header,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }

            }

        }
    }


}


@Preview(showBackground = true)
@Composable
fun AccueilScreenPreview() {
    val featured = listOf(
        Radio(
            radio_id = 56585,
            radio_name = "RUM - Radio Universitaria do Minho",
            radio_image ="https://visitdpstudio.net/radio_world/upload/36032912-2022-03-16.png",
            radio_url ="http://centova.radios.pt:9558/stream",
            genre="Talk",
            country_name= "Portugal",
            country_id= 76
        ),Radio(
            radio_id = 56585,
            radio_name = "RUM - Radio Universitaria do Minho",
            radio_image ="https://visitdpstudio.net/radio_world/upload/36032912-2022-03-16.png",
            radio_url ="http://centova.radios.pt:9558/stream",
            genre="Talk",
            country_name= "Portugal",
            country_id= 76
        ),Radio(
            radio_id = 56585,
            radio_name = "RUM - Radio Universitaria do Minho",
            radio_image ="https://visitdpstudio.net/radio_world/upload/36032912-2022-03-16.png",
            radio_url ="http://centova.radios.pt:9558/stream",
            genre="Talk",
            country_name= "Portugal",
            country_id= 76
        ),Radio(
            radio_id = 56585,
            radio_name = "RUM - Radio Universitaria do Minho",
            radio_image ="https://visitdpstudio.net/radio_world/upload/36032912-2022-03-16.png",
            radio_url ="http://centova.radios.pt:9558/stream",
            genre="Talk",
            country_name= "Portugal",
            country_id= 76
        ),Radio(
            radio_id = 56585,
            radio_name = "RUM - Radio Universitaria do Minho",
            radio_image ="https://visitdpstudio.net/radio_world/upload/36032912-2022-03-16.png",
            radio_url ="http://centova.radios.pt:9558/stream",
            genre="Talk",
            country_name= "Portugal",
            country_id= 76
        ))
    ListenScreen(rememberNavController(), ListenParameters(featured,featured.get(1)))
}