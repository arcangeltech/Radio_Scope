package marwen.project.radioscope.ui.home.fragments.favorite.commun

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
import marwen.project.radioscope.ui.theme.background
import marwen.project.radioscope.ui.theme.background2
import marwen.project.radioscope.ui.theme.white

@Composable
fun FavoriteItem (radio: Radio, onRadioClick: (List<Radio>,Radio) -> Unit){
    Surface(
    elevation = 20.dp,
    modifier = Modifier
    .requiredHeight(120.dp)
    .fillMaxWidth()
    .padding(10.dp)
    .clickable { onRadioClick(listOf(radio),radio) },
    shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp)
    ){
        Row(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(background2),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Image(
                painter = rememberAsyncImagePainter(radio.radio_image),
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,            // crop the image if it's not a square
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .weight(1f)
            )

            Column(modifier = Modifier
                .fillMaxHeight()
                .padding(10.dp)
                .weight(2.5f)
            ) {
                Text(radio.radio_name, fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium,
                    color =  white,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis

                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    "genre: " + radio.genre + " ," + radio.country_name,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium,
                    color = accentend,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis

                )
            }


        }

    }
}
/*@Preview(showBackground = true)
@Composable
fun FavoriteItemPreview(){
    FavoriteItem(
        Radio(
            radio_id = 111990,
            radio_name = "beside 1",
            radio_image = "https://visitdpstudio.net/radio_world/upload/60435616-2022-05-09.png",
            radio_url = "http://s3.radio.co/s4e064139a/listen",
            genre = "Community",
            country_name = "Canada",
            country_id = 196
        )
    )
}*/
