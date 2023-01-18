package marwen.project.radioscope.data.local

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import marwen.project.radioscope.data.remote.dto.Radio

@Parcelize
data class ListenParameters(val listRadio: List<Radio>,val radio: Radio) : Parcelable {
    companion object NavigationType : NavType<ListenParameters>(isNullableAllowed = false) {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun get(bundle: Bundle, key: String): ListenParameters {
            return bundle.getParcelable(key)!!
        }

        override fun parseValue(value: String): ListenParameters {
            return Gson().fromJson(value, ListenParameters::class.java)
        }

        override fun put(bundle: Bundle, key: String, value: ListenParameters) {
            bundle.putParcelable(key, value)
        }
    }
}
