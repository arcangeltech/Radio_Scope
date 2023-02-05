package marwen.project.radioscope.ui.home.fragments.accueil.commun

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import marwen.project.radioscope.data.remote.dto.Radio
import marwen.project.radioscope.ui.theme.accentend

@Composable
fun RadioItem(radio: Radio,list: List<Radio>, onRecipeClick: (List<Radio>,Radio) -> Unit){
    Surface(
        elevation = 4.dp,
        modifier = Modifier
            .requiredHeight(200.dp)
            .requiredWidth(160.dp)
            .testTag(Radio.name)
            .clickable { onRecipeClick(list,radio) },
        shape = RoundedCornerShape(20.dp)
    ){
        Box(contentAlignment = Alignment.BottomStart,modifier = Modifier
            .fillMaxSize()) {
            Image(
                painter = rememberAsyncImagePainter(radio.radio_image),
                contentDescription = "avatar",
                contentScale = ContentScale.FillBounds,            // crop the image if it's not a square
                modifier = Modifier
                    .fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0x00FFFFFF),
                                Color(0xFF202028)
                            )
                        )
                    )
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    radio.radio_name,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 10.dp),
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis

                )
                Text(
                    "genre: "+radio.genre +" ,"+radio.country_name,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium,
                    color = accentend,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(10.dp,0.dp,10.dp,10.dp),
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis

                )

            }
        }

    }
}
@Preview
@Composable
fun RadioItemPreview(){
    /*RadioItem(Radio(20,"Tunisia","music",22,"https://visitdpstudio.net/radio_world/upload/47801872-2022-03-19.png",
    "test","http://stream.dbmedia.se/gk80talMP3"), emptyList(),{})*/

}
