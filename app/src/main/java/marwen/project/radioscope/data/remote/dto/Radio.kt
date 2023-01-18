package marwen.project.radioscope.data.remote.dto

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class Radio(
    val country_id: Int,
    val country_name: String,
    val genre: String,
    val radio_id: Int,
    val radio_image: String,
    val radio_name: String,
    val radio_url: String
) : Parcelable {
    companion object NavigationType : NavType<Radio>(isNullableAllowed = false) {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun get(bundle: Bundle, key: String): Radio? {
            return bundle.getParcelable(key)
        }

        override fun parseValue(value: String): Radio {
            return Gson().fromJson(value, Radio::class.java)
        }

        override fun put(bundle: Bundle, key: String, value: Radio) {
            bundle.putParcelable(key, value)
        }
    }
}