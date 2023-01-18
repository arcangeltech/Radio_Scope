package marwen.project.radioscope.ui.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import marwen.project.radioscope.R


@Composable
fun SplashScreen(navController: NavController) {
    val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }

    // AnimationEffect
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.7f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                })
        )
        delay(1500L)
        navController.navigate("HomeScreen")

    }

    // Image
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.bg),
            contentDescription = "bg",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .matchParentSize()
        )
        Image(painter = painterResource(id = R.drawable.devider),
            contentDescription = "Logo",
            contentScale =ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .align(Alignment.TopCenter)
        )

        Image(painter = painterResource(id = R.drawable.radio_live),
            contentDescription = "Logo",
            contentScale =ContentScale.Fit,
            modifier = Modifier
                .size(300.dp)
                .padding(bottom = 20.dp)
                .align(Alignment.BottomEnd)
                .scale(scale.value))

    }
}


@Preview
@Composable
fun Preview() {
    SplashScreen(rememberNavController())
}
